package txlabz.com.geoconfess.service;


import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.otto.Subscribe;

import java.util.Calendar;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.constants.ApiConstants;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.CreatedDynamicSpotEvent;
import txlabz.com.geoconfess.events.DeletedDynamicSpotEvent;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.models.request.CreateSpotRequestModel;
import txlabz.com.geoconfess.models.request.UpdateSpotRequestModel;
import txlabz.com.geoconfess.network.requests.CreateSpotRequest;
import txlabz.com.geoconfess.network.requests.DeleteSpotRequest;
import txlabz.com.geoconfess.network.requests.UpdateSpotRequest;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;


/**
 * This is the service class which handles the location tracking using Google Fused Api
 *
 * @author Miroslav
 * @version 1.0
 * @since 2016-05-04
 */

public class MyLocationTracker extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = MyLocationTracker.class.getSimpleName();
    private static final float KMH_CALCULATE_CONSTANT = 3.6f;
    private static final long TIME_INTERVAL = 1000;//36000000; //10 min interval
    private static final long UPDATE_TIME_INTERVAL = 10000;//36000000; //10 min interval
    private static final int CREATE_SPOT_ACTION = 1;
    private static final int UPDATE_SPOT_ACTION = 2;
    private static final int DELETE_SPOT_ACTION = 3;
    private static final long JOB_DELAY = 1000;
    private static final float MEASURE_MOVE_LENGTH = 100f; //moving distance 100m
    public static boolean serviceRunning;
    private final IBinder mBinder = new LocationBinder();
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location oldLocation;
    private boolean isSpotCreated = false;
    private NotificationManager mNotificationManager;
    private int spotId = 0;
    private int currentAction;
    private long previousUpdate = 0;

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceRunning = true;
        try {
            BusProvider.getInstance().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferenceUtils.setTrackServiceRunning(this, true);
        buildGoogleApiClient();
        createLocationRequest();
        receiveIntentExtras(intent);
        return Service.START_NOT_STICKY;
    }

    private void receiveIntentExtras(Intent intent) {
        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(TIME_INTERVAL);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "onConnected");
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        //Used for testing purpose
//        Utils.playTestSound(this);
        float[] results = new float[1];
        if (oldLocation != null) {
            Location.distanceBetween(oldLocation.getLatitude(), oldLocation.getLongitude(),
                    location.getLatitude(), location.getLongitude(), results);
        }

        Log.i(TAG, "distance: " + results[0] + " speed: " + location.getSpeed() * KMH_CALCULATE_CONSTANT);
        long timeNow = Calendar.getInstance().getTimeInMillis();
        if ((timeNow - previousUpdate) > UPDATE_TIME_INTERVAL || results[0] > MEASURE_MOVE_LENGTH || oldLocation == null) {
            previousUpdate = timeNow;
            oldLocation = location;
            Log.i(TAG, "locationChanged " + location.getLatitude() + ", " + location.getLongitude());
            if (!isSpotCreated) {
                createSpot(location);
            } else {
                updateSpot(location);
            }
        }
    }

    @Override
    public boolean stopService(Intent name) {
        //Check if we have active spot id to delete
        if (spotId != 0)
            deleteSpot();


        try {
            BusProvider.getInstance().unregister(this);
            serviceRunning = false;
            SharedPreferenceUtils.setTrackServiceRunning(this, false);
            Log.i(TAG, "onStopService");
            stopLocationUpdates();
            stopSelf();
        } catch (Exception e) {
            Log.e("Tracker Issue", e.getLocalizedMessage());


        }

        return super.stopService(name);
    }

    private void dismissNotification(int notificationId) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notificationId);
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void deleteSpot() {
        currentAction = DELETE_SPOT_ACTION;
        isSpotCreated = false;
        previousUpdate = 0;
        oldLocation = null;
        Log.i(TAG, "send request for deleting spot");


        DeleteSpotRequest.deleteSpot(spotId, SharedPreferenceUtils.getAccessToken(this), ApiConstants.DYNAMIC_SPOT);
    }

    private void createSpot(Location location) {
        currentAction = CREATE_SPOT_ACTION;
        Log.i(TAG, "send request for creating spot");
        CreateSpotRequestModel createRequest = new CreateSpotRequestModel(SharedPreferenceUtils.getUserEmail(this), ApiConstants.DYNAMIC_SPOT,
                String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), SharedPreferenceUtils.getAccessToken(this));
        CreateSpotRequest.createDynamicSpot(createRequest);
    }

    private void updateSpot(Location location) {
        currentAction = UPDATE_SPOT_ACTION;
        Log.i(TAG, "send request for updating spot");
        UpdateSpotRequestModel updateRequest = new UpdateSpotRequestModel(spotId, String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()), SharedPreferenceUtils.getAccessToken(this));
        UpdateSpotRequest.update(updateRequest);
    }

    @Subscribe
    public void onEvent(CreatedDynamicSpotEvent event) {
        isSpotCreated = true;
        spotId = event.getResponse().getId();
    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        switch (currentAction) {
            case CREATE_SPOT_ACTION:
                repeatCreateJob();
                break;
            case UPDATE_SPOT_ACTION:
                break;
            case DELETE_SPOT_ACTION:
                deleteSpot();
                break;
        }
    }

    @Subscribe
    public void onEvent(DeletedDynamicSpotEvent event) {
        Toast.makeText(MyLocationTracker.this, R.string.spot_deleted, Toast.LENGTH_SHORT).show();
    }

    private void repeatCreateJob() {
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "repeat create job");
                createSpot(oldLocation);
            }
        };
        new Handler().postDelayed(runnableCode, JOB_DELAY);

    }

    public class LocationBinder extends Binder {
        public MyLocationTracker getService() {
            return MyLocationTracker.this;
        }
    }
}

package txlabz.com.geoconfess.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.otto.Subscribe;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.constants.ApiConstants;
import txlabz.com.geoconfess.constants.AppConstants;
import txlabz.com.geoconfess.events.LogoutEvent;
import txlabz.com.geoconfess.events.SpotsAroundEvent;
import txlabz.com.geoconfess.events.UserEvent;
import txlabz.com.geoconfess.helpers.ShowMapSpotsHelper;
import txlabz.com.geoconfess.models.response.SpotResponse;
import txlabz.com.geoconfess.network.requests.SpotsAroundRequest;
import txlabz.com.geoconfess.network.requests.UserDataRequest;
import txlabz.com.geoconfess.service.PushNotificationService;
import txlabz.com.geoconfess.utils.DialogUtility;
import txlabz.com.geoconfess.utils.LocationUtils;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;
import txlabz.com.geoconfess.views.SupportMap;

/**
 * Created by yagor on 5/3/2016.
 */
public class HomeFragment extends BaseFragment implements GoogleMap.OnMarkerClickListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute
    private static final int REQUEST_CODE_LOCATION = 2;
    static public Double longitude = null;
    static public Double latitude = null;
    SupportMap mMapFragment;
    @BindView(R.id.priest_login_warning)
    TextView mPriestWarning;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Location location;
    private Handler handler;
    final Runnable constantFetch = new Runnable() {
        public void run() {
            fetchSpots();
            handler.postDelayed(this, AppConstants.FETCH_SPOT_INTERVAL);
        }
    };
    private HashMap<Marker, SpotResponse> markerMap;

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderDisabled(String arg0) {

        }

        @Override
        public void onLocationChanged(Location location) {

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);
        getGps();
        if (mMapFragment == null) {
            mMapFragment = new SupportMap();

            SupportMap.MapViewCreatedListener mapViewCreatedListener = new SupportMap.MapViewCreatedListener() {
                @Override
                public void onMapCreated() {
                    googleMap = mMapFragment.getMap();
                    if (googleMap != null) {
                        upadte();
                    }

                }
            };
            mMapFragment.itsMapViewCreatedListener = mapViewCreatedListener;
            FragmentManager fm = getChildFragmentManager();
            SupportMapFragment supportMapFragment = mMapFragment;
            fm.beginTransaction().replace(R.id.map, supportMapFragment).commit();

        }
        handler = new Handler();
        handler.postDelayed(constantFetch, 0);

        checkUserData();
        //Below method already called in Handler, So no need of here?
//        fetchSpots();
        return view;
    }

    private void fetchSpots() {
        boolean active = latitude != null && longitude != null;
        if (active) {
            SpotsAroundRequest.getSpotsAround(SharedPreferenceUtils.getAccessToken(getActivity()), AppConstants.ACTIVE_NOW, latitude, longitude, AppConstants.SPOT_DISTANCE);
        } else {
            SpotsAroundRequest.getSpotsAround(SharedPreferenceUtils.getAccessToken(getActivity()), null, null, null, null);
        }
    }

    private void checkUserData() {
//        if (SharedPreferenceUtils.getLogInRole(getActivity()).isEmpty() || SharedPreferenceUtils.getUserId(getActivity()) < AppConstants.NOT_VALID_ID) {
        UserDataRequest.getUserData(SharedPreferenceUtils.getAccessToken(getActivity()));
//        } else {
//            checkRole();
//        }
    }

    private void checkRole() {
        if (TextUtils.equals(SharedPreferenceUtils.getLogInRole(getActivity()), AppConstants.PRIEST_ROLE) &&
                !SharedPreferenceUtils.isTrackServiceRunning(getActivity())) {
            ((MainActivity) getActivity()).checkIfServiceRunning();
        }

    }

    public void upadte() {
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        }

        googleMap.setMyLocationEnabled(true);

        if (LocationUtils.isGpsOn(getActivity())) {
            LatLng TutorialsPoint = new LatLng(location.getLatitude(), location.getLongitude());
            pointToPosition(TutorialsPoint);
        }

    }

    private void pointToPosition(LatLng position) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(16).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Subscribe
    public void onEvent(UserEvent event) {
        if (event.getUser().getRole() != null) {
            SharedPreferenceUtils.setUserId(getActivity(), event.getUser().getId());
            SharedPreferenceUtils.setLogInRole(getActivity(), event.getUser().getRole());
            SharedPreferenceUtils.setUserNameSurname(getActivity(), getString(R.string.two_resources_format,
                    event.getUser().getName(),
                    event.getUser().getSurname()));

            PushNotificationService.subscribeOnUserPushes(event.getUser().getId());
            ((MainActivity) getActivity()).setHeaderName();
            if (!event.getUser().isActive())
                ((MainActivity) getActivity()).showInactiveView();
            checkRole();
        }
    }

    @Subscribe
    public void onEvent(LogoutEvent event) {
        long userId = SharedPreferenceUtils.getUserId(getActivity());
        PushNotificationService.unsubscribeFromUserPushes(userId);
    }

    @Override
    public void onResume() {
        super.onResume();
        getGps();
    }

    private void getGps() {

        try {

            locationManager = (LocationManager) getActivity().getSystemService(
                    Context.LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            System.out.println("isgps en:" + isGPSEnabled);


            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            System.out.println("isgps nt en:" + isNetworkEnabled);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled

                System.out.println("no network called");

            } else {

                // this.canGetLocation = true;
                // First get location from Network Provider
                System.out.println("can get");

                if (isNetworkEnabled) {

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
                    }

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                    // locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,
                    // locationListener, null);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            ((MainActivity) getActivity()).setMyLocation(latitude, longitude);
                            System.out.println("longitude:" + longitude);
                            System.out.println("lotitude:" + latitude);


                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                locationListener);

                        Log.d("GPS Enabled", "GPS Enabled");

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location
                                        .getLatitude();
                                longitude = location
                                        .getLongitude();
                                ((MainActivity) getActivity()).setMyLocation(latitude, longitude);
                                System.out.println("longitude:" + longitude);
                                System.out.println("lotitude:" + latitude);

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void onEvent(SpotsAroundEvent event) {
        if (markerMap != null) {
            markerMap.clear();
        }
        markerMap = ShowMapSpotsHelper.updateMapSpots(getActivity(), googleMap, event.getResponse());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        SpotResponse spot = markerMap.get(marker);
        //  Toast.makeText(getActivity(), spot.getName(), Toast.LENGTH_SHORT).show();

        if (TextUtils.equals(spot.getActivity_type(), ApiConstants.DYNAMIC_SPOT)) {
            if (spot.getPriest().getId() == SharedPreferenceUtils.getUserId(getActivity())) {
                DialogUtility.showDialog(getActivity(), getString(R.string.geo_location), getString(R.string.service_start_toast_message));
            } else {
                sendConfessionRequest(spot);
            }
        } else {
            sendConfessionRequest(spot);
        }

        return false;
    }

    private void sendConfessionRequest(SpotResponse spot) {
        UserChatInitiate1Fragment fragment = new UserChatInitiate1Fragment();
        Bundle args = new Bundle();
        args.putDouble(AppConstants.MY_LATITUDE, latitude);
        args.putDouble(AppConstants.MY_LONGITUDE, longitude);
        args.putParcelable(AppConstants.SPOT_PARCELABLE, spot);
        fragment.setArguments(args);
        ((MainActivity) getActivity()).loadFragment(fragment, true, true);
    }

    @OnClick(R.id.priest_login_warning)
    public void onIntroMessageClick() {
        mPriestWarning.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        try {
            handler.removeCallbacks(constantFetch);
        } catch (Exception e) {
            Log.d(getString(R.string.app_name), e.getLocalizedMessage());
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        try {
            handler.removeCallbacks(constantFetch);
        } catch (Exception e) {
            Log.d(getString(R.string.app_name), e.getLocalizedMessage());
        }
        super.onDestroy();
    }
}

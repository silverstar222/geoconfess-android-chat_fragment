package txlabz.com.geoconfess.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import java.util.List;
import java.util.Locale;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.MainActivity;
import txlabz.com.geoconfess.adapters.PlaceAutocompleteAdapter;
import txlabz.com.geoconfess.constants.ApiConstants;
import txlabz.com.geoconfess.events.CreatedStaticSpotEvent;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.models.request.CreateSpotRequestModel;
import txlabz.com.geoconfess.network.requests.CreateSpotRequest;
import txlabz.com.geoconfess.utils.DialogUtility;
import txlabz.com.geoconfess.utils.KeyBoardUtils;
import txlabz.com.geoconfess.utils.SharedPreferenceUtils;
import txlabz.com.geoconfess.views.SupportMap;

/**
 * Created by yagor on 5/5/2016.
 */

public class SpotCreationStep3Fragment extends BaseFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = SpotCreationStep3Fragment.class.getSimpleName();
    private static final int REQUEST_CODE_LOCATION = 2;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute
    static public String longitude = " ";
    static public String latitude = " ";
    private static LatLngBounds BOUNDS_GREATER_SYDNEY = null;
    protected GoogleApiClient mGoogleApiClient;
    protected boolean gps_enabled, network_enabled;
    String lat;
    String selectedAddress;
    String provider;
    SupportMap mMapFragment;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private GoogleMap googleMap;
    //	private String state;
    private LocationManager locationManager;
    private Location location;
    private LatLng selectedPosition;
    private String newSpotName = "", mCity = "", mCountry = "", mStreet = "";
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                places.release();
                return;
            }
            final Place place = places.get(0);

            selectedAddress = mAutocompleteView.getText().toString();

            pointToPosition(place.getLatLng());
            places.release();
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            KeyBoardUtils.hide(mAutocompleteView);
        }
    };
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
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());


            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spotcreation_step3, container, false);

        getGps();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), 0 /* clientId */, this)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }
        mAutocompleteView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete);

        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        mAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteView.setAdapter(mAdapter);


        if (mMapFragment == null) {
            mMapFragment = new SupportMap();

            SupportMap.MapViewCreatedListener mapViewCreatedListener = new SupportMap.MapViewCreatedListener() {
                @Override
                public void onMapCreated() {
                    googleMap = mMapFragment.getMap();
                    if (googleMap != null) {
                        update();
                    }

                }
            };
            mMapFragment.itsMapViewCreatedListener = mapViewCreatedListener;

            FragmentManager fm = getChildFragmentManager();
            SupportMapFragment supportMapFragment = mMapFragment;
            fm.beginTransaction().replace(R.id.map, supportMapFragment).commit();

        }

        return view;
    }

    private void update() {

        try {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            }

            googleMap.setMyLocationEnabled(true);
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker marker) {

                    location.setLatitude(marker.getPosition().latitude);
                    location.setLongitude(marker.getPosition().longitude);

                    // Getting view from the layout file info_window_layout
                    View v = getActivity().getLayoutInflater().inflate(R.layout.windowmarker, null);

                    TextView address = (TextView) v.findViewById(R.id.address);
                    selectedAddress = getCompleteAddressString(location.getLatitude(), location.getLongitude());
                    address.setText(selectedAddress);
                    Button add = (Button) v.findViewById(R.id.add);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            selectedPosition = marker.getPosition();
                            dialogshow();
                        }
                    });
                    return v;
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // Clears any existing markers from the GoogleMap
                googleMap.clear();

                // Creating an instance of MarkerOptions to set position
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting position on the MarkerOptions
                markerOptions.position(arg0);

                // Animating to the currently touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));

                // Adding marker on the GoogleMap
                Marker marker = googleMap.addMarker(markerOptions);

                // Showing InfoWindow on the GoogleMap
                marker.showInfoWindow();


            }
        });
        if(location!=null){
            LatLng TutorialsPoint = new LatLng(location.getLatitude(), location.getLongitude());

            selectedAddress = getCompleteAddressString(location.getLatitude(), location.getLongitude());
            pointToPosition(TutorialsPoint);

        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(getActivity(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    private void pointToPosition(LatLng position) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(14).build();
        googleMap.addMarker(new MarkerOptions()
                .position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer)));

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
                            latitude = String.valueOf(location.getLatitude());
                            longitude = String.valueOf(location.getLongitude());
                            System.out.println("longitude:" + longitude);
                            System.out.println("lotitude:" + latitude);
                            BOUNDS_GREATER_SYDNEY = new LatLngBounds(
                                    new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(location.getLatitude(), location.getLongitude()));
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
                                latitude = String.valueOf(location
                                        .getLatitude());
                                longitude = String.valueOf(location
                                        .getLongitude());
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
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                mCity = returnedAddress.getSubLocality();
                mCountry = returnedAddress.getCountryName();
                mStreet = returnedAddress.getFeatureName();
//                strReturnedAddress.append(returnedAddress.getSubLocality()).append("\n");
//                strReturnedAddress.append(returnedAddress.getFeatureName()).append("\n");
//                strReturnedAddress.append(returnedAddress.getCountryName());

                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }

    private void dialogshow() {
        final Dialog dialog2 = new Dialog(getActivity());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog2.setContentView(R.layout.dialogshow);
        dialog2.show();

        Button ok = (Button) dialog2.findViewById(R.id.ok);
        Button cancel = (Button) dialog2.findViewById(R.id.cancel);
        final EditText spotname = (EditText) dialog2.findViewById(R.id.spotname);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();

                if (!spotname.getText().toString().equalsIgnoreCase("")) {
                    try {
                        ((MainActivity) getActivity()).showDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    newSpotName = String.valueOf(spotname.getText());
                    CreateSpotRequestModel requestModel = new CreateSpotRequestModel(newSpotName,
                            ApiConstants.STATIC_SPOT, "" + selectedPosition.latitude,
                            "" + selectedPosition.longitude, SharedPreferenceUtils.getAccessToken(getActivity()), mStreet, mCity, mCountry);
                    CreateSpotRequest.createStaticSpot(requestModel);
                } else {
                    DialogUtility.showDialog(getActivity(), getString(R.string.warning), getString(R.string.enter_spotname));
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {

    }

    @Subscribe
    public void onEvent(CreatedStaticSpotEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        //((MainActivity) getActivity()).closeCurrentFragment();
        SpotCreationStep4Fragment f = new SpotCreationStep4Fragment();
        Bundle b = new Bundle();
        b.putInt(ApiConstants.ENTITY_ID, event.getResponse().getId());

        f.setArguments(b);
        ((MainActivity) getActivity()).loadFragment(f, true, true);
    }

    @Subscribe
    public void onEvent(ErrorResponseEvent event) {
        ((MainActivity) getActivity()).hideDialog();
        Toast.makeText(getActivity(), event.getError().getErrors(), Toast.LENGTH_SHORT).show();
    }
}

package txlabz.com.geoconfess.helpers;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.constants.ApiConstants;
import txlabz.com.geoconfess.models.response.SpotResponse;

/**
 * Created by Ivan on 1.6.2016..
 */
public class ShowMapSpotsHelper {

    public static HashMap<Marker, SpotResponse> updateMapSpots(Context context, GoogleMap googleMap, SpotResponse[] response) {
        HashMap<Marker, SpotResponse> mMarkersHashMap = new HashMap<Marker, SpotResponse>();
        if (googleMap != null) {
            googleMap.clear();
            for (SpotResponse spotResponse : response) {

//                    if(!TextUtils.equals(spotResponse.getName(), SharedPreferenceUtils.getUserEmail(context))){
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(spotResponse.getLatitude(), spotResponse.getLongitude()));
                if (TextUtils.equals(spotResponse.getActivity_type(), ApiConstants.DYNAMIC_SPOT)) {
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_priest));
                } else if (TextUtils.equals(spotResponse.getActivity_type(), ApiConstants.STATIC_SPOT)) {
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_church));
                }

                Marker currentMarker = googleMap.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, spotResponse);
//                        Marker marker = googleMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(spotResponse.getLatitude(), spotResponse.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_priest)));
//                    }
            }
        }
        return mMarkersHashMap;
    }
}

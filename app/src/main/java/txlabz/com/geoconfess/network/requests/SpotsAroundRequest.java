package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.SpotsAroundEvent;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.models.response.SpotResponse;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class SpotsAroundRequest {

    private static final String TAG = SpotsAroundRequest.class.getSimpleName();

    public static void getSpotsAround(String accessToken, Integer now, Double latitude, Double longitude, Integer distance) {
        GeoConfessApplication.getAppApi().getSpotsAround(accessToken, now, latitude, longitude, distance).enqueue(new Callback<SpotResponse[]>() {


            @Override
            public void onResponse(Call<SpotResponse[]> call, Response<SpotResponse[]> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<SpotResponse[]> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });
    }

    public static void successEvent(SpotResponse[] response) {
        BusProvider.getInstance().post(new SpotsAroundEvent(response));
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }
}

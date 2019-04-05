package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.MySpotsEvent;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.models.response.MySpotResponse;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class MySpotsRequest {

    private static final String TAG = MySpotsRequest.class.getSimpleName();

    public static void getMyPosts(String accessToken) {
        GeoConfessApplication.getAppApi().getMySpots(accessToken).enqueue(new Callback<MySpotResponse[]>() {


            @Override
            public void onResponse(Call<MySpotResponse[]> call, Response<MySpotResponse[]> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<MySpotResponse[]> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });
    }

    public static void successEvent(MySpotResponse[] response) {
        BusProvider.getInstance().post(new MySpotsEvent(response));
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }
}

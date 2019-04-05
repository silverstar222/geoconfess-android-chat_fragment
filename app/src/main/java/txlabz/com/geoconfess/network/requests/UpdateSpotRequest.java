package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.UpdateSpotEvent;
import txlabz.com.geoconfess.models.request.UpdateSpotRequestModel;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.models.response.SimpleResponse;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class UpdateSpotRequest {

    private static final String TAG = UpdateSpotRequest.class.getSimpleName();

    public static void update(UpdateSpotRequestModel r) {
        GeoConfessApplication.getAppApi().updateSpot(r.getId(), r.getLatitude(), r.getLongitude(), r.getAccessToken()).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });

    }

    public static void successEvent(SimpleResponse response) {
        BusProvider.getInstance().post(new UpdateSpotEvent(response));
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }
}

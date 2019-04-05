package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.UpdateUserEvent;
import txlabz.com.geoconfess.models.UpdateUserRequestModel;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.models.response.SimpleResponse;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class UpdateUserRequest {

    private static final String TAG = UpdateUserRequest.class.getSimpleName();

    public static void update(UpdateUserRequestModel r) {
        GeoConfessApplication.getAppApi().updateUser(r.getAccessToken(), r.getEmail(), r.getPassword(), r.getName(),
                r.getPhone(), "" + r.isNotification(), "" + r.isNewsletter(),
                r.getSurname()).enqueue(new Callback<SimpleResponse>() {

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
        BusProvider.getInstance().post(new UpdateUserEvent(response));
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }
}

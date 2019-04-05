package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.LoginEvent;
import txlabz.com.geoconfess.models.request.AuthRequestModel;
import txlabz.com.geoconfess.models.response.AuthResponse;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class LoginRequest {

    private static final String TAG = LoginRequest.class.getSimpleName();

    public static void login(AuthRequestModel r) {
        GeoConfessApplication.getAppApi().oathToken(r.getGrantType(), r.getUsername(), r.getPassword(),
                r.getOs(), r.getPush_token()).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });
    }

    public static void successEvent(AuthResponse response) {
        BusProvider.getInstance().post(new LoginEvent(response));
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }
}

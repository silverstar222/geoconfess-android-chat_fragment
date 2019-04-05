package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.events.AccessConfessionEvent;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class AcceptConfessionRequest {

    private static final String TAG = AcceptConfessionRequest.class.getSimpleName();

    public static void access(long id, String accessToken) {
        GeoConfessApplication.getAppApi().acceptConfession(id, accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    successEvent();
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });

    }

    public static void successEvent() {
        BusProvider.getInstance().post(new AccessConfessionEvent());
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }
}

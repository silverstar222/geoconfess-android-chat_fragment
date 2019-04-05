package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.MarkReadEvent;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Dell on 6/30/2016.
 */
public class MarkNotificationReadRequest {

    private static final String TAG = AcceptConfessionRequest.class.getSimpleName();


    public static void markread(long id, String accessToken) {
        GeoConfessApplication.getAppApi().getMarkRead(id, accessToken).enqueue(new Callback<ResponseBody>() {
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
        BusProvider.getInstance().post(new MarkReadEvent());
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }
}

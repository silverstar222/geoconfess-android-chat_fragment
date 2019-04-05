package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.ServerNotificationEvent;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.models.response.ServerNotificationModel;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class NotificationsRequest {

    private static final String TAG = NotificationsRequest.class.getSimpleName();

    public static void getNotifications(String accessToken) {
        GeoConfessApplication.getAppApi().getNotifications(accessToken).enqueue(new Callback<ServerNotificationModel[]>() {

            @Override
            public void onResponse(Call<ServerNotificationModel[]> call, Response<ServerNotificationModel[]> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ServerNotificationModel[]> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });
    }

    public static void successEvent(ServerNotificationModel[] response) {
        BusProvider.getInstance().post(new ServerNotificationEvent(response));
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }
}

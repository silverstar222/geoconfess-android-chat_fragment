package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.constants.ApiConstants;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.DeletedDynamicSpotEvent;
import txlabz.com.geoconfess.events.DeletedStaticSpotEvent;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class DeleteSpotRequest {

    private static final String TAG = DeleteSpotRequest.class.getSimpleName();

    public static void deleteSpot(long id, String accessToken, final String deleteSpotType) {
        GeoConfessApplication.getAppApi().deleteSpot(id, accessToken).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    if (deleteSpotType.equals(ApiConstants.DYNAMIC_SPOT)) {
                        successDynamicEvent();
                    } else {
                        successStaticEvent();
                    }
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });

    }

    public static void successDynamicEvent() {
        BusProvider.getInstance().post(new DeletedDynamicSpotEvent());
    }


    public static void successStaticEvent() {
        BusProvider.getInstance().post(new DeletedStaticSpotEvent());
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }
}

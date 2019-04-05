package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.events.BookingCheckEvent;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.models.response.BookingConfessStateCheck;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class CheckRequestStatus {

    private static final String TAG = CheckRequestStatus.class.getSimpleName();

    public static void checkStatus(String accessToken, long party_id) {
        GeoConfessApplication.getAppApi().getRequestStatus(accessToken, party_id).enqueue(new Callback<BookingConfessStateCheck[]>() {

            @Override
            public void onResponse(Call<BookingConfessStateCheck[]> call, Response<BookingConfessStateCheck[]> response) {
                Log.i(TAG, "onResponse: " + response.code());

                if (response.isSuccessful()) {
                    successEvent(response.body());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<BookingConfessStateCheck[]> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });
    }

    public static void successEvent(BookingConfessStateCheck[] response) {
        BusProvider.getInstance().post(new BookingCheckEvent(response));
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }
}

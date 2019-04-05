package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.events.UpdateRecurrencesEvent;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.models.response.Recurrences;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class UpdateRecurrencesRequest {

    private static final String TAG = UpdateRecurrencesRequest.class.getSimpleName();

    public static void updateNotRecurrences(long id, String date, String startAt, String stopAt, String accessToken) {
        GeoConfessApplication.getAppApi().updateNotRecurrences(id, accessToken, date, startAt, stopAt).enqueue(new Callback<Recurrences>() {

            @Override
            public void onResponse(Call<Recurrences> call, Response<Recurrences> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Recurrences> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });
    }

    public static void successEvent(Recurrences response) {
        BusProvider.getInstance().post(new UpdateRecurrencesEvent(response));
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }

    public static void updateRecurrance(long id, String startAt, String stopAt, String accessToken, String[] days) {
        GeoConfessApplication.getAppApi().updateRecurrences(id, accessToken, startAt, stopAt, days).enqueue(new Callback<Recurrences>() {

            @Override
            public void onResponse(Call<Recurrences> call, Response<Recurrences> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Recurrences> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });
    }

    public static void createRecurrance(long id, String startAt, String stopAt, String accessToken, String[] days) {
        GeoConfessApplication.getAppApi().createRecurrences(id, accessToken, startAt, stopAt, days).enqueue(new Callback<Recurrences>() {

            @Override
            public void onResponse(Call<Recurrences> call, Response<Recurrences> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Recurrences> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });
    }


    public static void createDayRecurrence(long id, String date, String startAt, String stopAt, String accessToken) {
        GeoConfessApplication.getAppApi().createDayRecurrences(id, accessToken, date, startAt, stopAt).enqueue(new Callback<Recurrences>() {

            @Override
            public void onResponse(Call<Recurrences> call, Response<Recurrences> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Recurrences> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });
    }

}

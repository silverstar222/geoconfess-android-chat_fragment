package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.constants.ApiConstants;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.CreatedDynamicSpotEvent;
import txlabz.com.geoconfess.events.CreatedStaticSpotEvent;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.models.request.CreateSpotRequestModel;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.models.response.SpotResponse;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class CreateSpotRequest {

    private static final String TAG = CreateSpotRequest.class.getSimpleName();

    public static void createDynamicSpot(final CreateSpotRequestModel r) {
        GeoConfessApplication.getAppApi().createSpot(r.getName(), r.getActivityType(), r.getLatitude(), r.getLongitude(), r.getAccessToken()).enqueue(new Callback<SpotResponse>() {
            @Override
            public void onResponse(Call<SpotResponse> call, Response<SpotResponse> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body(), r.getActivityType());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<SpotResponse> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });
    }

    public static void successEvent(SpotResponse response, String type) {
        switch (type) {
            case ApiConstants.DYNAMIC_SPOT:
                BusProvider.getInstance().post(new CreatedDynamicSpotEvent(response));
                break;
            case ApiConstants.STATIC_SPOT:
                BusProvider.getInstance().post(new CreatedStaticSpotEvent(response));
                break;
        }
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }

    public static void createStaticSpot(final CreateSpotRequestModel r) {
        GeoConfessApplication.getAppApi().createStaticSpot(r.getName(), r.getActivityType(), r.getLatitude(),
                r.getLongitude(), r.getAccessToken(), r.getStreet(), r.getCity(), r.getCountry()).enqueue(new Callback<SpotResponse>() {
            @Override
            public void onResponse(Call<SpotResponse> call, Response<SpotResponse> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body(), r.getActivityType());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<SpotResponse> call, Throwable t) {

            }
        });
    }
}

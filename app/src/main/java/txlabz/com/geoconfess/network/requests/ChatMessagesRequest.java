package txlabz.com.geoconfess.network.requests;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import txlabz.com.geoconfess.GeoConfessApplication;
import txlabz.com.geoconfess.events.AllChatMessagesEvent;
import txlabz.com.geoconfess.events.BusProvider;
import txlabz.com.geoconfess.events.ErrorResponseEvent;
import txlabz.com.geoconfess.models.response.ChatMessage;
import txlabz.com.geoconfess.models.response.ErrorResponse;
import txlabz.com.geoconfess.utils.ErrorUtils;

/**
 * Created by Ivan on 16.5.2016..
 */
public class ChatMessagesRequest {

    private static final String TAG = ChatMessagesRequest.class.getSimpleName();

    public static void getAll(long chatId, String accessToken) {
        GeoConfessApplication.getAppApi().getAllMessages(chatId, accessToken).enqueue(new Callback<ChatMessage[]>() {

            @Override
            public void onResponse(Call<ChatMessage[]> call, Response<ChatMessage[]> response) {
                if (response.isSuccessful()) {
                    successEvent(response.body());
                } else {
                    errorMsg(ErrorUtils.parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ChatMessage[]> call, Throwable t) {
                errorMsg(new ErrorResponse(null, null));
            }
        });
    }

    public static void successEvent(ChatMessage[] response) {
        BusProvider.getInstance().post(new AllChatMessagesEvent(response));
    }

    public static void errorMsg(ErrorResponse error) {
        Log.i(TAG, "error msg: " + error.getErrors());
        BusProvider.getInstance().post(new ErrorResponseEvent(error));
    }
}

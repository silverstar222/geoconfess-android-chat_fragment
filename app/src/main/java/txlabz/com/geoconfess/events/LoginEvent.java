package txlabz.com.geoconfess.events;

import android.text.TextUtils;

import txlabz.com.geoconfess.models.response.AuthResponse;

/**
 * Created by Ivan on 16.5.2016..
 */
public class LoginEvent {

    private final AuthResponse response;

    public LoginEvent(AuthResponse response) {
        this.response = response;
    }

    public AuthResponse getResponse() {
        return response;
    }

    public boolean isResponseValid() {
        if (response != null && !TextUtils.isEmpty(response.getAccessToken())) {
            return true;
        } else {
            return false;
        }
    }

}

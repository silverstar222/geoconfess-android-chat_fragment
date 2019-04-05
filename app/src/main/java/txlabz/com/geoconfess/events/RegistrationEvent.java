package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.SimpleResponse;

/**
 * Created by Ivan on 16.5.2016..
 */
public class RegistrationEvent {

    private final String response;

    public RegistrationEvent(SimpleResponse response) {
        this.response = response.getResult();
    }

    public String getResponse() {
        return response;
    }
}

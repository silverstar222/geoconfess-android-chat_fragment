package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.SimpleResponse;

/**
 * Created by Ivan on 16.5.2016..
 */
public class UpdateSpotEvent {

    private final String response;

    public UpdateSpotEvent(SimpleResponse response) {
        this.response = response.getResult();
    }

    public String getResponse() {
        return response;
    }
}

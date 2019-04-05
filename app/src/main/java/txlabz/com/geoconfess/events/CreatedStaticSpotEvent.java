package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.SpotResponse;

/**
 * Created by Ivan on 16.5.2016..
 */
public class CreatedStaticSpotEvent {

    private final SpotResponse response;

    public CreatedStaticSpotEvent(SpotResponse response) {
        this.response = response;
    }

    public SpotResponse getResponse() {
        return response;
    }
}

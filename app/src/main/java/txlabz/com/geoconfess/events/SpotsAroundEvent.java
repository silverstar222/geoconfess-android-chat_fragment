package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.SpotResponse;

/**
 * Created by Ivan on 31.5.2016..
 */
public class SpotsAroundEvent {
    private final SpotResponse[] response;

    public SpotsAroundEvent(SpotResponse[] response) {
        this.response = response;
    }

    public SpotResponse[] getResponse() {
        return response;
    }
}

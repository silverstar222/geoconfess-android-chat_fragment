package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.MySpotResponse;

/**
 * Created by Ivan on 30.5.2016..
 */
public class MySpotsEvent {

    private final MySpotResponse[] response;

    public MySpotsEvent(MySpotResponse[] response) {
        this.response = response;
    }

    public MySpotResponse[] getResponse() {
        return response;
    }
}

package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.Recurrences;

/**
 * Created by Ivan on 31.5.2016..
 */
public class UpdateRecurrencesEvent {
    private final Recurrences response;

    public UpdateRecurrencesEvent(Recurrences response) {
        this.response = response;
    }

    public Recurrences getResponse() {
        return response;
    }
}

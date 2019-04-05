package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.BookingResponse;

/**
 * Created by Ivan on 1.6.2016..
 */
public class BookingEvent {
    private final BookingResponse response;

    public BookingEvent(BookingResponse response) {
        this.response = response;
    }

    public BookingResponse getResponse() {
        return response;
    }
}

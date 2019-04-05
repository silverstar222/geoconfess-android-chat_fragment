package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.BookingConfessStateCheck;

/**
 * Created by Ivan on 1.6.2016..
 */
public class BookingCheckEvent {


    private final BookingConfessStateCheck[] response;

    public BookingCheckEvent(BookingConfessStateCheck[] response) {
        this.response = response;
    }

    public BookingConfessStateCheck[] getResponse() {
        return response;
    }
}

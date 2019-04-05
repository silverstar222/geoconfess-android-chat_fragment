package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.ErrorResponse;

/**
 * Created by Miroslav on 31.10.2015..
 */
public class ErrorResponseEvent {
    private ErrorResponse error;

    public ErrorResponseEvent(ErrorResponse error) {
        this.error = error;
    }

    public ErrorResponse getError() {
        return error;
    }
}

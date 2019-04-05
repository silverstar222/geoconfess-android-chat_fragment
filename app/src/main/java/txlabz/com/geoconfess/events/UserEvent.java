package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.User;

/**
 * Created by Ivan on 16.5.2016..
 */
public class UserEvent {

    private final User user;

    public UserEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

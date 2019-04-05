package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.ServerNotificationModel;

/**
 * Created by dusan on 31.05.16..
 */
public class ServerNotificationEvent {
    private final ServerNotificationModel[] response;

    public ServerNotificationEvent(ServerNotificationModel[] response) {
        this.response = response;
    }

    public ServerNotificationModel[] getResponse() {
        return response;
    }
}

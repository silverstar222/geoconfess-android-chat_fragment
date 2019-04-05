package txlabz.com.geoconfess.events;

import txlabz.com.geoconfess.models.response.ChatMessage;

/**
 * Created by Ivan on 6.6.2016..
 */
public class AllChatMessagesEvent {
    private final ChatMessage[] response;

    public AllChatMessagesEvent(ChatMessage[] response) {
        this.response = response;
    }

    public ChatMessage[] getResponse() {
        return response;
    }
}

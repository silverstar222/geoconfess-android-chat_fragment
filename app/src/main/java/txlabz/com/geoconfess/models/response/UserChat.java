package txlabz.com.geoconfess.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ivan on 16.5.2016..
 */
public class UserChat extends BaseModel {
//    [
//    {
//        "id": 19,
//            "user": {
//        "id": 19,
//                "name": "Oleg",
//                "surname": "Test"
//    },
//        "last_message": {
//        "id": 28,
//                "sender_id": 24,
//                "recipient_id": 19,
//                "text": "Hello user 19"
//    }
//    },
//            ]

    @SerializedName("user")
    private User user;
    @SerializedName("last_message")
    private ChatMessage lastMessage;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }
}

package txlabz.com.geoconfess.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ivan on 16.5.2016..
 */
public class ChatMessage extends BaseModel {
    //    {
//        "id": 28,
//                "sender_id": 24,
//                "recipient_id": 19,
//                "text": "Hello user 19"gdfgdf
//    }
    @SerializedName("sender_id")
    private long senderId;
    @SerializedName("recipient_id")
    private long recipientId;
    @SerializedName("text")
    private String text;

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

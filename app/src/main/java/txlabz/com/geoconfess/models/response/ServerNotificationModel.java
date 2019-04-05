package txlabz.com.geoconfess.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dusan on 31.05.16..
 */
public class ServerNotificationModel {
    @SerializedName("id")
    long id;
    @SerializedName("unread")
    boolean unread;
    @SerializedName("model")
    String model;
    @SerializedName("action")
    String action;
    @SerializedName("meet_request")
    MeetRequestModel meetRequestModel;

    @SerializedName("message")
    ChatMessage message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public MeetRequestModel getMeetRequestModel() {
        return meetRequestModel;
    }

    public void setMeetRequestModel(MeetRequestModel meetRequestModel) {
        this.meetRequestModel = meetRequestModel;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }
}
//
//          "id": 3,
//        "unread": false,
//        "model": "MeetRequest",
//        "action": "received",
//        "meet_request": {
//        "id": 10,
//        "priest_id": 24,
//        "status": "pending",
//        "penitent": {
//        "id": 25,
//        "name": "Test user",
//        "surname": "Surname",
//        "latitude": "12.234",
//        "longitude": "23.345"

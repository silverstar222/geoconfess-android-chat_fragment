package txlabz.com.geoconfess.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ivan on 1.6.2016..
 */
public class BookingResponse {

    @SerializedName("id")
    long id;
    @SerializedName("priest_id")
    long priestId;
    @SerializedName("status")
    String status;
    @SerializedName("penitent")
    SmallPenitent penitent;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPriestId() {
        return priestId;
    }

    public void setPriestId(long priestId) {
        this.priestId = priestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SmallPenitent getPenitent() {
        return penitent;
    }

    public void setPenitent(SmallPenitent penitent) {
        this.penitent = penitent;
    }

    class SmallPenitent {
        @SerializedName("penitent_id")
        long penitentId;
    }
}

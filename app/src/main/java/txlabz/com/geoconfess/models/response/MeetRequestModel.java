package txlabz.com.geoconfess.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dusan on 31.05.16..
 */
public class MeetRequestModel {
    @SerializedName("id")
    long id;
    @SerializedName("priest_id")
    long priestId;
    @SerializedName("status")
    String status;
    @SerializedName("penitent")
    PenitentModel penitentModel;

    @SerializedName("priest")
    Priest priestModel;


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

    public Priest getPriestModel() {
        return priestModel;
    }

    public void setPriestModel(Priest priestModel) {
        this.priestModel = priestModel;
    }

    public PenitentModel getPenitentModel() {
        return penitentModel;
    }

    public void setPenitentModel(PenitentModel penitentModel) {
        this.penitentModel = penitentModel;
    }
}

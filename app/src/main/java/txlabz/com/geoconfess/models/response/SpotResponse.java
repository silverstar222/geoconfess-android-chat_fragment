package txlabz.com.geoconfess.models.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yagor on 5/5/16.
 */
public class SpotResponse implements Parcelable {

    public static final Parcelable.Creator<SpotResponse> CREATOR = new Parcelable.Creator<SpotResponse>() {
        @Override
        public SpotResponse createFromParcel(Parcel source) {
            return new SpotResponse(source);
        }

        @Override
        public SpotResponse[] newArray(int size) {
            return new SpotResponse[size];
        }
    };
    @SerializedName("recurrences")
    Recurrences[] recurrences;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("activity_type")
    private String activity_type;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("priest")
    private Priest priest;

    public SpotResponse() {
    }

    protected SpotResponse(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.activity_type = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.priest = in.readParcelable(Priest.class.getClassLoader());
        this.recurrences = in.createTypedArray(Recurrences.CREATOR);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Priest getPriest() {
        return priest;
    }

    public void setPriest(Priest priest) {
        this.priest = priest;
    }

    public Recurrences[] getRecurrences() {
        return recurrences;
    }

    public void setRecurrences(Recurrences[] recurrences) {
        this.recurrences = recurrences;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.activity_type);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeParcelable(this.priest, flags);
        dest.writeTypedArray(this.recurrences, flags);
    }
}

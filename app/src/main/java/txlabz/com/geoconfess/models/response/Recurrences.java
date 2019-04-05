package txlabz.com.geoconfess.models.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ivan on 30.5.2016..
 */
public class Recurrences implements Parcelable {
    public static final Parcelable.Creator<Recurrences> CREATOR = new Parcelable.Creator<Recurrences>() {
        @Override
        public Recurrences createFromParcel(Parcel source) {
            return new Recurrences(source);
        }

        @Override
        public Recurrences[] newArray(int size) {
            return new Recurrences[size];
        }
    };
    @SerializedName("id")
    long id;
    @SerializedName("spot_id")
    long spot_id;
    @SerializedName("stop_at")
    String stopTime;
    @SerializedName("start_at")
    String startTime;
    @SerializedName("date")
    String date;
    @SerializedName("week_days")
    String[] days;

    public Recurrences() {
    }

    protected Recurrences(Parcel in) {
        this.id = in.readLong();
        this.spot_id = in.readLong();
        this.stopTime = in.readString();
        this.startTime = in.readString();
        this.date = in.readString();
        this.days = in.createStringArray();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSpot_id() {
        return spot_id;
    }

    public void setSpot_id(long spot_id) {
        this.spot_id = spot_id;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDate() {
        return date;
    }


    /*
    {
        "id": 4,
        "spot_id": 1,
        "start_at": "10:00",
        "stop_at": "16:00",
        "date": "2016-04-11"
      },
      {
        "id": 6,
        "spot_id": 1,
        "start_at": "10:00",
        "stop_at": "20:00",
        "week_days": [
          "Tuesday",
          "Wednesday",
          "Thursday",
          "Friday"
     */

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.spot_id);
        dest.writeString(this.stopTime);
        dest.writeString(this.startTime);
        dest.writeString(this.date);
        dest.writeStringArray(this.days);
    }
}

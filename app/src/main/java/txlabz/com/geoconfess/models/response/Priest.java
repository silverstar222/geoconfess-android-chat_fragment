package txlabz.com.geoconfess.models.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yagor on 5/5/16.
 */
public class Priest implements Parcelable {

    public static final Parcelable.Creator<Priest> CREATOR = new Parcelable.Creator<Priest>() {
        @Override
        public Priest createFromParcel(Parcel source) {
            return new Priest(source);
        }

        @Override
        public Priest[] newArray(int size) {
            return new Priest[size];
        }
    };
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("surname")
    private String surname;

    public Priest() {
    }

    protected Priest(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.surname = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.surname);
    }
}

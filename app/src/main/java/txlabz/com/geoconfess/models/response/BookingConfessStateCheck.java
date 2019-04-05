package txlabz.com.geoconfess.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ivan on 1.6.2016..
 */
public class BookingConfessStateCheck {
    @SerializedName("id")
    long bookingRequest;
    @SerializedName("priest_id")
    long priestId;
    @SerializedName("status")
    String status;
    @SerializedName("penitent")
    PenitentModel penitent;
    @SerializedName("priest")
    Priest priest;

    public long getBookingRequest() {
        return bookingRequest;
    }

    public void setBookingRequest(long bookingRequest) {
        this.bookingRequest = bookingRequest;
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

    public PenitentModel getPenitent() {
        return penitent;
    }

    public void setPenitent(PenitentModel penitent) {
        this.penitent = penitent;
    }

    public Priest getPriest() {
        return priest;
    }

    public void setPriest(Priest priest) {
        this.priest = priest;
    }

    /*
    {
    "id": 4,
    "priest_id": 24,
    "status": "pending",
    "penitent": {
      "id": 25,
      "name": "Test user",
      "surname": "Surname",
      "latitude": "24.123234",
      "longitude": "21.234234"
    }
  },
  {
    "id": 9,
    "status": "pending",
    "penitent": {
      "id": 24
    },
    "priest": {
      "id": 2,
      "name": null,
      "surname": null
    }
  }
     */
}

package txlabz.com.geoconfess.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ivan on 30.5.2016..
 */
public class MySpotResponse extends SpotResponse {

    @SerializedName("street")
    String street;
    @SerializedName("postcode")
    String postCode;
    @SerializedName("city")
    String city;
    @SerializedName("state")
    String state;
    @SerializedName("country")
    String country;


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    /*
    {
    "id": 389,
    "name": "radanovic.miroslav@gmail.com",
    "activity_type": "static",
    "latitude": 45.7319501,
    "longitude": 15.9433745,
    "street": null,
    "postcode": null,
    "city": null,
    "state": null,
    "country": null,
    "recurrences": []
  }
     */
}

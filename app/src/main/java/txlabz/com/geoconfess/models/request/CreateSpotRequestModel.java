package txlabz.com.geoconfess.models.request;

/**
 * Created by Ivan on 16.5.2016..
 */
public class CreateSpotRequestModel {

    private final String name;
    private final String activityType;
    private final String latitude;
    private final String longitude;
    private final String accessToken;
    private String street;
    private String country;
    private String city;

    public CreateSpotRequestModel(String name, String activity_type, String latitude, String longitude, String access_token) {
        this.name = name;
        this.activityType = activity_type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accessToken = access_token;
    }

    public CreateSpotRequestModel(String name, String activity_type, String latitude, String longitude,
                                  String access_token, String street, String city, String country) {
        this.name = name;
        this.activityType = activity_type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accessToken = access_token;
        this.street = street;
        this.country = country;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAccessToken() {
        return accessToken;
    }
}

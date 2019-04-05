package txlabz.com.geoconfess.models.request;

/**
 * Created by Ivan on 16.5.2016..
 */
public class UpdateSpotRequestModel {

    private final long id;
    private final String latitude;
    private final String longitude;
    private final String accessToken;

    public UpdateSpotRequestModel(long id, String latitude, String longitude, String access_token) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accessToken = access_token;
    }


    public long getId() {
        return id;
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

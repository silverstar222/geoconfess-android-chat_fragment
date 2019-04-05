package txlabz.com.geoconfess.models.request;

/**
 * Created by Ivan on 16.5.2016..
 */
public class AuthRequestModel {

    String grantType;
    String username;
    String password;
    String os;
    String push_token;

    public AuthRequestModel(String grantType, String username, String password, String os, String push_token) {
        this.grantType = grantType;
        this.username = username;
        this.password = password;
        this.os = os;
        this.push_token = push_token;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getOs() {
        return os;
    }

    public String getPush_token() {
        return push_token;
    }
}

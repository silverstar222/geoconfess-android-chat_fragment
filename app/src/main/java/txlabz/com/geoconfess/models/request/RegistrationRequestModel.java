package txlabz.com.geoconfess.models.request;

/**
 * Created by Ivan on 16.5.2016..
 */
public class RegistrationRequestModel extends BaseUserRequestModel {

    private final String role;
    private final String celebretUrl;

    public RegistrationRequestModel(String role, String email, String password, String name, String surname,
                                    String phone, String celebretUrl, boolean notification, boolean newsletter) {
        super(email, password, name, surname, phone, notification, newsletter);

        this.role = role;
        this.celebretUrl = celebretUrl;
    }

    public String getRole() {
        return role;
    }

    public String getCelebretUrl() {
        return celebretUrl;
    }
}

package txlabz.com.geoconfess.models.request;

/**
 * Created by Ivan on 16.5.2016..
 */
public class BaseUserRequestModel {

    private final String email;
    private final String password;
    private final String name;
    private final String surname;
    private final String phone;
    private final boolean notification;
    private final boolean newsletter;

    public BaseUserRequestModel(String email, String password, String name, String surname,
                                String phone, boolean notification, boolean newsletter) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.notification = notification;
        this.newsletter = newsletter;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isNotification() {
        return notification;
    }

    public boolean isNewsletter() {
        return newsletter;
    }
}

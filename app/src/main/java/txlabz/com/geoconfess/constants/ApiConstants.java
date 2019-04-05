package txlabz.com.geoconfess.constants;

/**
 * Created by Ivan on 16.5.2016..
 */
public class ApiConstants {

    //all apis
    public static final String OAUTH_TOKEN_API = "oauth/token";
    public static final String SIGNUP_API = "api/v1/registrations";
    public static final String FORGOT_PASSWORDS_API = "api/v1/passwords";
    public static final String CREATE_SPOT_API = "api/v1/spots";
    public static final String UPDATE_SPOT_API = "api/v1/spots/{id}";
    public static final String DELETE_SPOT_API = "api/v1/spots/{id}";
    public static final String CHATS_API = "api/v1/messages";
    public static final String ALL_CHAT_MSG_API = "api/v1/messages/{id}";
    public static final String API_V1_SPOTS_ID_RECURRENCES_CREATE = "api/v1/spots/{id}/recurrences";
    public static final String API_V1_SPOTS_ID_UPDATE_RECURRENCES = "api/v1/recurrences/{id}";


    //request params for apis
    public static final String GRANT_TYPE = "grant_type";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String OS = "os";
    public static final String PUSH_TOKEN = "push_token";

    public static final String USER_ROLE = "user[role]";
    public static final String USER_PASSWORD = "user[password]";
    public static final String USER_NAME = "user[name]";
    public static final String USER_PHONE = "user[phone]";
    public static final String USER_CELEBRET_URL = "user[celebret_url]";
    public static final String USER_NOTIFICATION = "user[notification]";
    public static final String USER_NEWSLETTER = "user[newsletter]";
    public static final String USER_SURNAME = "user[surname]";
    public static final String USER_EMAIL = "user[email]";
    public static final String SPOT_NAME = "spot[name]";
    public static final String SPOT_ACTIVITY_TYPE = "spot[activity_type]";
    public static final String SPOT_LONGITUDE = "spot[longitude]";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ENTITY_ID = "id";
    public static final String NOTIFICATION_ID = "id";

    public static final String SPOT_LATITUDE = "spot[latitude]";

    public static final String DYNAMIC_SPOT = "dynamic";
    public static final String API_V1_NOTIFICATIONS = "api/v1/notifications";
    public static final String API_V1_NOTIFICATIONS_MARKREAD = "api/v1/notifications/{id}/mark_read";

    public static final String STATIC_SPOT = "static";
    public static final String SPOT_STREET = "spot[street]";
    public static final String SPOT_CITY = "spot[city]";
    public static final String SPOT_COUNTRY = "spot[country]";

    public static final String SPOT_DATE = "recurrence[date]";
    public static final String SPOT_START_AT = "recurrence[start_at]";
    public static final String SPOT_STOP_AT = "recurrence[stop_at]";
    public static final String SPOT_WORKING_DAYS = "recurrence[week_days][]";
    public static final String ACTIVE_NOW = "now";

    public static final String SPOT_LAT = "lat";
    public static final String SPOT_LNG = "lng";
    public static final String SPOT_DISTANCE = "distance";
    public static final String PARTY_ID = "party_id";
    public static final String API_V1_REQUESTS = "api/v1/requests";
    public static final String REQUEST_PRIEST_ID = "request[priest_id]";
    public static final String REQUEST_LAT = "request[latitude]";
    public static final String REQUEST_LON = "request[longitude]";
    public static final String REQUEST_STATUS = "request[status]";

    public static final String REQUEST_STATUS_PENDING = "pending";
    public static final String REQUEST_STATUS_ACCEPTED = "accepted";
    public static final String REQUEST_STATUS_REFUSED = "refused";
    public static final String API_V1_REQUESTS_ID_ACCEPT = "api/v1/requests/{id}/accept";
    public static final String API_V1_REQUESTS_ID_REFUSE = "api/v1/requests/{id}/refuse";
    public static final String MESSAGE_RECIPIENT_ID = "message[recipient_id]";
    public static final String MESSAGE_TEXT = "message[text]";
    public static final String API_V1_CHATS_ID = "api/v1/chats/{id}";
}

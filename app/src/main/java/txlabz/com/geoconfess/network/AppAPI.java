package txlabz.com.geoconfess.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import txlabz.com.geoconfess.constants.ApiConstants;
import txlabz.com.geoconfess.models.response.AuthResponse;
import txlabz.com.geoconfess.models.response.BookingConfessStateCheck;
import txlabz.com.geoconfess.models.response.BookingResponse;
import txlabz.com.geoconfess.models.response.ChatMessage;
import txlabz.com.geoconfess.models.response.MySpotResponse;
import txlabz.com.geoconfess.models.response.Recurrences;
import txlabz.com.geoconfess.models.response.ServerNotificationModel;
import txlabz.com.geoconfess.models.response.SimpleResponse;
import txlabz.com.geoconfess.models.response.SpotResponse;
import txlabz.com.geoconfess.models.response.User;
import txlabz.com.geoconfess.models.response.UserChat;

/**
 * Created by yagor on 20/04/2016.
 */
public interface AppAPI {

    @POST(ApiConstants.OAUTH_TOKEN_API)
    Call<AuthResponse> oathToken(@Query(ApiConstants.GRANT_TYPE) String grant_type,
                                 @Query(ApiConstants.USERNAME) String username,
                                 @Query(ApiConstants.PASSWORD) String password,
                                 @Query(ApiConstants.OS) String os,
                                 @Query(ApiConstants.PUSH_TOKEN) String push_token
    );


    @POST(ApiConstants.SIGNUP_API)
    Call<SimpleResponse> signUp(
            @Query(ApiConstants.USER_ROLE) String role,
            @Query(ApiConstants.USER_EMAIL) String email,
            @Query(ApiConstants.USER_PASSWORD) String password,
            @Query(ApiConstants.USER_NAME) String name,
            @Query(ApiConstants.USER_PHONE) String phon,
            @Query(ApiConstants.USER_CELEBRET_URL) String celebret_url,
            @Query(ApiConstants.USER_NOTIFICATION) String notification,
            @Query(ApiConstants.USER_NEWSLETTER) String newsletter,
            @Query(ApiConstants.USER_SURNAME) String surname
    );

    @POST(ApiConstants.FORGOT_PASSWORDS_API)
    Call<SimpleResponse> forgot(
            @Query(ApiConstants.USER_EMAIL) String email
    );


    /*
   *  Defining Retrofit interface for calling spot creation Api with the requested parameters
   * */
    @POST(ApiConstants.CREATE_SPOT_API)
    Call<SpotResponse> createSpot(
            @Query(ApiConstants.SPOT_NAME) String name,
            @Query(ApiConstants.SPOT_ACTIVITY_TYPE) String activity_type,
            @Query(ApiConstants.SPOT_LATITUDE) String latitude,
            @Query(ApiConstants.SPOT_LONGITUDE) String longitude,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token
    );


    /*
   *  Defining Retrofit interface for calling spot updation Api with the requested parameters
   * */
    @PATCH(ApiConstants.UPDATE_SPOT_API)
    Call<SimpleResponse> updateSpot(
            @Path(ApiConstants.ENTITY_ID) long spotId,
            @Query(ApiConstants.SPOT_LATITUDE) String latitude,
            @Query(ApiConstants.SPOT_LONGITUDE) String longitude,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token
    );


    /*
  *  Defining Retrofit interface for Deleting spot
  * */
    @DELETE(ApiConstants.DELETE_SPOT_API)
    Call<ResponseBody> deleteSpot(
            @Path(ApiConstants.ENTITY_ID) long spotId,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token

    );

    //CHAT APIS

    /**
     * returning all chats from current user
     */
    //TODO we need params
    @GET(ApiConstants.CHATS_API)
    Call<UserChat[]> getAllChats();

//    /**
//     * returning all messages of certain chat
//     */
//    //TODO need to test API
//    @GET(ApiConstants.ALL_CHAT_MSG_API)
//    Call<ChatMessage[]> getChatMessages(
//            @Path(ApiConstants.ENTITY_ID) long id
//    );

    @POST(ApiConstants.CHATS_API)
    Call<ResponseBody> sendMessage(
            @Query(ApiConstants.MESSAGE_RECIPIENT_ID) long recipientId,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token,
            @Query(ApiConstants.MESSAGE_TEXT) String message
    );

    @GET(ApiConstants.API_V1_CHATS_ID)
    Call<ChatMessage[]> getAllMessages(
            @Path(ApiConstants.ENTITY_ID) long spotId,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token
    );

    //CREDENTIALS APIS

    /**
     * Method for returning user data
     */
    @GET("api/v1/me")
    Call<User> getUserData(
            @Query(ApiConstants.ACCESS_TOKEN) String accessToken
    );

    @PUT("api/v1/me")
    Call<SimpleResponse> updateUser(
            @Query(ApiConstants.ACCESS_TOKEN) String accessToken,
            @Query(ApiConstants.USER_EMAIL) String email,
            @Query(ApiConstants.USER_PASSWORD) String password,
            @Query(ApiConstants.USER_NAME) String name,
            @Query(ApiConstants.USER_PHONE) String phon,
            @Query(ApiConstants.USER_NOTIFICATION) String notification,
            @Query(ApiConstants.USER_NEWSLETTER) String newsletter,
            @Query(ApiConstants.USER_SURNAME) String surname

    );

    @GET(ApiConstants.API_V1_NOTIFICATIONS)
    Call<ServerNotificationModel[]> getNotifications(
            @Query(ApiConstants.ACCESS_TOKEN) String accessToken
    );

    @PUT(ApiConstants.API_V1_NOTIFICATIONS_MARKREAD)
    Call<ResponseBody> getMarkRead(
            @Path(ApiConstants.NOTIFICATION_ID) long id,
            @Query(ApiConstants.ACCESS_TOKEN) String accessToken
    );

    @GET("/api/v1/me/spots")
    Call<MySpotResponse[]> getMySpots(
            @Query(ApiConstants.ACCESS_TOKEN) String accessToken
    );

    @POST(ApiConstants.CREATE_SPOT_API)
    Call<SpotResponse> createStaticSpot(
            @Query(ApiConstants.SPOT_NAME) String name,
            @Query(ApiConstants.SPOT_ACTIVITY_TYPE) String activity_type,
            @Query(ApiConstants.SPOT_LATITUDE) String latitude,
            @Query(ApiConstants.SPOT_LONGITUDE) String longitude,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token,
            @Query(ApiConstants.SPOT_STREET) String street,
            @Query(ApiConstants.SPOT_CITY) String city,
            @Query(ApiConstants.SPOT_COUNTRY) String country
    );

    @PATCH(ApiConstants.API_V1_SPOTS_ID_UPDATE_RECURRENCES)
    Call<Recurrences> updateNotRecurrences(
            @Path(ApiConstants.ENTITY_ID) long spotId,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token,
            @Query(ApiConstants.SPOT_DATE) String date,
            @Query(ApiConstants.SPOT_START_AT) String startAt,
            @Query(ApiConstants.SPOT_STOP_AT) String stopAt
    );

    @POST(ApiConstants.API_V1_SPOTS_ID_RECURRENCES_CREATE)
    Call<Recurrences> createDayRecurrences(
            @Path(ApiConstants.ENTITY_ID) long spotId,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token,
            @Query(ApiConstants.SPOT_DATE) String date,
            @Query(ApiConstants.SPOT_START_AT) String startAt,
            @Query(ApiConstants.SPOT_STOP_AT) String stopAt
    );

    @POST(ApiConstants.API_V1_SPOTS_ID_RECURRENCES_CREATE)
    Call<Recurrences> createRecurrences(
            @Path(ApiConstants.ENTITY_ID) long spotId,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token,
            @Query(ApiConstants.SPOT_START_AT) String startAt,
            @Query(ApiConstants.SPOT_STOP_AT) String stopAt,
            @Query(ApiConstants.SPOT_WORKING_DAYS) String[] days
    );

    @PATCH(ApiConstants.API_V1_SPOTS_ID_UPDATE_RECURRENCES)
    Call<Recurrences> updateRecurrences(
            @Path(ApiConstants.ENTITY_ID) long spotId,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token,
            @Query(ApiConstants.SPOT_START_AT) String startAt,
            @Query(ApiConstants.SPOT_STOP_AT) String stopAt,
            @Query(ApiConstants.SPOT_WORKING_DAYS) String[] days
    );

    @GET("api/v1/spots")
    Call<SpotResponse[]> getSpotsAround(
            @Query(ApiConstants.ACCESS_TOKEN) String access_token,
            @Query(ApiConstants.ACTIVE_NOW) Integer now,
            @Query(ApiConstants.SPOT_LAT) Double latitude,
            @Query(ApiConstants.SPOT_LNG) Double longitude,
            @Query(ApiConstants.SPOT_DISTANCE) Integer distance
    );

    @GET(ApiConstants.API_V1_REQUESTS)
    Call<BookingConfessStateCheck[]> getRequestStatus(
            @Query(ApiConstants.ACCESS_TOKEN) String accessToken,
            @Query(ApiConstants.PARTY_ID) long partyId
    );

    @POST(ApiConstants.API_V1_REQUESTS)
    Call<BookingResponse> sendBookingRequest(
            @Query(ApiConstants.ACCESS_TOKEN) String access_token,
            @Query(ApiConstants.REQUEST_PRIEST_ID) long priestId,
            @Query(ApiConstants.REQUEST_LAT) double latitude,
            @Query(ApiConstants.REQUEST_LON) double longitude,
            @Query(ApiConstants.REQUEST_STATUS) String status
    );

    @PUT(ApiConstants.API_V1_REQUESTS_ID_ACCEPT)
    Call<ResponseBody> acceptConfession(
            @Path(ApiConstants.ENTITY_ID) long id,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token
    );

    @PUT(ApiConstants.API_V1_REQUESTS_ID_REFUSE)
    Call<ResponseBody> refuseConfession(
            @Path(ApiConstants.ENTITY_ID) long id,
            @Query(ApiConstants.ACCESS_TOKEN) String access_token
    );

    @POST("api/v1/passwords")
    Call<ResponseBody> resetPassword(
            @Query(ApiConstants.USER_EMAIL) String email
    );
}

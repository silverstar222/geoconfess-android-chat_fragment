package txlabz.com.geoconfess.utils;

import android.content.Context;
import android.content.SharedPreferences;

import txlabz.com.geoconfess.BuildConfig;

/**
 * Created by Ivan on 17.5.2016..
 */
public class SharedPreferenceUtils {

    private static final String ACCESS_TOKEN = "AccessToken";
    private static final String PUSH_TOKEN = "PushToken";
    private static final String SERVICE_RUNNING = "ServiceRunning";
    private static final String USER_EMAIL = "UserEmail";
    private static final String USER_ROLE = "UserRole";
    private static final String USER_NAME_SURNAME = "UserNameSurname";
    private static final String NOTE = "Note";
    private static final String FIRST_USE = "firstUse";
    private static final String USER_ID = "USER_ID";
    private static final String BADGE_COUNT = "badge_count";

    private static SharedPreferences sPrefs;

    private static SharedPreferences getPreferences(Context context) {
        if (sPrefs == null) {
            sPrefs = context.getSharedPreferences(
                    BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        }
        return sPrefs;
    }

    public static String getAccessToken(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getString(ACCESS_TOKEN, "");
    }

    public static void setAccessToken(Context context, String token) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putString(ACCESS_TOKEN, token).apply();
    }

    public static long getUserId(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getLong(USER_ID, -1);
    }

    public static void setUserId(Context context, long id) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putLong(USER_ID, id).apply();
    }

    public static String getPushToken(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getString(PUSH_TOKEN, "");
    }

    public static void setPushToken(Context context, String token) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putString(PUSH_TOKEN, token).apply();
    }

    public static void setTrackServiceRunning(Context context, boolean running) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putBoolean(SERVICE_RUNNING, running).apply();
    }

    public static boolean isTrackServiceRunning(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getBoolean(SERVICE_RUNNING, false);
    }

    public static void setUserEmail(Context context, String email) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putString(USER_EMAIL, email).apply();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getString(USER_EMAIL, "");
    }

    public static void setLogInRole(Context context, String role) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putString(USER_ROLE, role).apply();
    }

    public static String getLogInRole(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getString(USER_ROLE, "");
    }

    public static void setUserNameSurname(Context context, String name) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putString(USER_NAME_SURNAME, name).apply();
    }

    public static String getUserNameSurname(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getString(USER_NAME_SURNAME, "");
    }

    public static void setNote(Context context, String name) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putString(NOTE, name).apply();
    }

    public static String getNote(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getString(NOTE, "");
    }

    public static boolean isFirstUse(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getBoolean(FIRST_USE, true);
    }

    public static void setFirstUse(Context context, boolean use) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putBoolean(FIRST_USE, use).apply();
    }

    public static void setBadgeCount(Context context, int count) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putInt(BADGE_COUNT, count).apply();
    }

    public static int getBadgeCount(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getInt(BADGE_COUNT, 0);
    }
//
//    public static void setUserId(Context context, long id){
//        SharedPreferences preferences = getPreferences(context);
//        preferences.edit().putLong(USER_ID, id).apply();
//    }
//
//    public static long getUserId(Context context) {
//        SharedPreferences preferences = getPreferences(context);
//        return preferences.getLong(USER_ID, AppConstants.NOT_VALID_ID);
//    }
}

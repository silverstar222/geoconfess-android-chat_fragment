package txlabz.com.geoconfess.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import txlabz.com.geoconfess.GeoConfessApplication;

/**
 * Created by marko on 7/27/15.
 */
public class NetworkUtils {
    public static boolean connected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) GeoConfessApplication.getAppInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}

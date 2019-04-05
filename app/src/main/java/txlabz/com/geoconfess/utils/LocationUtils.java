package txlabz.com.geoconfess.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import txlabz.com.geoconfess.R;
import txlabz.com.geoconfess.activities.LoginActivity;

/**
 * Created by Ivan on 30.5.2016..
 */
public class LocationUtils {

    public static final float ACCEPTABLE_DISTANCE_METERS = 5000;

    public static boolean isGpsOn(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.yes_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    /*Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);*/
                    Intent myIntent = new Intent(context, LoginActivity.class);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(context.getString(R.string.no_button), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }

        return gps_enabled && network_enabled;
    }


    public static String getReadableDistance(Context context, Double latFrom, Double longFrom, Double latTo, Double longTo) {
        if (latFrom == null || longFrom == null || latTo == null || longTo == null) {
            return "";
        }
        float[] dist = new float[1];
        Location.distanceBetween(latFrom, longFrom, latTo, longTo, dist);
        float meters = dist[0];
        if (meters > ACCEPTABLE_DISTANCE_METERS) {
            float km = meters / 1000;
            return context.getString(R.string.two_resources_format, Math.round(km), context.getString(R.string.kilometers));
        }
        return context.getString(R.string.two_resources_format, Math.round(meters), context.getString(R.string.meters));
    }
}

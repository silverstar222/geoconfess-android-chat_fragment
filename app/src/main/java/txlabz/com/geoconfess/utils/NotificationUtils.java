package txlabz.com.geoconfess.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import txlabz.com.geoconfess.R;


/**
 * Created by Ivan on 7/6/2015.
 */
public class NotificationUtils {

    @SuppressLint("NewApi")
    public static Notification getAppNotification(String text, Intent intent, Context context) {
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, intent, 0);
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            return new Notification.Builder(context)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(text)
                    .setColor(context.getResources().getColor(R.color.red_edit_text_background))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(contentIntent).getNotification();
        } else {
            return new Notification.Builder(context)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(contentIntent).getNotification();
        }
    }

    @SuppressLint("NewApi")
    public static Notification getStickyAppNotification(String text, Intent intent, Context context) {
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, intent, 0);

        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            return new Notification.Builder(context)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(text)
                    .setColor(context.getResources().getColor(R.color.red_edit_text_background))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setContentIntent(contentIntent).getNotification();
        } else {
            return new Notification.Builder(context)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(text)
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(contentIntent).getNotification();
        }
    }
}

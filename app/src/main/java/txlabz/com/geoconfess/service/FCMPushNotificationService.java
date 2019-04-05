package txlabz.com.geoconfess.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import txlabz.com.geoconfess.utils.SharedPreferenceUtils;

/**
 * Created by vavaka on 6/6/16.
 */
public class FCMPushNotificationService extends FirebaseMessagingService {
    private static final String TAG = "FCMPushService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        int count = SharedPreferenceUtils.getBadgeCount(this);
        SharedPreferenceUtils.setBadgeCount(this, count++);

//        ShortcutBadger.applyCount(this, SharedPreferenceUtils.getBadgeCount(this));

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {

    }

}

package txlabz.com.geoconfess.service;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by vavaka on 6/6/16.
 */

    public class PushNotificationService {
    public static void subscribeOnUserPushes(long id) {
        subscribeOnTopic(userPushesKey(id));
    }

    public static void unsubscribeFromUserPushes(long id) {
        unsubscribeFromTopic(userPushesKey(id));
    }

    public static void subscribeOnChannelPushes(String id) {
        subscribeOnTopic(channelPushesKey(id));
    }

    public static void unsubscribeFromChannelPushes(String id) {
        unsubscribeFromTopic(channelPushesKey(id));
    }

    private static void subscribeOnTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topicKey(topic));
    }

    private static void unsubscribeFromTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topicKey(topic));
    }

    private static String userPushesKey(long id) {
        return "user-" + id;
    }

    private static String channelPushesKey(String id) {
        return "channel-" + id;
    }

    private static String topicKey(String topic) {
        return "/topics/" + topic;
    }
}

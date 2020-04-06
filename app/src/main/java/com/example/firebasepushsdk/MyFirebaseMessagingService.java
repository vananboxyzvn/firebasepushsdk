package com.example.firebasepushsdk;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import static com.example.firebasepushsdk.ApplicationFirebase.getContext;
import static com.example.firebasepushsdk.ApplicationFirebase.getIconPush;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public String title, body;
    public static String fcm_token;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData() != null) {
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            title = notification.getTitle();
            body = notification.getBody();
            sendNotification(getContext(), getIconPush());
        }

    }

    @Override
    public void onNewToken(@NonNull String token) {
        sendRegistrationToServer(token);
        fcm_token = token;
        Log.d("NEW TOKEN", token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    public void sendNotification(@NonNull Context context, int icon) {
        Intent intent = new Intent(this, context.getClass());
        intent.putExtra("check_notifi", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        Random random = new Random();
        int m = random.nextInt(8999) + 1000;
        notificationManager.notify(m, notificationBuilder.build());
    }
}

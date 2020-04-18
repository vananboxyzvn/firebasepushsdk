package com.example.firebasepushsdk;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.example.firebasepushsdk.ApplicationFirebase.contextSdk;
import static com.example.firebasepushsdk.ApplicationFirebase.getContext;
import static com.example.firebasepushsdk.ApplicationFirebase.getIconPush;
import static com.example.firebasepushsdk.ApplicationFirebase.receivedPush;
import static com.example.firebasepushsdk.ApplicationFirebase.setContent;
import static com.example.firebasepushsdk.ApplicationFirebase.setDetail;
import static com.example.firebasepushsdk.ApplicationFirebase.setTokenFCM;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public String title, body = null;

    public MyFirebaseMessagingService() {

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage != null) {
            remoteMessage.getData();
            Map<String, String> data = remoteMessage.getData();
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            if (data.size() != 0) {
                receivedPush = true;
                title = data.get("title");
                body = data.get("body");
                sendNotification(title, Objects.requireNonNull(body), remoteMessage.getSentTime());
                setDetail(data);
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        sendRegistrationToServer(token);
        setTokenFCM(token);
        Log.d("NEW TOKEN", token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    public void sendNotification(@NonNull String title, @NonNull String body, long time) {
        Intent intent;
        if (getContext() == null) {
            intent = new Intent(this, ViewPushActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("body", body);
            bundle.putLong("time", time);
            intent.putExtra("data_push", bundle);
        } else {
            intent = new Intent(this, getContext().getClass());
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(getIconPush())
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

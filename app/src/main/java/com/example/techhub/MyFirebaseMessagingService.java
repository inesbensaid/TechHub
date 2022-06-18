package com.example.techhub;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.techhub.App.CHANNEL_1_ID;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String title,message;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setContentTitle(title)
                        //.setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        Intent intent = new Intent (MyFirebaseMessagingService.this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Intent.putExtra("message", "id");

        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}

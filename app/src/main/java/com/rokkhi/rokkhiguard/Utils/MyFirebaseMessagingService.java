package com.rokkhi.rokkhiguard.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rokkhi.rokkhiguard.Activity.VisitorAcceptedActivity;
import com.rokkhi.rokkhiguard.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "onMessageReceived: llll " + remoteMessage.getData());


        Log.e(TAG, "From: " + remoteMessage.getFrom());

        try {
            if (remoteMessage.getData().get("notificationType").equals("VISITOR_UPDATE_ALERT_FOR_GUARD")){

                handleNowDataForVisitor(remoteMessage);
            }
        }catch (Exception e){

        }


    }
    // [END receive_message]

    @Override
    public void onNewToken(String token) {
        Log.e(TAG, "Refreshed token: " + token);

    }

    // Visitor FullScreen Notification
    private void handleNowDataForVisitor(final RemoteMessage remoteMessage) {


        String id= remoteMessage.getData().get("id");

        String notificationType= remoteMessage.getData().get("notificationType");


        int mNotificationID = (int) System.currentTimeMillis();
        Intent intent = new Intent(getApplicationContext(), VisitorAcceptedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("id",id);
        intent.putExtra("notificationType",notificationType);


        String  notificationString= "Visitor Notification";


        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.logotext)
                .setContentTitle(notificationString)
                .setContentText("visitor notification")
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_SOUND)

                .setLights(Color.WHITE, 3000, 3000)
                .setPriority(NotificationCompat.PRIORITY_MAX);

            startActivity(intent);



        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    "Default channel", NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }

        manager.notify(mNotificationID, mBuilder.build());

        Log.e(TAG, "Short lived task is done.");

    }



}

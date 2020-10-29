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

        Log.e(TAG, "onMessageReceived: llll " + remoteMessage);


        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            handleNowDataForVisitor(remoteMessage);


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


    }
    // [END receive_message]

    @Override
    public void onNewToken(String token) {
        Log.e(TAG, "Refreshed token: " + token);

    }

    // Visitor FullScreen Notification
    private void handleNowDataForVisitor(final RemoteMessage remoteMessage) {

        String visitorName = remoteMessage.getData().get("visitorName");
        String status = remoteMessage.getData().get("status");
        String visitorAddress = remoteMessage.getData().get("visitorAddress");
        String visitorImage= remoteMessage.getData().get("visitorImage");
        String id= remoteMessage.getData().get("id");
        String visitorContact= remoteMessage.getData().get("visitorContact");
        String notificationType= remoteMessage.getData().get("notificationType");
        String flatName= remoteMessage.getData().get("flatName");

        int mNotificationID = (int) System.currentTimeMillis();
        Intent intent = new Intent(getApplicationContext(), VisitorAcceptedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("visitorName",visitorName);
        intent.putExtra("status",status);
        intent.putExtra("visitorAddress",visitorAddress);
        intent.putExtra("visitorImage",visitorImage);
        intent.putExtra("id",id);
        intent.putExtra("visitorContact",visitorContact);
        intent.putExtra("notificationType",notificationType);
        intent.putExtra("flatName",flatName);


        String notificationString= "";
        if(status!=null && status.equals("INSIDE_COMPOUND")){
            notificationString="Visitor Accepted";
        }
        else {
            notificationString= "Visitor Cancel";
        }

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.logotext)
                .setContentTitle(notificationString)
                .setContentText(visitorName)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(visitorContact))

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

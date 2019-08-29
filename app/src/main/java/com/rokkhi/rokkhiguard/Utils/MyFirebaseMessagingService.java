/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rokkhi.rokkhiguard.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rokkhi.rokkhiguard.FullscreenVisitorNormal;
import com.rokkhi.rokkhiguard.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "onMessageReceived: llll " + remoteMessage);



        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            handleNowdataforvisitor(remoteMessage);


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
           // handleNow(remoteMessage);
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }


    private void handleNow(RemoteMessage remoteMessage) {
        String click_Action="";

        if(remoteMessage.getNotification().getClickAction()!=null) click_Action = remoteMessage.getNotification().getClickAction();


        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();



        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "home")
                .setSmallIcon(R.drawable.logooffice35)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody))


                .setLights(Color.WHITE, 3000, 3000)
                .setPriority(NotificationCompat.PRIORITY_MAX);


        Intent intent = new Intent(click_Action);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);


        int mNotificationID = (int)System.currentTimeMillis();
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("home",
                    "Default channel", NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }
        manager.notify(mNotificationID,mBuilder.build());

        Log.d(TAG, "Short lived task is done.");
    }


    private void handleNowdataforvisitor(final RemoteMessage remoteMessage) {

        String subject = remoteMessage.getData().get("subject");
        String body = remoteMessage.getData().get("body");
        String who_add = remoteMessage.getData().get("who_add");
        String time= remoteMessage.getData().get("time");






//        Log.d(TAG, "handleNowdata: " + name+ " "+ org+" "+pic+" "+ response);
        int mNotificationID = (int) System.currentTimeMillis();




        Intent intent1 = new Intent(getApplicationContext(), FullscreenVisitorNormal.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("subject",subject);
        intent1.putExtra("body",body);
        intent1.putExtra("who_add",who_add);
        intent1.putExtra("time",time);
        intent1.putExtra("nid",mNotificationID);





        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.logooffice35)
                .setContentTitle("New Notification arrived")
                .setContentText(subject)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(subject))

                .setLights(Color.WHITE, 3000, 3000)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        startActivity(intent1);








        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                "Default channel", NotificationManager.IMPORTANCE_HIGH);

        manager.createNotificationChannel(channel);
        manager.notify(mNotificationID, mBuilder.build());

        Log.d(TAG, "Short lived task is done.");

    }





    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }


}

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
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rokkhi.rokkhiguard.Activity.WaitingVisitorActivity;
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

        String name = remoteMessage.getData().get("name");
        String where = remoteMessage.getData().get("where");
        String pic = remoteMessage.getData().get("pic");
        String uid= remoteMessage.getData().get("uid");
        String purpose= remoteMessage.getData().get("purpose");
        String type= remoteMessage.getData().get("type");
        String org= remoteMessage.getData().get("org");
        final String click_action= remoteMessage.getData().get("click_action");
        String response = remoteMessage.getData().get("response");

        Log.e(TAG, "handleNowdataforvisitor: org = "+org);
        Log.e(TAG, "handleNowdata: mmm "+ type +" "+ name );


        int mNotificationID = (int) System.currentTimeMillis();



        Intent intent = new Intent(getApplicationContext(), WaitingVisitorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("name",name);
        intent.putExtra("where",where);
        intent.putExtra("pic",pic);
        intent.putExtra("uid",uid);
        intent.putExtra("purpose",purpose);
        intent.putExtra("response",response);
        intent.putExtra("type",type);
        intent.putExtra("org",org);
        intent.putExtra("nid",mNotificationID);


        String notificationString= "";
        if(type!=null && type.equals("gone")){
            notificationString= "A visitor exited!";
        }
        else {
            notificationString="New visitor arrived";
        }

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.logotext)
                .setContentTitle(notificationString)
                .setContentText(name)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(name))

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

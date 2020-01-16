package com.the.marketapp.Other;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private static final String TAG = "MyGcmListenerService";
    private NotificationManager notifManager;
    private NotificationChannel mChannel;
    private int numMessages = 0;
    int NOTIFICATION_ID = 1;
    String type = "";
    boolean hasLoggedIn;

    SharedPreferences preferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        int logedIn = preferences.getInt("logedIn", 0);
        Log.v("logedInNOT",""+logedIn);
        if(logedIn==1)
        {
            hasLoggedIn =true;
        }

        if (hasLoggedIn) {
            if (remoteMessage.getData().size() > 0) {
                //handle the data message here

                Map<String, String> data = remoteMessage.getData();

                String title = data.get("title");
                Log.v("titleNOT",title);

                String body = data.get("body");
                Log.v("bodyNOT",body);

                if (data.containsKey("order_id")) {
                    String id = data.get("order_id");
                    type = "order";
                    sendNotification(title, body, id, type);
                } else if (data.containsKey("product_id")) {
                    String id = data.get("product_id");
                    type = "product";
                    sendNotification(title, body, id, type);
                }
                else
                {
                    String id = "";
                    sendNotification(title, body, id, type);
                }
                //   Log.d("MyNotification", remoteMessage.getData().toString());
            }
        }
    }
    private void sendNotification(String title, String body,String id,String type) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notificationId", NOTIFICATION_ID);


        intent.setClass(this, MainActivity.class);
        if (type.equals("")) {
            intent.putExtra("orderDetailsFragment","");


        } else if (type.equals("order")) {
            intent.putExtra("orderDetailsFragment",type);
            intent.putExtra("orderDetailsFragmentID",id);


        } else if (type.equals("product")) {
            intent.putExtra("orderDetailsFragment",type);
            intent.putExtra("orderDetailsFragmentID",id);


        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService

                    (Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                NotificationChannel mChannel = new NotificationChannel
                        ("0", title, importance);
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setDescription(body);

                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, "0");
            builder.setContentTitle(title)  // flare_icon_30

                    .setSmallIcon(R.mipmap.launcher) // required

                    .setContentText(body)  // required
                    .setShowWhen(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setLights(Color.RED, 1000, 300)
                    .setNumber(++numMessages)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource
                            (getResources(), R.mipmap.launcher))
                    .setBadgeIconType(R.mipmap.launch)
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{100, 200, 300, 400,
                            500, 400, 300, 200, 400});
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;
            builder.setDefaults(defaults);
        } else {

            builder = new NotificationCompat.Builder(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentTitle(title)  // flare_icon_30

                    .setSmallIcon(R.drawable.launcher) // required
                    .setContentText(body)  // required
                    .setShowWhen(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setLights(Color.RED, 1000, 300)
                    .setNumber(++numMessages)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource
                            (getResources(), R.mipmap.launch))
                    .setBadgeIconType(R.mipmap.launch)
                    .setContentIntent(pendingIntent);

            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;
            builder.setDefaults(defaults);
            builder.setAutoCancel(true);
        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Notification notification = builder.build();
        notifManager.notify(0, notification);
    }
}
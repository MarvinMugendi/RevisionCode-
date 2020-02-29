package com.marvedie.notifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button_notify;
    private Button button_cancel;
    private Button button_update;

    //Every notification channel must be associated with an ID that is unique within your package
    //Android API level 26+ allows use of notification channnels
    /*Best Practice

    Target the latest available SDK.
    Check the device's SDK version in your code. If the SDK version is 26 or higher, build notification channels.
    */
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    //Assign Notifiction with a notification ID
    private static final int NOTIFICATION_ID = 0;

    //unique constant member variable to represent the update notification action for your broadcast
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";


    // Android system uses the NotificationManager class to deliver notifications to the user.
    private NotificationManager mNotifyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();


        // register your broadcast receiver to receive ACTION_UPDATE_NOTIFICATION intent
        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));

        //Initialize Button
        button_notify = findViewById(R.id.notify);

        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        button_cancel = findViewById(R.id.cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancel notification
                cancelNotification();
            }
        });

        button_update = findViewById(R.id.update);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update notification
                updateNotification();
            }
        });

        setNotificationButtonState(true, false, false);
    }

    public void sendNotification(){

        //Add update action to the notification
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);

        //Use getBroadcast() to get a PendingIntent
        //o make sure that this pending intent is sent and used only once, set FLAG_ONE_SHOT
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID,
                updateIntent, PendingIntent.FLAG_ONE_SHOT);



        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        // Add the action button using the pending intent.
        notifyBuilder.addAction(R.drawable.ic_update,
                getString(R.string.update_me), updatePendingIntent);

        // Deliver the notification.
        mNotifyManager.notify(NOTIFICATION_ID,notifyBuilder.build());

        /// Enable the update and cancel buttons but disables the "Notify
        //// Me!" button.
        setNotificationButtonState(false, true, true);
    }

    public void createNotificationChannel() {

        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Condition to check android API level. Notification channels only available in API 26+

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //Fun fact Capital O translates to int 26
            //Create a Notification Channel

            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager.IMPORTANCE_HIGH);


            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");


            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
        //getNotificationBuilder method
        private NotificationCompat.Builder getNotificationBuilder()
        {

            //Intent to launch MainActivity
            Intent notificationIntent = new Intent(this, MainActivity.class);

            //Create Pending Intent using getActivity() method
            PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,
                    notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            //Create and Instantiate notification builder
            NotificationCompat.Builder notifyBuilder = new NotificationCompat
                        .Builder(this, PRIMARY_CHANNEL_ID)
                        .setContentTitle("You've been notified!")
                        .setContentText("This is your notification text.")
                        .setSmallIcon(R.drawable.ic_androidicon)
                        //set the content Intent
                        .setContentIntent(notificationPendingIntent)
                        //closes notification when clicked
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL);

                return notifyBuilder;

        }

        //insert a condensable notification image
    public void updateNotification(){
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(),R.drawable.mascot_1);

        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!"));

        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        setNotificationButtonState(false, false, true);
    }

    //cancel notification
    public void cancelNotification(){

        mNotifyManager.cancel(NOTIFICATION_ID);

        setNotificationButtonState(true, false, false);
    }

    public void setNotificationButtonState(Boolean isNotifyEnabled,
                                           Boolean isUpdateEnabled,
                                           Boolean isCancelEnabled) {
        button_notify.setEnabled(isNotifyEnabled);
        button_update.setEnabled(isUpdateEnabled);
        button_cancel.setEnabled(isCancelEnabled);
    }
    private NotificationReceiver mReceiver = new NotificationReceiver();

    public class NotificationReceiver extends BroadcastReceiver{

        public NotificationReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            //Update the Notification
            updateNotification();
        }
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}





package com.marvedie.notifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button_notify;

    //Every notification channel must be associated with an ID that is unique within your package
    //Android API level 26+ allows use of notification channnels
    /*Best Practice

    Target the latest available SDK.
    Check the device's SDK version in your code. If the SDK version is 26 or higher, build notification channels.
    */
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    //Assign Notifiction with a notification ID
    private static final int NOTIFICATION_ID = 0;


    // Android system uses the NotificationManager class to deliver notifications to the user.
    private NotificationManager mNotifyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();


        //Initialize Button
        button_notify = findViewById(R.id.notify);

        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });


    }

    public void sendNotification(){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID,notifyBuilder.build());
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
            //Create and Instantiate notification builder
            NotificationCompat.Builder notifyBuilder = new NotificationCompat
                        .Builder(this, PRIMARY_CHANNEL_ID)
                        .setContentTitle("You've been notified!")
                        .setContentText("This is your notification text.")
                        .setSmallIcon(R.drawable.ic_androidicon);

                return notifyBuilder;

        }
    }





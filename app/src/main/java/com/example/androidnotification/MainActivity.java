package com.example.androidnotification;


import android.app.*;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


public class MainActivity extends AppCompatActivity {


    public NotificationManager notificationManager;
    private static final int NOTIF_ID = 0;
    private static final String CHANNEL_ID = BuildConfig.APPLICATION_ID + "notification-channel";
    public static final String UPDATE_EVENT = "UPDATE_EVENT";
    public static MainActivity instance;
    private NotificationReceiver notificationReceiver;

    public Button notifButton;
    public Button cancelButton;
    public Button updateButton;


    public MainActivity(){
        super();
        this.setInstance();
    }

    public void setInstance(){
        MainActivity.instance = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setInstance();

        notificationReceiver = new NotificationReceiver();
        registerReceiver(notificationReceiver, new IntentFilter(UPDATE_EVENT));

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notifButton = findViewById(R.id.notif_btn);
        updateButton = findViewById(R.id.update_btn);
        cancelButton = findViewById(R.id.cancel_btn);


        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.updateNotification(MainActivity.instance);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();
            }
        });

        notifButton.setEnabled(true);
        cancelButton.setEnabled(false);
        updateButton.setEnabled(false);
    }

    private void sendNotification(){
        Intent contentIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingContentIntent = PendingIntent.getActivity(getApplicationContext(),NOTIF_ID, contentIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        String URL = "https://linktr.ee/KevinAS28";
        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        PendingIntent pendingLearnMoreIntent = PendingIntent.getActivity(getApplicationContext(), NOTIF_ID,learnMoreIntent, PendingIntent.FLAG_ONE_SHOT);
        Intent updateIntent = new Intent(UPDATE_EVENT);
        PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(getApplicationContext(), NOTIF_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder built = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        built.setContentTitle("Hello there!");
        built.setContentText("Let's get knowing each other!");
        built.setSmallIcon(R.drawable.ic_notification);
        built.setContentIntent(pendingContentIntent);
        built.setPriority(NotificationCompat.PRIORITY_HIGH);
        built.addAction(R.drawable.ic_notification, "Click me to know more", pendingLearnMoreIntent);
        built.addAction(R.drawable.ic_notification, "Update", pendingUpdateIntent);

        Notification notif = built.build();
        notificationManager.notify(NOTIF_ID, notif);

        notifButton.setEnabled(false);
        cancelButton.setEnabled(true);
        updateButton.setEnabled(true);
    }
    private void cancelNotification(){
        notificationManager.cancel(NOTIF_ID);

        notifButton.setEnabled(true);
        cancelButton.setEnabled(false);
        updateButton.setEnabled(false);
    }

    public static void updateNotification(MainActivity activity){
        Intent contentIntent = new Intent(activity.getApplicationContext(), MainActivity.class);
        PendingIntent pendingContentIntent = PendingIntent.getActivity(activity.getApplicationContext(),NOTIF_ID, contentIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder built = new NotificationCompat.Builder(activity.getApplicationContext(), CHANNEL_ID);
        String info = String.valueOf(Build.VERSION.SDK_INT) + " | " + String.valueOf(Build.VERSION_CODES.O);
        built.setContentTitle("Please click the first button :(");
        built.setContentText(info);
        built.setSmallIcon(R.drawable.ic_notification);
        built.setContentIntent(pendingContentIntent);
        built.setPriority(NotificationCompat.PRIORITY_DEFAULT);


        Bitmap mascotBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.mascot_1);
        built.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(mascotBitmap).setBigContentTitle("The Notification has been updated"));

        Notification notif = built.build();
        activity.notificationManager.notify(NOTIF_ID, notif);

        activity.notifButton.setEnabled(false);
        activity.cancelButton.setEnabled(true);
        activity.updateButton.setEnabled(false);
    }

}
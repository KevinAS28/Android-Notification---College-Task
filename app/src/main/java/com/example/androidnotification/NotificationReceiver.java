package com.example.androidnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == MainActivity.UPDATE_EVENT){
            MainActivity.updateNotification(MainActivity.instance);
        }
    }


}

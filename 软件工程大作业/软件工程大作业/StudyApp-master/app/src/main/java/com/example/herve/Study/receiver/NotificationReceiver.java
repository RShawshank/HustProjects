package com.example.herve.Study.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.herve.Study.utils.system.SystemUtils;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //判断app进程是否存活
        if (SystemUtils.isAppAlive(context, "com.example.herve.notifidemo")) {
            Log.i("NotificationReceiver", "the app process is alive");

        } else {
            Log.i("NotificationReceiver", "the app process is not alive");

        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }
}
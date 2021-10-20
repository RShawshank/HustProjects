package com.example.herve.Study.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShowNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "RepeatReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        JPush(context, intent);
    }

    private void JPush(Context context, Intent intent) {
//        Log.d(TAG, "ShowNotificationReceiver onReceive");
//        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
//        Bundle bundle = intent.getExtras();
//        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//        String extra2 = bundle.getString(JPushInterface.EXTRA_EXTRA);
//
//        Log.i(TAG, "onReceive: extra2=" + extra2);
//        if (extra2 != null) {
//
//            JSONObject json = null;
//            try {
//                json = new JSONObject(extra2);
//                String url = json.getString("url");
//                String tab = json.getString("tab");
//                Log.i(TAG, "onReceive: url=" + url);
//                Log.i(TAG, "onReceive: tab=" + tab);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        PendingIntent pendingIntent = PendingIntent.
//                getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setContentTitle(title)
//                .setTicker("这是通知的ticker")
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(android.R.drawable.ic_lock_idle_charging);
//
//        Log.i("repeat", "showNotification");
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(2, builder.build());
    }
}
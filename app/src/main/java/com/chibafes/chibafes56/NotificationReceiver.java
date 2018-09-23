package com.chibafes.chibafes56;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String ticker = intent.getStringExtra("TICKER");
        String message = intent.getStringExtra("MESSAGE");

        Intent sendIntent = new Intent(context, MainActivity.class);
        PendingIntent sender = PendingIntent.getActivity(context, 0, sendIntent, 0);

        //通知オブジェクトの生成
        Notification noti = new NotificationCompat.Builder(context)
                .setContentTitle("第56回千葉大祭")
                .setTicker(ticker)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(sender)
                .build();

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(0, noti);
        }
    }

}
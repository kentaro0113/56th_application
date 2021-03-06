package com.chibafes.chibafes56;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

class NotificationUtil {

    static void setLocalNotification(Context context, String ticker, String message, int requestCode, Calendar calendar){
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("TICKER", ticker);
        intent.putExtra("MESSAGE", message);
        PendingIntent sender = PendingIntent.getBroadcast(context,  requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }

    static void cancelLocalNotification(Context context, int requestCode){
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(sender);
        }
    }

}
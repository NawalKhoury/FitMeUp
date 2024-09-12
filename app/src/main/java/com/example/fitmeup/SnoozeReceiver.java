package com.example.fitmeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import java.util.Calendar;

public class SnoozeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);

        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra("title", title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(context, "Snoozed for 2 minutes", Toast.LENGTH_SHORT).show();
    }
}

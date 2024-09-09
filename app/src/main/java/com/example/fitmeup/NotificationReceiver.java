package com.example.fitmeup;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");

        // Create an intent for the Snooze action
        Intent snoozeIntent = new Intent(context, SnoozeReceiver.class);
        snoozeIntent.putExtra("title", title);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create an intent for the OK action (dismiss notification)
        Intent okIntent = new Intent(context, DismissReceiver.class);
        okIntent.putExtra("notificationId", 1); // Use 1 as the notification ID for simplicity
        PendingIntent okPendingIntent = PendingIntent.getBroadcast(context, 1, okIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Reminder3Activity.CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText("It's time to drink water!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.snooze, "Snooze 2 min", snoozePendingIntent)
                .addAction(R.drawable.ok, "OK", okPendingIntent)
                .setAutoCancel(true);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}

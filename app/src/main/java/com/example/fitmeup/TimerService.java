package com.example.fitmeup;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

public class TimerService extends Service {

    private final int NOTIFICATION_ID = 1;
    private final String CHANNEL_ID = "TimerServiceChannel";
    private final IBinder binder = new TimerBinder();

    private Handler handler = new Handler();
    private long startTime;
    private boolean isRunning = false;

    public class TimerBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            startTime = SystemClock.elapsedRealtime(); // Save the start time
            handler.postDelayed(timerRunnable, 1000); // Start the timer loop
            isRunning = true;
        }
        return START_STICKY;
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long elapsedTime = SystemClock.elapsedRealtime() - startTime;
            int seconds = (int) (elapsedTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            // Update the notification with the elapsed time
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            handler.postDelayed(this, 1000); // Continue the timer loop
        }
    };

    // Return the elapsed time in seconds
    public long getElapsedTime() {
        return SystemClock.elapsedRealtime() - startTime;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Timer Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timerRunnable);
    }
}

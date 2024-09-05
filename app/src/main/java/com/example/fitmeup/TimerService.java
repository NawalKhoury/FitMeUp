package com.example.fitmeup;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class TimerService extends Service {

    private final IBinder binder = new LocalBinder();
    private int seconds = 0;
    private boolean isRunning = false;
    private Handler handler = new Handler();
    private TimerListener timerListener;

    public class LocalBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    public interface TimerListener {
        void onTick(int hours, int minutes, int seconds);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void startTimer() {
        isRunning = true;
        handler.postDelayed(runnable, 1000);
    }

    public void stopTimer() {
        isRunning = false;
        handler.removeCallbacks(runnable);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setTimerListener(TimerListener listener) {
        this.timerListener = listener;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seconds++;
            if (timerListener != null) {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                timerListener.onTick(hours, minutes, secs);
            }
            if (isRunning) {
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // If the timer is not already running, start it
        if (!isRunning) {
            startTimer();
        }

        // Keep the service running until explicitly stopped
        return START_STICKY;
    }
}
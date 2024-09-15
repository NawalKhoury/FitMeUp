package com.example.fitmeup;

public class TimeFormatUtil {
    public static String formatTime(int seconds) {

        int totalHours = seconds / 3600;
        int totalMinutes = (seconds % 3600) / 60;
        int totalSeconds = seconds % 60;

        return String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds);
    }
}

package com.example.fitmeup;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminder_table")
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String chooseTime;  // For the selected time
    private String remindEvery;  // For the "remind me every" field
    private boolean isNotificationEnabled;  // To store the state of the notification switch

    // Constructor
    public Reminder(String title, String chooseTime, String remindEvery, boolean isNotificationEnabled) {
        this.title = title;
        this.chooseTime = chooseTime;
        this.remindEvery = remindEvery;
        this.isNotificationEnabled = isNotificationEnabled;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChooseTime() {
        return chooseTime;
    }

    public void setChooseTime(String chooseTime) {
        this.chooseTime = chooseTime;
    }

    public String getRemindEvery() {
        return remindEvery;
    }

    public void setRemindEvery(String remindEvery) {
        this.remindEvery = remindEvery;
    }

    public boolean isNotificationEnabled() {
        return isNotificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        isNotificationEnabled = notificationEnabled;
    }
}

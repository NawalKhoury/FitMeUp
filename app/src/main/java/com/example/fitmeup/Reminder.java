package com.example.fitmeup;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminder_table")
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String startTime;
    private String endTime;
    private String remindEvery;

    // Constructor
    public Reminder(String title, String startTime, String endTime, String remindEvery) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.remindEvery = remindEvery;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemindEvery() {
        return remindEvery;
    }

    public void setRemindEvery(String remindEvery) {
        this.remindEvery = remindEvery;
    }
}

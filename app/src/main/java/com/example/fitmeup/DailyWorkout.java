package com.example.fitmeup;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "daily_workouts")
public class DailyWorkout {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String workoutType;
    public Date date;  // Date field
    public int caloriesBurned;
    public String icon;
}

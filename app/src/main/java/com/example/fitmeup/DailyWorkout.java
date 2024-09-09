package com.example.fitmeup;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "daily_workouts")
public class DailyWorkout {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date;
    private int calories;
    private int stepsCounter;
    private int distance;
    private int waterCups;
    private int workoutTime;
    private String Description;

    // Empty constructor
    public DailyWorkout() {}

    // Parameterized constructor
    public DailyWorkout(Date date, int calories, int stepsCounter, int distance, int waterCups, int workoutTime, String description) {
        this.date = date;
        this.calories = calories;
        this.stepsCounter = stepsCounter;
        this.distance = distance;
        this.waterCups = waterCups;
        this.workoutTime = workoutTime;
        this.Description = description;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getStepsCounter() {
        return stepsCounter;
    }

    public void setStepsCounter(int stepsCounter) {
        this.stepsCounter = stepsCounter;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getWaterCups() {
        return waterCups;
    }

    public void setWaterCups(int waterCups) {
        this.waterCups = waterCups;
    }

    public int getWorkoutTime() {
        return workoutTime;
    }

    public void setWorkoutTime(int workoutTime) {
        this.workoutTime = workoutTime;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    @Override
    public String toString() {
        return "DailyWorkout{" +
                "id=" + id +
                ", date=" + date +
                ", calories=" + calories +
                ", stepsCounter=" + stepsCounter +
                ", distance=" + distance +
                ", waterCups=" + waterCups +
                ", workoutTime=" + workoutTime +
                ", Description='" + Description + '\'' +
                '}';
    }
}

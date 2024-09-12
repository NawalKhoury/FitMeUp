package com.example.fitmeup;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "daily_workouts")
public class DailyWorkout {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date;
    private int calory;
    private int stepsCounter;
    private int distans;
    private int waterCups;
    private ArrayList<Workout> workoutDailyHistory;
    private String description;

    // Empty constructor
    public DailyWorkout() {
    }

    public DailyWorkout(int calory, Date date, int stepsCounter, int distans, int waterCups, ArrayList<Workout> workoutDailyHistory, String description) {
        this.calory = calory;
        this.date = date;
        this.stepsCounter = stepsCounter;
        this.distans = distans;
        this.waterCups = waterCups;
        this.workoutDailyHistory = workoutDailyHistory;
        this.description = description;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and Setters for other fields
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCalory() {
        return calory;
    }

    public void setCalory(int calory) {
        this.calory = calory;
    }

    public int getStepsCounter() {
        return stepsCounter;
    }

    public void setStepsCounter(int stepsCounter) {
        this.stepsCounter = stepsCounter;
    }

    public int getDistans() {
        return distans;
    }

    public void setDistans(int distans) {
        this.distans = distans;
    }

    public ArrayList<Workout> getWorkoutDailyHistory() {
        return workoutDailyHistory;
    }

    public void setWorkoutDailyHistory(ArrayList<Workout> workoutDailyHistory) {
        this.workoutDailyHistory = workoutDailyHistory;
    }

    public int getWaterCups() {
        return waterCups;
    }

    public void setWaterCups(int waterCups) {
        this.waterCups = waterCups;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "DailyWorkout{" +
                "id=" + id +
                ", date=" + date +
                ", calory=" + calory +
                ", stepsCounter=" + stepsCounter +
                ", distans=" + distans +
                ", waterCups=" + waterCups +
                ", workoutDailyHistory=" + workoutDailyHistory +
                ", description='" + description + '\'' +
                '}';
    }
}

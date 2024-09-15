package com.example.fitmeup;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "workout")
public class Workout {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String workoutType;
    private Date date;
    private int caloriesBurned;
    private String icon;
    private int userId;
    private int totalNumberOfSeconds;

    // Constructors
    public Workout(String workoutType, Date date, int caloriesBurned, String icon,int totalNumberOfSeconds ,int userId) {
        this.workoutType = workoutType;
        this.date = date;
        this.caloriesBurned = caloriesBurned;
        this.icon = icon;
        this.userId = userId;
        this.totalNumberOfSeconds = totalNumberOfSeconds;
    }

    @Ignore
    public Workout() {}

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTotalNumberOfSeconds() {
        return totalNumberOfSeconds;
    }

    public void setTotalNumberOfSeconds(int totalNumberOfSeconds) {
        this.totalNumberOfSeconds = totalNumberOfSeconds;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", workoutType='" + workoutType + '\'' +
                ", date=" + date +
                ", caloriesBurned=" + caloriesBurned +
                ", icon='" + icon + '\'' +
                '}';
    }
}

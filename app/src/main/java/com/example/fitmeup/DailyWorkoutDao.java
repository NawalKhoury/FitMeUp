package com.example.fitmeup;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;
import java.util.Date;

@Dao
public interface DailyWorkoutDao {
    @Insert
    void insert(DailyWorkout dailyWorkout);

    @Update
    void update(DailyWorkout dailyWorkout);

    @Delete
    void delete(DailyWorkout dailyWorkout);

    @Query("SELECT * FROM daily_workouts WHERE id = :id LIMIT 1")
    DailyWorkout getWorkoutById(int id);

    @Query("SELECT * FROM daily_workouts ORDER BY date DESC")
    List<DailyWorkout> getAllWorkouts();

    @Query("SELECT * FROM daily_workouts WHERE date = :date LIMIT 1")
    DailyWorkout getWorkoutByDate(Date date);
}
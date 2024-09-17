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

    @Query("SELECT * FROM daily_workouts WHERE id = :id")
    DailyWorkout getWorkoutById(int id);

    @Query("SELECT * FROM daily_workouts ORDER BY date DESC")
    List<DailyWorkout> getAllWorkouts();

    @Query("SELECT * FROM daily_workouts WHERE date = :date And userId = :userId ORDER BY date DESC")
    List<DailyWorkout> getWorkoutsByDate(Date date, String userId);

    @Query("SELECT * FROM daily_workouts WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    List<DailyWorkout> getWorkoutsForWeek(Date startDate, Date endDate);

    @Query("SELECT dw.* FROM daily_workouts AS dw " +
            "INNER JOIN ( " +
            "    SELECT MAX(date) AS maxDate, strftime('%Y-%m-%d', date / 1000, 'unixepoch') AS workoutDay " +
            "    FROM daily_workouts " +
            "    WHERE userId = :userId AND date BETWEEN :startDate AND :endDate " +
            "    GROUP BY workoutDay " +
            ") AS latest_workouts " +
            "ON strftime('%Y-%m-%d', dw.date / 1000, 'unixepoch') = latest_workouts.workoutDay AND dw.date = latest_workouts.maxDate " +
            "WHERE dw.userId = :userId " +
            "ORDER BY dw.date ASC")
    List<DailyWorkout> getWorkoutsForWeek(int userId, long startDate, long endDate);


}

    package com.example.fitmeup;

    import androidx.lifecycle.LiveData;
    import androidx.room.Dao;
    import androidx.room.Delete;
    import androidx.room.Insert;
    import androidx.room.Query;
    import androidx.room.Update;

    import java.util.List;

    @Dao
    public interface WorkoutDao {

        // Insert a single workout
        @Insert
        void insert(Workout workout);

        // Insert multiple workouts
        @Insert
        void insertAll(List<Workout> workouts);

        // Update a workout
        @Update
        void update(Workout workout);

        // Delete a workout
        @Delete
        void delete(Workout workout);

        // Query to get all workouts, ordered by date in descending order
        @Query("SELECT * FROM workout ORDER BY date DESC")
        LiveData<List<Workout>> getAllWorkouts();

        // Query to get a workout by id
        @Query("SELECT * FROM workout WHERE id = :id LIMIT 1")
        Workout getWorkoutById(int id);

        // Query to get all workouts for a specific user, ordered by date in descending order
        @Query("SELECT * FROM workout WHERE userId = :userId ORDER BY date DESC")
        LiveData<List<Workout>> getAllWorkoutsForUser(int userId);

        @Query("SELECT * FROM workout WHERE userId = :userId ORDER BY date DESC LIMIT 1")
        LiveData<Workout> getLastWorkoutForUser(int userId);



    }

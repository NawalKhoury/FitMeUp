package com.example.fitmeup;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Insert
    void insertExercise(Exercise exercise);

    @Query("SELECT * FROM exercises WHERE muscle = :muscle AND description = :description AND videoLink = :videoLink")
    List<Exercise> getExerciseByDetails(String muscle, String description, String videoLink);

    @Query("SELECT * FROM exercises WHERE muscle = :muscle")
    List<Exercise> getExercisesByMuscle(String muscle);
}

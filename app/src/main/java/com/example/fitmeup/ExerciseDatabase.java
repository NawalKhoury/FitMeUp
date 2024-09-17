package com.example.fitmeup;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Exercise.class}, version = 1, exportSchema = false)
public abstract class ExerciseDatabase extends RoomDatabase {

    private static volatile ExerciseDatabase INSTANCE;

    public abstract ExerciseDao exerciseDao();

    public static ExerciseDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ExerciseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ExerciseDatabase.class, "exercise_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

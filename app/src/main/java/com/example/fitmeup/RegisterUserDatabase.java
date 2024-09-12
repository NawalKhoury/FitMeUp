package com.example.fitmeup;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;

@Database(entities = {RegisterUser.class, DailyWorkout.class, Workout.class}, version = 1) // Incremented version to 5
@TypeConverters({Converters.class}) // Include converters if needed
public abstract class RegisterUserDatabase extends RoomDatabase {

    // Singleton instance
    private static RegisterUserDatabase instance;

    // DAOs
    public abstract RegisterUserDao registerUserDao();
    public abstract DailyWorkoutDao dailyWorkoutDao();
    public abstract WorkoutDao WorkoutDao();

    // Singleton instance of the database
    public static synchronized RegisterUserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            RegisterUserDatabase.class, "fitmeup_database")
                    .fallbackToDestructiveMigration() // Optional: Wipes the DB if migration fails (for development)
                    .build();
        }
        return instance;
    }
}

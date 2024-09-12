package com.example.fitmeup;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;

@Database(entities = {RegisterUser.class, DailyWorkout.class, Workout.class}, version = 3) // Updated version number
@TypeConverters({Converters.class})
public abstract class RegisterUserDatabase extends RoomDatabase {

    private static RegisterUserDatabase instance;

    public abstract RegisterUserDao registerUserDao();
    public abstract DailyWorkoutDao dailyWorkoutDao();
    public abstract WorkoutDao WorkoutDao();

    // Example migration from version 2 to 3
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add a new column to the workout table
            // You can replace this with actual changes based on your schema changes
            database.execSQL("ALTER TABLE workout ADD COLUMN new_column_name TEXT");
        }
    };

    public static synchronized RegisterUserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            RegisterUserDatabase.class, "fitmeup_database")
                    // Add migrations to handle schema changes without data loss
                    .addMigrations(MIGRATION_2_3)  // Add other migrations as needed
                    // OR fallback to destructive migration if you don't need to preserve data
                    .fallbackToDestructiveMigration()  // Deletes old data if version mismatch
                    .build();
        }
        return instance;
    }
}

package com.example.fitmeup;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;

@Database(entities = {RegisterUser.class, DailyWorkout.class, Workout.class, Reminder.class, ImageEntity.class}, version = 3)
@TypeConverters({Converters.class}) // Include converters if needed
public abstract class RegisterUserDatabase extends RoomDatabase {

    // Singleton instance
    private static RegisterUserDatabase instance;


    // DAO for user registration
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Migration logic here
            // For example, if you added a new column:
            database.execSQL("ALTER TABLE reminder_table ADD COLUMN newColumn TEXT");
        }
    };
    // DAOs
    public abstract RegisterUserDao registerUserDao();
    public abstract ReminderDao reminderDao();
    public abstract DailyWorkoutDao dailyWorkoutDao();
    public abstract WorkoutDao WorkoutDao();
    public abstract ImageDao imageDao();

    // Singleton instance of the database
    public static synchronized RegisterUserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            RegisterUserDatabase.class, "fitmeup_database")


                    .fallbackToDestructiveMigration()  // Handle schema migrations

                    .build();
        }
        return instance;
    }
}

package com.example.fitmeup;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {RegisterUser.class, DailyWorkout.class}, version = 2)
@TypeConverters({Converters.class})  // Add the Converters class here
public abstract class RegisterUserDatabase extends RoomDatabase {

    private static RegisterUserDatabase instance;

    public abstract RegisterUserDao registerUserDao();
    public abstract DailyWorkoutDao dailyWorkoutDao();

    public static synchronized RegisterUserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            RegisterUserDatabase.class, "fitmeup_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

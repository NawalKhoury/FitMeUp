package com.example.fitmeup;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RegisterUser.class}, version = 1)
public abstract class RegisterUserDatabase extends RoomDatabase {

    private static RegisterUserDatabase instance;

    public abstract RegisterUserDao registerUserDao();

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

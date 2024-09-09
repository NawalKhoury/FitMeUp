package com.example.fitmeup;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {RegisterUser.class, Reminder.class}, version = 1)
public abstract class RegisterUserDatabase extends RoomDatabase {

    private static RegisterUserDatabase instance;

    // DAO for reminders
    public abstract ReminderDao reminderDao();

    // DAO for user registration
    public abstract RegisterUserDao registerUserDao();
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Migration logic here
            // For example, if you added a new column:
            database.execSQL("ALTER TABLE reminder_table ADD COLUMN newColumn TEXT");
        }
    };
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

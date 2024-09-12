package com.example.fitmeup;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert
    void insertReminder(Reminder reminder);

    @Update
    void updateReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);

    @Query("DELETE FROM reminder_table")
    void deleteAllReminders();

    @Query("SELECT * FROM reminder_table")
    LiveData<List<Reminder>> getAllReminders();
}

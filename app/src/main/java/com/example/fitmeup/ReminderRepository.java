package com.example.fitmeup;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderRepository {

    private ReminderDao reminderDao;
    private LiveData<List<Reminder>> allReminders;
    private final ExecutorService executorService;

    public ReminderRepository(Application application) {
        RegisterUserDatabase database = RegisterUserDatabase.getInstance(application);
        reminderDao = database.reminderDao();
        allReminders = reminderDao.getAllReminders();
        executorService = Executors.newFixedThreadPool(2); // Use a thread pool
    }

    public void insert(Reminder reminder) {
        executorService.execute(() -> reminderDao.insertReminder(reminder));
    }

    public void update(Reminder reminder) {
        executorService.execute(() -> reminderDao.updateReminder(reminder));
    }

    public void delete(Reminder reminder) {
        executorService.execute(() -> reminderDao.deleteReminder(reminder));
    }

    public void deleteAllReminders() {
        executorService.execute(() -> reminderDao.deleteAllReminders());
    }

    public LiveData<List<Reminder>> getAllReminders() {
        return allReminders;
    }
}

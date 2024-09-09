package com.example.fitmeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Reminder3Activity extends AppCompatActivity {

    private LinearLayout reminderContainer;
    private ReminderViewModel reminderViewModel;
    public static final String CHANNEL_ID = "reminder_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder3);

        reminderContainer = findViewById(R.id.reminderContainer);
        FloatingActionButton addReminderButton = findViewById(R.id.addReminderButton);

        // Initialize ViewModel
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        // Show dialog for adding a new reminder
        addReminderButton.setOnClickListener(v -> showAddReminderDialog(null, null, null, null, null));

        // Load existing reminders from ViewModel and add them to the UI
        reminderViewModel.getAllReminders().observe(this, reminders -> {
            if (reminders != null) {
                reminderContainer.removeAllViews();
                for (Reminder reminder : reminders) {
                    CardView reminderCard = createReminderCard(reminder.getTitle(), reminder.getStartTime(), reminder.getEndTime(), reminder.getRemindEvery(), reminder);
                    reminderContainer.addView(reminderCard);
                }
            }
        });
    }

    // Overloaded method to handle new reminder creation
    private void showAddReminderDialog(CardView reminderCard, String title, String startTime, String endTime, String remindEvery) {
        showAddReminderDialog(reminderCard, title, startTime, endTime, remindEvery, null);
    }

    // Full method to handle both creating and updating reminders
    private void showAddReminderDialog(CardView reminderCard, String title, String startTime, String endTime, String remindEvery, Reminder reminder) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_reminder, null);

        EditText reminderTitleEdit = dialogView.findViewById(R.id.reminderTitleEdit);
        Spinner startTimeSpinner = dialogView.findViewById(R.id.startTimeSpinner);
        Spinner endTimeSpinner = dialogView.findViewById(R.id.endTimeSpinner);
        Spinner remindEverySpinner = dialogView.findViewById(R.id.remindEverySpinner);

        // Pre-populate fields if editing
        if (title != null) {
            reminderTitleEdit.setText(title);
        }

        // Set up spinners for time
        List<String> timeIntervals = generateTimeIntervals();
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeIntervals);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeSpinner.setAdapter(timeAdapter);
        endTimeSpinner.setAdapter(timeAdapter);

        if (startTime != null) {
            startTimeSpinner.setSelection(timeIntervals.indexOf(startTime));
        }
        if (endTime != null) {
            endTimeSpinner.setSelection(timeIntervals.indexOf(endTime));
        }

        // Set up the "remind every" spinner
        ArrayAdapter<CharSequence> remindEveryAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_options, android.R.layout.simple_spinner_item);
        remindEveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remindEverySpinner.setAdapter(remindEveryAdapter);

        if (remindEvery != null) {
            remindEverySpinner.setSelection(remindEveryAdapter.getPosition(remindEvery));
        }

        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle(reminderCard == null ? "Add Your Reminder" : "Edit Your Reminder")
                .setPositiveButton("OK", (dialog, which) -> {
                    String newTitle = reminderTitleEdit.getText().toString().trim();
                    String newStartTime = startTimeSpinner.getSelectedItem().toString();
                    String newEndTime = endTimeSpinner.getSelectedItem().toString();
                    String newRemindEvery = remindEverySpinner.getSelectedItem().toString();

                    if (newTitle.isEmpty()) {
                        Toast.makeText(Reminder3Activity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if (!isValidPeriod(newStartTime, newEndTime)) {
                        Toast.makeText(Reminder3Activity.this, "Invalid period! Start time must be before end time.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (reminderCard == null) {
                            // Add new reminder
                            addNewReminder(newTitle, newStartTime, newEndTime, newRemindEvery);
                        } else {
                            // Update existing reminder
                            updateReminder(reminderCard, reminder, newTitle, newStartTime, newEndTime, newRemindEvery);
                        }
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // Method to generate time intervals
    private List<String> generateTimeIntervals() {
        List<String> timeIntervals = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            String hourString = (hour < 10 ? "0" : "") + hour;
            timeIntervals.add(hourString + ":00");
            timeIntervals.add(hourString + ":30");
        }
        return timeIntervals;
    }

    // Method to add a new reminder
    private void addNewReminder(String title, String startTime, String endTime, String remindEvery) {
        Reminder reminder = new Reminder(title, startTime, endTime, remindEvery);
        reminderViewModel.insert(reminder);
        CardView newCard = createReminderCard(title, startTime, endTime, remindEvery, reminder);
        reminderContainer.addView(newCard);

        // Schedule notifications for the reminder
        scheduleNotifications(title, startTime, endTime, remindEvery);

        // Open the details dialog or activity for the newly created reminder
        showReminderDetails(reminder);
    }

    // Method to create a reminder card based on the provided XML layout
    private CardView createReminderCard(String title, String startTime, String endTime, String remindEvery, Reminder reminder) {
        LayoutInflater inflater = LayoutInflater.from(this);

        // Inflate the custom XML layout which is a LinearLayout, not a CardView
        View cardViewLayout = inflater.inflate(R.layout.reminder_item, null);

        // Find individual components within the inflated layout
        TextView reminderTitleView = cardViewLayout.findViewById(R.id.reminderTitle);
        TextView startTimeLabel = cardViewLayout.findViewById(R.id.startTimeLabel);
        TextView endTimeLabel = cardViewLayout.findViewById(R.id.endTimeLabel);
        TextView remindEveryLabel = cardViewLayout.findViewById(R.id.remindEveryLabel);
        Switch reminderSwitch = cardViewLayout.findViewById(R.id.reminderSwitch);
        ImageView editIcon = cardViewLayout.findViewById(R.id.editReminder);
        ImageView deleteIcon = cardViewLayout.findViewById(R.id.deleteReminder);

        // Set the data
        reminderTitleView.setText(title);
        startTimeLabel.setText("Start Time: " + startTime);
        endTimeLabel.setText("End Time: " + endTime);
        remindEveryLabel.setText("Remind Every: " + remindEvery);

        // Set up click listeners for edit and delete icons
        editIcon.setOnClickListener(v -> {
            // Show dialog to edit this reminder
            showAddReminderDialog((CardView) cardViewLayout.getParent(), title, startTime, endTime, remindEvery, reminder);
        });

        deleteIcon.setOnClickListener(v -> {
            // Delete reminder from the database and UI
            deleteReminder(reminder, (CardView) cardViewLayout.getParent());
        });

        // Create a CardView to wrap the LinearLayout
        CardView card = new CardView(this);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        card.setCardElevation(4);
        card.setRadius(12);
        card.setPadding(16, 16, 16, 16);

        // Add the LinearLayout as a child of the CardView
        card.addView(cardViewLayout);

        return card;
    }


    // Method to show reminder details (could open a dialog or new activity)
    private void showReminderDetails(Reminder reminder) {
        Toast.makeText(this, "Reminder: " + reminder.getTitle() + "\nStart: " + reminder.getStartTime() + "\nEnd: " + reminder.getEndTime(), Toast.LENGTH_LONG).show();
    }

    // Method to check if the period is valid
    private boolean isValidPeriod(String startTime, String endTime) {
        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int endHour = Integer.parseInt(endTime.split(":")[0]);

        // Check if the start time is before the end time
        return startHour < endHour;
    }

    // Method to schedule notifications
    private void scheduleNotifications(String title, String startTime, String endTime, String remindEvery) {
        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int startMinute = Integer.parseInt(startTime.split(":")[1]);

        int remindInterval = Integer.parseInt(remindEvery.split(" ")[0]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, startHour);
        calendar.set(Calendar.MINUTE, startMinute);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long intervalMillis = remindInterval * 60 * 1000;  // Convert minutes to milliseconds
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pendingIntent);
    }

    // Method to update a reminder
    private void updateReminder(CardView reminderCard, Reminder reminder, String title, String startTime, String endTime, String remindEvery) {
        reminder.setTitle(title);
        reminder.setStartTime(startTime);
        reminder.setEndTime(endTime);
        reminder.setRemindEvery(remindEvery);

        reminderViewModel.update(reminder);

        // Update the UI card with the new values
        TextView reminderTitleView = reminderCard.findViewById(R.id.reminderTitle);
        TextView startTimeLabel = reminderCard.findViewById(R.id.startTimeLabel);
        TextView endTimeLabel = reminderCard.findViewById(R.id.endTimeLabel);
        TextView remindEveryLabel = reminderCard.findViewById(R.id.remindEveryLabel);

        if (reminderTitleView != null && startTimeLabel != null && endTimeLabel != null && remindEveryLabel != null) {
            reminderTitleView.setText(title);
            startTimeLabel.setText("Start Time: " + startTime);
            endTimeLabel.setText("End Time: " + endTime);
            remindEveryLabel.setText("Remind Every: " + remindEvery);
        }

        // Reschedule notifications after updating
        scheduleNotifications(title, startTime, endTime, remindEvery);
    }

    // Method to delete a reminder
    private void deleteReminder(Reminder reminder, CardView reminderCard) {
        reminderViewModel.delete(reminder);
        reminderContainer.removeView(reminderCard);

        // Optionally, cancel scheduled notifications for this reminder
    }
}

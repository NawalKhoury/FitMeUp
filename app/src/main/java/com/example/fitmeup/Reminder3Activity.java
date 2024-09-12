package com.example.fitmeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
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
    public static final String CHANNEL_ID = "reminder_channel";

    private LinearLayout reminderContainer;
    private ReminderViewModel reminderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder3);

        reminderContainer = findViewById(R.id.reminderContainer);
        FloatingActionButton addReminderButton = findViewById(R.id.addReminderButton);

        // Initialize ViewModel
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        // Show dialog for adding a new reminder
        addReminderButton.setOnClickListener(v -> showAddReminderDialog(null));

        // Load existing reminders from ViewModel and add them to the UI
        reminderViewModel.getAllReminders().observe(this, reminders -> {
            if (reminders != null) {
                reminderContainer.removeAllViews();
                for (Reminder reminder : reminders) {
                    CardView reminderCard = createReminderCard(reminder);
                    reminderContainer.addView(reminderCard);
                }
            }
        });
    }

    private void showAddReminderDialog(CardView reminderCard) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_reminder, null);

        EditText reminderTitleEdit = dialogView.findViewById(R.id.reminderTitleEdit);
        Spinner chooseTimeSpinner = dialogView.findViewById(R.id.chooseTimeSpinner);
        Spinner remindEverySpinner = dialogView.findViewById(R.id.remindEverySpinner);
        Switch reminderSwitch = dialogView.findViewById(R.id.reminderSwitch);

        List<String> timeIntervals = generateTimeIntervals();
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeIntervals);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseTimeSpinner.setAdapter(timeAdapter);

        ArrayAdapter<CharSequence> remindEveryAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_options, android.R.layout.simple_spinner_item);
        remindEveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remindEverySpinner.setAdapter(remindEveryAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle(reminderCard == null ? "Add Your Reminder" : "Edit Your Reminder")
                .setPositiveButton("OK", (dialog, which) -> {
                    String newTitle = reminderTitleEdit.getText().toString().trim();
                    String chooseTime = chooseTimeSpinner.getSelectedItem().toString();
                    String remindEvery = remindEverySpinner.getSelectedItem().toString();
                    boolean isNotificationEnabled = reminderSwitch.isChecked();

                    if (newTitle.isEmpty()) {
                        Toast.makeText(Reminder3Activity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        if (reminderCard == null) {
                            addNewReminder(newTitle, chooseTime, remindEvery, isNotificationEnabled);
                        } else {
                            Reminder reminder = (Reminder) reminderCard.getTag();
                            updateReminder(reminder, newTitle, chooseTime, remindEvery, isNotificationEnabled);
                        }
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private List<String> generateTimeIntervals() {
        List<String> timeIntervals = new ArrayList<>();
        for (int hour = 8; hour <= 23; hour++) {
            String hourString = (hour < 10 ? "0" : "") + hour;
            timeIntervals.add(hourString + ":00");
            timeIntervals.add(hourString + ":30");
        }
        return timeIntervals;
    }

    private void addNewReminder(String title, String chooseTime, String remindEvery, boolean isNotificationEnabled) {
        Reminder reminder = new Reminder(title, chooseTime, remindEvery, isNotificationEnabled);
        reminderViewModel.insert(reminder);
        CardView newCard = createReminderCard(reminder);
        reminderContainer.addView(newCard);

        if (isNotificationEnabled) {
            scheduleNotifications(title, chooseTime, remindEvery);
        }
    }

    private CardView createReminderCard(Reminder reminder) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardViewLayout = inflater.inflate(R.layout.reminder_item, null);

        TextView reminderTitleView = cardViewLayout.findViewById(R.id.reminderTitleEdit);
        Spinner chooseTimeSpinner = cardViewLayout.findViewById(R.id.chooseTimeSpinner);
        Spinner remindEverySpinner = cardViewLayout.findViewById(R.id.remindEverySpinner);
        Switch reminderSwitch = cardViewLayout.findViewById(R.id.reminderSwitch);
        ImageView editIcon = cardViewLayout.findViewById(R.id.editReminder);
        ImageView deleteIcon = cardViewLayout.findViewById(R.id.deleteReminder);

        reminderTitleView.setText(reminder.getTitle());

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generateTimeIntervals());
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseTimeSpinner.setAdapter(timeAdapter);

        ArrayAdapter<CharSequence> remindEveryAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_options, android.R.layout.simple_spinner_item);
        remindEveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remindEverySpinner.setAdapter(remindEveryAdapter);

        setSpinnerSelection(chooseTimeSpinner, reminder.getChooseTime());
        setSpinnerSelection(remindEverySpinner, reminder.getRemindEvery());

        reminderSwitch.setChecked(reminder.isNotificationEnabled());

        reminderSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            reminder.setNotificationEnabled(isChecked);
            reminderViewModel.update(reminder);
            if (isChecked) {
                scheduleNotifications(reminder.getTitle(), reminder.getChooseTime(), reminder.getRemindEvery());
            }
        });

        editIcon.setOnClickListener(v -> showAddReminderDialog((CardView) cardViewLayout.getParent()));
        deleteIcon.setOnClickListener(v -> deleteReminder(reminder, (CardView) cardViewLayout.getParent()));

        CardView card = new CardView(this);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        card.setCardElevation(4);
        card.setRadius(12);
        card.setPadding(16, 16, 16, 16);
        card.addView(cardViewLayout);
        card.setTag(reminder);

        return card;
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void updateReminder(Reminder reminder, String title, String chooseTime, String remindEvery, boolean isNotificationEnabled) {
        reminder.setTitle(title);
        reminder.setChooseTime(chooseTime);
        reminder.setRemindEvery(remindEvery);
        reminder.setNotificationEnabled(isNotificationEnabled);

        reminderViewModel.update(reminder);

        if (isNotificationEnabled) {
            scheduleNotifications(title, chooseTime, remindEvery);
        }
    }

    private void deleteReminder(Reminder reminder, CardView reminderCard) {
        reminderViewModel.delete(reminder);
        reminderContainer.removeView(reminderCard);
    }

    private void scheduleNotifications(String title, String chooseTime, String remindEvery) {
        int hour = Integer.parseInt(chooseTime.split(":")[0]);
        int minute = Integer.parseInt(chooseTime.split(":")[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long intervalMillis = mapRemindEveryToMillis(remindEvery);
        if (intervalMillis != -1) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pendingIntent);
        }
    }

    private long mapRemindEveryToMillis(String remindEvery) {
        switch (remindEvery) {
            case "Day":
                return AlarmManager.INTERVAL_DAY;
            case "Week":
                return AlarmManager.INTERVAL_DAY * 7;
            case "Month":
                return AlarmManager.INTERVAL_DAY * 30;
            case "3 Months":
                return AlarmManager.INTERVAL_DAY * 90;
            case "6 Months":
                return AlarmManager.INTERVAL_DAY * 180;
            case "Year":
                return AlarmManager.INTERVAL_DAY * 365;
            default:
                return -1;
        }
    }
}

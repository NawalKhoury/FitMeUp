package com.example.fitmeup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    private ImageButton handshakeButton;
    private ImageButton home;
    private ImageButton workout;
    private ImageButton profile;
    private ImageButton training;
    private ImageButton reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        handshakeButton = findViewById(R.id.toolbar_handshake);
        home = findViewById(R.id.toolbar_home);
        workout = findViewById(R.id.toolbar_target);
        profile = findViewById(R.id.toolbar_profile);
        training = findViewById(R.id.toolbar_exercise);
        reminder = findViewById(R.id.reminderButton);

        handshakeButton.setOnClickListener(v -> startActivity(new Intent(HomePage.this, community_activity.class)));
        home.setOnClickListener(v -> startActivity(new Intent(HomePage.this, HomePage.class)));
        profile.setOnClickListener(v -> startActivity(new Intent(HomePage.this, ProfilePageActivity.class)));
        reminder.setOnClickListener(v -> startActivity(new Intent(HomePage.this, ReminderPage.class)));
        training.setOnClickListener(v -> startActivity(new Intent(HomePage.this, WorkoutActivity.class)));

        // Fetch the last workout type and time from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("WorkoutPrefs", MODE_PRIVATE);
        String lastWorkoutType = sharedPref.getString("LAST_WORKOUT_TYPE", "No workout recorded");
        String lastWorkoutTime = sharedPref.getString("LAST_WORKOUT_TIME", "00:00:00");

        // Find and set the text in the record workout section
        TextView recordWorkoutText = findViewById(R.id.textView2); // Assuming this is the "Record Workout" section
        recordWorkoutText.setText(String.format("Last Workout: %s\nTime: %s", lastWorkoutType, lastWorkoutTime));
    }
}

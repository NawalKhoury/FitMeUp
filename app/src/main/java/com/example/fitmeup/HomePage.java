package com.example.fitmeup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

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

//test
        handshakeButton = findViewById(R.id.toolbar_handshake);
        home = findViewById(R.id.toolbar_home);
        workout = findViewById(R.id.toolbar_target);
        profile = findViewById(R.id.toolbar_profile);
        training = findViewById(R.id.toolbar_exercise);
        reminder = findViewById(R.id.reminderButton);

        
        reminder.setOnClickListener(v -> startActivity(new Intent(this, ReminderPage.class)));

        handshakeButton.setOnClickListener(v -> startActivity(new Intent(HomePage.this, community_activity.class)));
        training.setOnClickListener(v -> startActivity(new Intent(HomePage.this, RegisterActivity.Timer_activity.class)));
        profile.setOnClickListener(v -> startActivity(new Intent(HomePage.this, ProfilePageActivity.class)));
        reminder.setOnClickListener(v -> startActivity(new Intent(this, ReminderPage.class)));

        training.setOnClickListener(v -> startActivity(new Intent(HomePage.this, WorkoutActivity.class)));

    }
}

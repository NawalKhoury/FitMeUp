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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        handshakeButton = findViewById(R.id.toolbar_handshake);
        home = findViewById(R.id.toolbar_home);
        workout = findViewById(R.id.toolbar_target);
        profile = findViewById(R.id.toolbar_profile);
        training = findViewById(R.id.toolbar_exercise);

        handshakeButton.setOnClickListener(v -> startActivity(new Intent(HomePage.this, community_activity.class)));
        training.setOnClickListener(v -> startActivity(new Intent(HomePage.this, Timer_activity.class)));
        home.setOnClickListener(v -> startActivity(new Intent(HomePage.this, HomePage.class)));
    }
}

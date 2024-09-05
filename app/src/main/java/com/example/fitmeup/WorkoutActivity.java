package com.example.fitmeup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class WorkoutActivity extends AppCompatActivity {

    private ImageButton homeScreenButton;
    private ImageButton profileButton;
    private ImageButton targetButton;
    private ImageButton exerciseButton;
    private ImageButton handshakeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // Initialize MaterialCardViews
        MaterialCardView cardRunning = findViewById(R.id.card_running);
        MaterialCardView cardCoreTraining = findViewById(R.id.card_core_training);
        MaterialCardView cardPoolSwim = findViewById(R.id.card_pool_swim);
        MaterialCardView cardMartialArts = findViewById(R.id.card_martial_arts);
        MaterialCardView cardYoga = findViewById(R.id.card_yoga);
        MaterialCardView cardCycling = findViewById(R.id.card_cycling);

        // Initialize toolbar buttons
        homeScreenButton = findViewById(R.id.toolbar_home);
        profileButton = findViewById(R.id.toolbar_profile);
        targetButton = findViewById(R.id.toolbar_target);
        exerciseButton = findViewById(R.id.toolbar_exercise);
        handshakeButton = findViewById(R.id.toolbar_handshake);

        // Set click listeners for toolbar buttons
        handshakeButton.setOnClickListener(v -> startActivity(new Intent(WorkoutActivity.this, community_activity.class)));
        homeScreenButton.setOnClickListener(v -> startActivity(new Intent(WorkoutActivity.this, HomePage.class)));


        // Set click listeners for each workout
        cardRunning.setOnClickListener(v -> navigateToWorkoutDetail("Running"));
        cardCoreTraining.setOnClickListener(v -> navigateToWorkoutDetail("Core Training"));
        cardPoolSwim.setOnClickListener(v -> navigateToWorkoutDetail("Pool Swim"));
        cardMartialArts.setOnClickListener(v -> navigateToWorkoutDetail("Martial Arts"));
        cardYoga.setOnClickListener(v -> navigateToWorkoutDetail("Yoga"));
        cardCycling.setOnClickListener(v -> navigateToWorkoutDetail("Cycling"));
    }

    // Helper method to handle workout selection
    private void navigateToWorkoutDetail(String workoutType) {
        // For now, display a Toast message to show selected workout
        Toast.makeText(WorkoutActivity.this, "Selected Workout: " + workoutType, Toast.LENGTH_SHORT).show();

        // Uncomment this code if you want to navigate to a new activity in the future
        Intent intent = new Intent(WorkoutActivity.this, Timer_activity.class);
        intent.putExtra("WORKOUT_TYPE", workoutType);
        startActivity(intent);
    }
}

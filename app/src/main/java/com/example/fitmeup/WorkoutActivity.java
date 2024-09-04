package com.example.fitmeup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.fitmeup.R;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // Initialize CardViews
        CardView cardRunning = findViewById(R.id.card_running);
        CardView cardCoreTraining = findViewById(R.id.card_core_training);
        CardView cardPoolSwim = findViewById(R.id.card_pool_swim);
        CardView cardMartialArts = findViewById(R.id.card_martial_arts);
        CardView cardYoga = findViewById(R.id.card_yoga);
        CardView cardCycling = findViewById(R.id.card_cycling);

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
        /*
        Intent intent = new Intent(WorkoutActivity.this, WorkoutDetailActivity.class);
        intent.putExtra("WORKOUT_TYPE", workoutType);
        startActivity(intent);
        */
    }
}

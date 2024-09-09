package com.example.fitmeup;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WorkoutHistory extends AppCompatActivity {

    private LinearLayout workoutListContainer;
    private DailyWorkoutDao dailyWorkoutDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_history);

        // Initialize the container for workouts
        workoutListContainer = findViewById(R.id.workout_list_container);

        // Initialize Room database and DAO
        RegisterUserDatabase db = RegisterUserDatabase.getInstance(getApplicationContext());
        dailyWorkoutDao = db.dailyWorkoutDao();

        // Load the workout history from the database using background thread
        loadWorkoutHistory();
    }

    private void loadWorkoutHistory() {
        // Create a background executor
        Executor executor = Executors.newSingleThreadExecutor();

        // Execute the database query in a background thread
        executor.execute(() -> {
            // Fetch workouts from Room (background thread)
            List<DailyWorkout> workoutList = dailyWorkoutDao.getAllWorkouts();

            // Switch back to the main thread to update the UI
            runOnUiThread(() -> {
                if (workoutList.isEmpty()) {
                    TextView noWorkoutsTextView = new TextView(this);
                    noWorkoutsTextView.setText("No workout history available.");
                    workoutListContainer.addView(noWorkoutsTextView);
                    return;
                }

                // Display each workout
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

                for (DailyWorkout workout : workoutList) {
                    // Create a TextView for each workout entry
                    TextView workoutTextView = new TextView(this);
                    workoutTextView.setTextSize(18);
                    workoutTextView.setTextColor(getResources().getColor(android.R.color.black));

                    // Format the workout details into a string
                    String workoutDetails = String.format(
                            Locale.getDefault(),
                            "%s - %s - %d calories",
                            workout.workoutType,
                            dateFormat.format(workout.date),
                            workout.caloriesBurned
                    );

                    workoutTextView.setText(workoutDetails);
                    workoutListContainer.addView(workoutTextView); // Add the TextView to the container
                }
            });
        });
    }
}

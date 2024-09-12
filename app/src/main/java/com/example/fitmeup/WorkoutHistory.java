package com.example.fitmeup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

        // Load the workout history from the database using a background thread
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

                // Inflate and display each workout using your provided layout
                LayoutInflater inflater = LayoutInflater.from(this);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

                for (DailyWorkout workout : workoutList) {
                    // Inflate the CardView layout
                    View workoutCard = inflater.inflate(R.layout.workout_card, workoutListContainer, false);

                    // Set workout details in the CardView
                    TextView workoutType = workoutCard.findViewById(R.id.workout_type);
                    workoutType.setText(workout.workoutType);

                    TextView caloriesInfo = workoutCard.findViewById(R.id.calories_info);
                    caloriesInfo.setText(String.format(Locale.getDefault(), "%d Cal", workout.caloriesBurned));

                    TextView workoutDate = workoutCard.findViewById(R.id.workout_type);
                    workoutDate.setText(dateFormat.format(workout.date));

                    // Dynamically set the workout icon
                    ImageView workoutIcon = workoutCard.findViewById(R.id.workout_icon);
                    workoutIcon.setImageResource(getWorkoutIcon(workout.workoutType));

                    // Add the card to the container
                    workoutListContainer.addView(workoutCard);
                }
            });
        });
    }

    // Helper method to map workout type to corresponding icon
    private int getWorkoutIcon(String workoutType) {
        switch (workoutType.toLowerCase()) {
            case "running":
                return R.drawable.run;
            case "core training":
                return R.drawable.core;
            case "pool swim":
                return R.drawable.swim;
            case "martial arts":
                return R.drawable.art;
            case "yoga":
                return R.drawable.yoga;
            case "cycling":
                return R.drawable.bike;
            default:
                return R.drawable.run; // Fallback icon
        }
    }
}

package com.example.fitmeup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WorkoutHistory extends AppCompatActivity {

    private LinearLayout workoutListContainer;
    private WorkoutDao workoutDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_history);

        workoutListContainer = findViewById(R.id.workout_list_container);

        RegisterUserDatabase db = RegisterUserDatabase.getInstance(getApplicationContext());
        workoutDao = db.WorkoutDao();
        loadWorkoutHistory();
        saveWorkoutWithCurrentDate();
    }

    private void loadWorkoutHistory() {
        workoutDao.getAllWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workoutList) {
                workoutListContainer.removeAllViews();

                if (workoutList == null || workoutList.isEmpty()) {
                    TextView noWorkoutsTextView = new TextView(WorkoutHistory.this);
                    noWorkoutsTextView.setText("No workout history available.");
                    workoutListContainer.addView(noWorkoutsTextView);
                    return;
                }

                LayoutInflater inflater = LayoutInflater.from(WorkoutHistory.this);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

                for (Workout workout : workoutList) {
                    View workoutCard = inflater.inflate(R.layout.workout_card, workoutListContainer, false);

                    TextView workoutType = workoutCard.findViewById(R.id.workout_type);
                    workoutType.setText(workout.getWorkoutType());

                    TextView caloriesInfo = workoutCard.findViewById(R.id.calories_info);
                    caloriesInfo.setText(String.format(Locale.getDefault(), "%d Cal", workout.getCaloriesBurned()));

                    ImageView workoutIcon = workoutCard.findViewById(R.id.workout_icon);
                    workoutIcon.setImageResource(getWorkoutIcon(workout.getIcon()));

                    workoutListContainer.addView(workoutCard);
                }
            }
        });
    }

    private void saveWorkoutWithCurrentDate() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Workout workout = new Workout("Running", new Date(), 300, "running_icon");
            workoutDao.insert(workout);
        });
    }

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
                return R.drawable.run;  // Fallback icon
        }
    }
}

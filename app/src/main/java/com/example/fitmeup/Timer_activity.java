package com.example.fitmeup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Timer_activity extends AppCompatActivity {

    private ImageButton handshakeButton;
    private ImageButton home;
    private ImageButton workout;
    private ImageButton profile;
    private ImageButton training;

    private Handler handler = new Handler();
    private int seconds = 0;
    private boolean isRunning = false;
    private ImageView startPauseButton;
    private TextView hoursTextView, minutesTextView, secondsTextView;
    private LinearLayout indicatorLayout;
    private ViewPager2 viewPager;
    private int pageCount = 2;
    private TextView workOutType;
    private ImageView backButton;
    private ImageView saveButton;
    private ImageView workoutImage;

    private WorkoutDao workoutDao;  // Correct DAO reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // Initialize Room database and WorkoutDao
        RegisterUserDatabase db = RegisterUserDatabase.getInstance(getApplicationContext());
        workoutDao = db.WorkoutDao();  // Get WorkoutDao instance

        // Initialize toolbar buttons
        handshakeButton = findViewById(R.id.toolbar_handshake);
        home = findViewById(R.id.toolbar_home);
        workout = findViewById(R.id.toolbar_target);
        profile = findViewById(R.id.toolbar_profile);
        training = findViewById(R.id.toolbar_exercise);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.save_button);
        workoutImage = findViewById(R.id.rfacddfkk3vn);

        // Set toolbar button click listeners
        handshakeButton.setOnClickListener(v -> startActivity(new Intent(Timer_activity.this, community_activity.class)));
        home.setOnClickListener(v -> startActivity(new Intent(Timer_activity.this, HomePage.class)));
        backButton.setOnClickListener(v -> startActivity(new Intent(this, WorkoutActivity.class)));

        viewPager = findViewById(R.id.viewPager);
        indicatorLayout = findViewById(R.id.indicatorLayout);

        int[] layouts = {R.layout.layout_time1, R.layout.layout_time2};
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, layouts);
        viewPager.setAdapter(adapter);

        createIndicators(pageCount);

        // Safely initialize workOutTypeText
        String workOutTypeText = null;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                workOutTypeText = extras.getString("WORKOUT_TYPE");
            }
        } else {
            workOutTypeText = (String) savedInstanceState.getSerializable("WORKOUT_TYPE");
        }

        // Set a default value if workOutTypeText is null
        if (workOutTypeText == null) {
            workOutTypeText = "Default Workout";  // Provide a default workout type to prevent null
        }

        workOutType = findViewById(R.id.workOutType);
        workOutType.setText(workOutTypeText);

        // Safely pass workOutTypeText to setWorkoutImage
        setWorkoutImage(workOutTypeText, workoutImage);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicators(position);

                RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);

                if (viewHolder != null) {
                    View pageView = viewHolder.itemView;

                    NumberPicker numberPicker1 = pageView.findViewById(R.id.numberPicker1);
                    NumberPicker numberPicker2 = pageView.findViewById(R.id.numberPicker2);

                    if (numberPicker1 != null && numberPicker2 != null) {
                        numberPicker1.setMinValue(0);
                        numberPicker1.setMaxValue(10);
                        numberPicker1.setValue(0);

                        numberPicker2.setMinValue(0);
                        numberPicker2.setMaxValue(59);
                        numberPicker2.setFormatter(value -> String.format("%02d", value));
                        numberPicker2.setValue(0);
                    }

                    startPauseButton = pageView.findViewById(R.id.start_pause_button);
                    hoursTextView = pageView.findViewById(R.id.hours);
                    minutesTextView = pageView.findViewById(R.id.minutes);
                    secondsTextView = pageView.findViewById(R.id.seconds);

                    if (startPauseButton != null) {
                        startPauseButton.setOnClickListener(v -> toggleTimer());
                    }

                    if (isRunning) {
                        updateTime();
                    }
                }
            }
        });

        saveButton.setOnClickListener(v -> {
            String workoutType = workOutType.getText().toString();
            saveWorkoutToPreferences(workoutType);
            saveWorkoutToDatabase(workoutType);
            Intent intent = new Intent(Timer_activity.this, HomePage.class);
            startActivity(intent);
        });

        if (isRunning) {
            handler.postDelayed(runnable, 1000);
        }
    }

    private void setWorkoutImage(String workoutType, ImageView workoutImage) {
        int imageResId;

        switch (workoutType) {
            case "Running":
                imageResId = R.drawable.runnn;
                break;
            case "Core Training":
                imageResId = R.drawable.imageworkout;
                break;
            case "Pool Swim":
                imageResId = R.drawable.swimingg;
                break;
            case "Martial Arts":
                imageResId = R.drawable.artmat;
                break;
            case "Yoga":
                imageResId = R.drawable.yogaaa;
                break;
            case "Cycling":
                imageResId = R.drawable.cyc;
                break;
            default:
                imageResId = R.drawable.imageworkout;
                break;
        }

        workoutImage.setImageResource(imageResId);
    }

    private void toggleTimer() {
        if (isRunning) {
            stopTimerSafely();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        isRunning = true;
        if (startPauseButton != null) {
            startPauseButton.setImageResource(R.drawable.play);
        }
        handler.postDelayed(runnable, 1000);
    }

    private void stopTimerSafely() {
        isRunning = false;
        if (startPauseButton != null) {
            startPauseButton.setImageResource(R.drawable.pause);
        }
        handler.removeCallbacks(runnable);
    }

    private void saveWorkoutToPreferences(String workoutType) {
        int currentPage = viewPager.getCurrentItem();
        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(currentPage);

        if (viewHolder != null) {
            View pageView = viewHolder.itemView;
            NumberPicker numberPicker1 = pageView.findViewById(R.id.numberPicker1);
            NumberPicker numberPicker2 = pageView.findViewById(R.id.numberPicker2);

            int selectedHours = numberPicker1 != null ? numberPicker1.getValue() : 0;
            int selectedMinutes = numberPicker2 != null ? numberPicker2.getValue() : 0;

            int selectedTotalSeconds = (selectedHours * 3600) + (selectedMinutes * 60);
            int totalWorkoutSeconds = seconds + selectedTotalSeconds;

            String totalTime = TimeFormatUtil.formatTime(totalWorkoutSeconds);

            // Calculate calories burned during the workout
            int caloriesBurned = calculateCalories(workoutType, totalWorkoutSeconds);

            SharedPreferences sharedPref = getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("LAST_WORKOUT_TYPE", workoutType);
            editor.putString("LAST_WORKOUT_TIME", totalTime);

            // Load the previously saved calories burned and add the new calories
            int previousCalories = sharedPref.getInt("caloriesBurned", 0);
            int updatedCalories = previousCalories + caloriesBurned;
            // Save the updated total calories burned
            editor.putInt("caloriesBurned", updatedCalories);

            editor.apply();

            Toast.makeText(this, "Workout and calories saved successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveWorkoutToDatabase(String workoutType) {
        // Create a background executor
        Executor executor = Executors.newSingleThreadExecutor();


        // Fetch userId from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = Integer.parseInt(sharedPref.getString("userId", null));  // Fetch userId from SharedPreferences



        // Execute the database insertion on a background thread
        executor.execute(() -> {
            int currentPage = viewPager.getCurrentItem();
            RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(currentPage);

            if (viewHolder != null) {
                View pageView = viewHolder.itemView;

                NumberPicker numberPicker1 = pageView.findViewById(R.id.numberPicker1);
                NumberPicker numberPicker2 = pageView.findViewById(R.id.numberPicker2);

                int selectedHours = numberPicker1 != null ? numberPicker1.getValue() : 0;
                int selectedMinutes = numberPicker2 != null ? numberPicker2.getValue() : 0;

                int selectedTotalSeconds = (selectedHours * 3600) + (selectedMinutes * 60);
                int totalWorkoutSeconds = seconds + selectedTotalSeconds;

                // Create a new Workout object
                Workout workout = new Workout(workoutType, new Date(), calculateCalories(workoutType, totalWorkoutSeconds), "some_icon_name",totalWorkoutSeconds, userId);

                // Insert the workout into the Room database using the correct DAO instance
                workoutDao.insert(workout);

                // Use runOnUiThread to show a Toast on the main thread
                runOnUiThread(() -> Toast.makeText(Timer_activity.this, "Workout saved to database!", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private int calculateCalories(String workoutType, int totalSeconds) {
        int calories = 0;
        switch (workoutType) {
            case "Running":
                calories = totalSeconds / 10;
                break;
            case "Core Training":
                calories = totalSeconds / 12;
                break;
            case "Pool Swim":
                calories = totalSeconds / 8;
                break;
            case "Martial Arts":
                calories = totalSeconds / 9;
                break;
            case "Yoga":
                calories = totalSeconds / 15;
                break;
            case "Cycling":
                calories = totalSeconds / 11;
                break;
            default:
                calories = totalSeconds / 13;
                break;
        }
        return calories;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seconds++;
            updateTime();
            if (isRunning) {
                handler.postDelayed(this, 1000);
            }
        }
    };
    private void updateTime() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        if (hoursTextView != null) {
            hoursTextView.setText(String.format("%02d", hours));
        }
        if (minutesTextView != null) {
            minutesTextView.setText(String.format("%02d", minutes));
        }
        if (secondsTextView != null) {
            secondsTextView.setText(String.format("%02d", secs));
        }
    }

    private void createIndicators(int count) {
        for (int i = 0; i < count; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.indicator_size),
                    getResources().getDimensionPixelSize(R.dimen.indicator_size));
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.indicator_inactive);
            indicatorLayout.addView(dot);
        }
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicatorLayout.getChildCount(); i++) {
            View dot = indicatorLayout.getChildAt(i);
            if (i == position) {
                dot.setBackgroundResource(R.drawable.indicator_active);
            } else {
                dot.setBackgroundResource(R.drawable.indicator_inactive);
            }
        }
    }
}

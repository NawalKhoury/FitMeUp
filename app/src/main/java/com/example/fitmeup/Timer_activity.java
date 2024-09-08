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
    private int pageCount = 2; // Set this to the number of pages you have
    private TextView workOutType;
    private ImageView backButton;
    private ImageView saveButton; // Change to ImageView for save_button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // Initialize toolbar buttons
        handshakeButton = findViewById(R.id.toolbar_handshake);
        home = findViewById(R.id.toolbar_home);
        workout = findViewById(R.id.toolbar_target);
        profile = findViewById(R.id.toolbar_profile);
        training = findViewById(R.id.toolbar_exercise);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.save_button); // Now it's an ImageView

        // Set toolbar button click listeners
        handshakeButton.setOnClickListener(v -> startActivity(new Intent(Timer_activity.this, community_activity.class)));
        home.setOnClickListener(v -> startActivity(new Intent(Timer_activity.this, HomePage.class)));
        backButton.setOnClickListener(v -> startActivity(new Intent(this, WorkoutActivity.class)));

        viewPager = findViewById(R.id.viewPager);
        indicatorLayout = findViewById(R.id.indicatorLayout);

        // Setup ViewPager2 and its adapter
        int[] layouts = {R.layout.layout_time1, R.layout.layout_time2};
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, layouts);
        viewPager.setAdapter(adapter);

        // Create indicator dots
        createIndicators(pageCount);

        String workOutTypeText;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                workOutTypeText = null;
            } else {
                workOutTypeText = extras.getString("WORKOUT_TYPE");
            }
        } else {
            workOutTypeText = (String) savedInstanceState.getSerializable("WORKOUT_TYPE");
        }
        workOutType = findViewById(R.id.workOutType);
        workOutType.setText(workOutTypeText);

        // Register a callback for when the page changes
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicators(position);

                // Accessing Views in the currently displayed page
                RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);

                if (viewHolder != null) {
                    View pageView = viewHolder.itemView;

                    // Set up NumberPickers
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

                    // Set up timer related views
                    startPauseButton = pageView.findViewById(R.id.start_pause_button);
                    hoursTextView = pageView.findViewById(R.id.hours);
                    minutesTextView = pageView.findViewById(R.id.minutes);
                    secondsTextView = pageView.findViewById(R.id.seconds);

                    if (startPauseButton != null) {
                        startPauseButton.setOnClickListener(v -> toggleTimer());
                    }

                    // Update the UI if the timer is running
                    if (isRunning && hoursTextView != null && minutesTextView != null && secondsTextView != null) {
                        updateTime();  // Immediately update the UI with the current timer value
                    }
                }
            }
        });

        // Save button click event
        saveButton.setOnClickListener(v -> {
            // Get the workout type and time
            String workoutType = workOutType.getText().toString();

            // Call the save method
            saveWorkoutToPreferences(workoutType);

            // After saving, navigate to the HomePage and pass the total time
            Intent intent = new Intent(Timer_activity.this, HomePage.class);
            startActivity(intent);
        });

        // Continue running the timer if it was already started before
        if (isRunning) {
            handler.postDelayed(runnable, 1000);
        }
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
            startPauseButton.setImageResource(R.drawable.play); // Change to play
        }
        handler.postDelayed(runnable, 1000);
    }

    private void stopTimerSafely() {
        isRunning = false;
        if (startPauseButton != null) {
            startPauseButton.setImageResource(R.drawable.pause); // Change to pause
        }
        handler.removeCallbacks(runnable);
    }

    private void saveWorkoutToPreferences(String workoutType) {
        // Get the current NumberPicker values from the displayed page
        int currentPage = viewPager.getCurrentItem();
        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(currentPage);

        if (viewHolder != null) {
            View pageView = viewHolder.itemView;

            // Retrieve the values from the NumberPickers
            NumberPicker numberPicker1 = pageView.findViewById(R.id.numberPicker1);
            NumberPicker numberPicker2 = pageView.findViewById(R.id.numberPicker2);

            int selectedHours = numberPicker1 != null ? numberPicker1.getValue() : 0;
            int selectedMinutes = numberPicker2 != null ? numberPicker2.getValue() : 0;

            // Convert the selected time to seconds
            int selectedTotalSeconds = (selectedHours * 3600) + (selectedMinutes * 60);

            // Add the timer seconds to the selected time
            int totalWorkoutSeconds = seconds + selectedTotalSeconds;

            // Calculate the total time in hours, minutes, and seconds
            int totalHours = totalWorkoutSeconds / 3600;
            int totalMinutes = (totalWorkoutSeconds % 3600) / 60;
            int totalSeconds = totalWorkoutSeconds % 60;

            String totalTime = String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds);

            // Save the workout type and total time in SharedPreferences
            SharedPreferences sharedPref = getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("LAST_WORKOUT_TYPE", workoutType);
            editor.putString("LAST_WORKOUT_TIME", totalTime);
            editor.apply();

            // Show a success message using Toast
            Toast.makeText(this, "Workout saved successfully!", Toast.LENGTH_SHORT).show();
        }
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

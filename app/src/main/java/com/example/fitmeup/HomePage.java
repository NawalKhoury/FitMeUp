package com.example.fitmeup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.widget.ProgressBar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomePage extends AppCompatActivity implements SensorEventListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final float STEP_LENGTH_IN_METERS = 0.762f; // Average step length

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    private TextView stepCountTextView;
    private TextView distanceTextView;
    private TextView dateTextView;
    private TextView dateYearTextView;

    private int stepCount = 0;


    private ImageButton handshakeButton;
    private ImageButton home;
    private ImageButton targetButton;
    private ImageButton profile;
    private ImageButton training;
    private ImageButton reminder;
    private ImageView historyIcon;
    private TextView date_year;
    private ProgressBar progressBar;
    private ImageButton increaseWater;
    private ImageButton dicreaseWater;
    private  TextView waterText;
    private  TextView Name;
    int waterCount = 0;
    private WorkoutDao workoutDao;
    private int userId;
    private ImageButton deleteWorkoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Fetch userId from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = Integer.parseInt(sharedPref.getString("userId", "0"));

        // Initialize WorkoutDao
        RegisterUserDatabase db = RegisterUserDatabase.getInstance(getApplicationContext());
        workoutDao = db.WorkoutDao();

        // Initialize UI components
        handshakeButton = findViewById(R.id.toolbar_handshake);
        home = findViewById(R.id.toolbar_home);
        targetButton = findViewById(R.id.toolbar_target);
        profile = findViewById(R.id.toolbar_profile);
        training = findViewById(R.id.toolbar_exercise);
        reminder = findViewById(R.id.reminderButton);
        historyIcon = findViewById(R.id.historyic);
        stepCountTextView = findViewById(R.id.steps_counter);
        distanceTextView = findViewById(R.id.rot6kqp9h3a9); // Ensure this matches your layout ID
        dateTextView = findViewById(R.id.Date);
        date_year = findViewById(R.id.dateText);
         progressBar = findViewById(R.id.circularProgressBar);
        increaseWater=findViewById(R.id.waterImageRight);
        dicreaseWater=findViewById(R.id.waterImageLeft);
        waterText=findViewById(R.id.waterText);
        Name=findViewById(R.id.Name);
        deleteWorkoutButton = findViewById(R.id.delete_workout_button);


        String username = sharedPref.getString("username", null);

        if (username != null) {
            Name.setText("Hello, " + username);
        }

            waterText.setText(waterCount + " Cups");
            increaseWater.setOnClickListener(v -> {
                // Increment the waterCount by 1
                waterCount++;

                // Update the waterText TextView with the new count
                waterText.setText(waterCount + " Cups");
            });

        dicreaseWater.setOnClickListener(v -> {
            // Increment the waterCount by 1
            if(waterCount>0) {
                waterCount--;
            }
            // Update the waterText TextView with the new count
            waterText.setText(waterCount + " Cups");
        });
        dateYearTextView = findViewById(R.id.dateText);

        // Set up button click listeners
        setUpButtonListeners();

        // Set the current date and year text views
        setCurrentDateAndYear();

        // Initialize step counter sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        // Check and request activity recognition permission if necessary
        checkAndRequestPermissions();

        // Display last workout details from SharedPreferences
        displayLastWorkoutDetails();

        deleteWorkoutButton.setOnClickListener(v -> deleteLastWorkout());
    }
    private void deleteLastWorkout() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Get the number of workouts for the user
            List<Workout> workouts = workoutDao.getAllWorkoutsForUserBlocking(userId); // Use blocking query method to get the list
            if (workouts != null && !workouts.isEmpty()) {
                Workout lastWorkout = workouts.get(0); // Get the most recent workout (first in the ordered list)

                // Delete the workout
                workoutDao.delete(lastWorkout);

                if (workouts.size() == 1) {
                    // This was the only workout, update SharedPreferences to show "No workout recorded"
                    SharedPreferences sharedPref = getSharedPreferences("WorkoutPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("LAST_WORKOUT_TYPE", "No workout recorded");
                    editor.putString("LAST_WORKOUT_TIME", "00:00:00");
                    editor.apply();
                } else {
                    // More workouts remain, update SharedPreferences with the next most recent workout
                    Workout nextWorkout = workouts.get(1); // Get the next workout after deleting the latest one
                    SharedPreferences sharedPref = getSharedPreferences("WorkoutPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("LAST_WORKOUT_TYPE", nextWorkout.getWorkoutType());
                    editor.putString("LAST_WORKOUT_TIME", TimeFormatUtil.formatTime(nextWorkout.getTotalNumberOfSeconds()));
                    editor.apply();
                }

                // Update UI on the main thread
                runOnUiThread(() -> {
                    Toast.makeText(HomePage.this, "Last workout deleted", Toast.LENGTH_SHORT).show();
                    // Reload the workout history to reflect the deletion
                    displayLastWorkoutDetails();
                });
            } else {
                // No workout found to delete
                runOnUiThread(() -> Toast.makeText(HomePage.this, "No workout to delete", Toast.LENGTH_SHORT).show());
            }
        });
    }





    private void setUpButtonListeners() {
        // Set up navigation button listeners
        handshakeButton.setOnClickListener(v -> startActivity(new Intent(HomePage.this, community_activity.class)));
        training.setOnClickListener(v -> startActivity(new Intent(HomePage.this, WorkoutActivity.class)));
        profile.setOnClickListener(v -> startActivity(new Intent(HomePage.this, ProfilePageActivity.class)));
        targetButton.setOnClickListener(v -> startActivity(new Intent(HomePage.this, Model_activity.class)));
        reminder.setOnClickListener(v -> startActivity(new Intent(this, Reminder3Activity.class)));

        // Fetch userId from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
       int userId = Integer.parseInt(sharedPref.getString("userId", null));

        // Fetch the last workout type and time from SharedPreferences
        SharedPreferences workoutPref = getSharedPreferences("WorkoutPrefs", MODE_PRIVATE);
        String lastWorkoutType = sharedPref.getString("LAST_WORKOUT_TYPE", "No workout recorded");
        String lastWorkoutTime = sharedPref.getString("LAST_WORKOUT_TIME", "00:00:00");

        // Find and set the text in the record workout section
        TextView recordWorkoutText = findViewById(R.id.textView2); // Assuming this is the "Record Workout" section
        recordWorkoutText.setText(String.format("Last Workout: %s\nTime: %s", lastWorkoutType, lastWorkoutTime));

        // Start WorkoutHistory and pass userId
        historyIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, WorkoutHistory.class);
            intent.putExtra("userId", userId);  // Pass the userId to WorkoutHistory
            startActivity(intent);
        });
    }

    private void setCurrentDateAndYear() {
        // Set formatted date and year/month in the UI
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("E, d MMM yyyy", Locale.getDefault());
        String currentDate = dateFormat1.format(calendar.getTime());
        dateTextView.setText(currentDate);

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String currentYearMonth = dateFormat2.format(calendar.getTime());
        dateYearTextView.setText(currentYearMonth);
    }

    private void checkAndRequestPermissions() {
        // Check for activity recognition permission, request if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_CODE);
        } else {
            registerStepCounterSensor();
        }
    }

private void displayLastWorkoutDetails() {

    workoutDao.getLastWorkoutForUser(userId).observe(this, workout -> {
        if(workout != null) {
            // Fetch and display the last workout details from SharedPreferences
            SharedPreferences sharedPref = getSharedPreferences("WorkoutPrefs", MODE_PRIVATE);
            String lastWorkoutType = workout.getWorkoutType() != null ? workout.getWorkoutType() : "No workout recorded";
            String lastWorkoutTime = TimeFormatUtil.formatTime(workout.getTotalNumberOfSeconds());

            TextView recordWorkoutText = findViewById(R.id.textView2); // Assuming this is the "Record Workout" section
            recordWorkoutText.setText(String.format("Last Workout: %s\nTime: %s", lastWorkoutType, lastWorkoutTime));
        }
    });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Handle sensor changes for step counter
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            stepCountTextView.setText(stepCount+"/90000");

        String stepText = stepCount + "/90000";
        stepCountTextView.setText(stepText);
        progressBar.setProgress(stepCount);

        // Calculate and display distance in kilometers
        float distanceInMeters = stepCount * STEP_LENGTH_IN_METERS;
        float distanceInKilometers = distanceInMeters / 1000;
        distanceTextView.setText(String.format("%.2f KM", distanceInKilometers));
    }
}


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No need to implement for step counter
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
            registerStepCounterSensor();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterStepCounterSensor();
    }

    private void registerStepCounterSensor() {
        // Register the step counter sensor listener
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void unregisterStepCounterSensor() {
        // Unregister the step counter sensor listener
        if (stepCounterSensor != null) {
            sensorManager.unregisterListener(this, stepCounterSensor);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Handle permission request results
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerStepCounterSensor();
            }
        }
    }
}

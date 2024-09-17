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

import android.os.Handler;
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
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
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
    private DailyWorkoutDao dailyWorkoutDao;
    private int userId;

    private Executor executor = Executors.newSingleThreadExecutor();


    private ProgressBar[] dailyProgressBars = new ProgressBar[7];  // Array to hold 7 progress bars for each day of the week
    private ProgressBar circularProgressBar;
    private static final int MAX_STEPS = 10000;  // Assuming the step goal is 10,000 per day




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
        dailyWorkoutDao = db.dailyWorkoutDao();


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
        displayLastWorkoutDetails();


        dailyProgressBars[0] = findViewById(R.id.progressBar1);
        dailyProgressBars[1] = findViewById(R.id.progressBar2);
        dailyProgressBars[2] = findViewById(R.id.progressBar3);
        dailyProgressBars[3] = findViewById(R.id.progressBar4);
        dailyProgressBars[4] = findViewById(R.id.progressBar5);
        dailyProgressBars[5] = findViewById(R.id.progressBar6);
        dailyProgressBars[6] = findViewById(R.id.progressBar7);

        circularProgressBar = findViewById(R.id.circularProgressBar);

        // Initialize step counter sensor (no redeclaration here)
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        loadWeeklyWorkoutData();

    }


    private void loadWeeklyWorkoutData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date startOfWeek = calendar.getTime();
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date endOfWeek = calendar.getTime();

        // Fetch workouts for the current user
        new Thread(() -> {
            List<DailyWorkout> workouts = dailyWorkoutDao.getWorkoutsForWeek(userId, startOfWeek.getTime(), endOfWeek.getTime());

            if (workouts != null && !workouts.isEmpty()) {
                // Pass the fetched workouts to the UI thread to update the progress bars
                runOnUiThread(() -> updateProgressBars(workouts));
            } else {
                // If no workouts, ensure progress bars are reset or set to zero
                runOnUiThread(() -> resetProgressBars());
            }
        }).start();
    }

    private void resetProgressBars() {
        for (ProgressBar progressBar : dailyProgressBars) {
            progressBar.setProgress(0);
        }
    }




    private void updateProgressBars(List<DailyWorkout> workouts) {
        // Get today's date and day of the week (Sunday = 0, Monday = 1, etc.)
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK) - 1;  // Use 0-based index (0 = Sunday)

        // Reset progress for all days (set progress to 0)
        for (int i = 0; i < dailyProgressBars.length; i++) {
            dailyProgressBars[i].setMax(MAX_STEPS);
            dailyProgressBars[i].setProgress(0);  // Reset all progress bars to 0
        }

        // Loop through the workouts and update the corresponding ProgressBar for past days and today
        for (DailyWorkout workout : workouts) {
            calendar.setTime(workout.getDate());
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;  // Get the day of the week (0 = Sunday)

            if (dayOfWeek <= today) {  // Only update for past days or today
                int steps = workout.getStepsCounter();
                dailyProgressBars[dayOfWeek].setProgress(steps);  // Set the progress for the past days or today
            }
        }

        updateCurrentDayProgress();
    }

    private void updateCurrentDayProgress() {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        dailyProgressBars[today].setMax(MAX_STEPS);
        dailyProgressBars[today].setProgress(stepCount);

        circularProgressBar.setMax(MAX_STEPS);
        circularProgressBar.setProgress(stepCount);
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

        updateCurrentDayProgress();

        // Calculate and display distance in kilometers
        float distanceInMeters = stepCount * STEP_LENGTH_IN_METERS;
        float distanceInKilometers = distanceInMeters / 1000;
        distanceTextView.setText(String.format("%.2f KM", distanceInKilometers));
        executor.execute(() -> {
            dailyWorkoutDao.insert(new DailyWorkout(stepCount, new Date(), stepCount, (int) distanceInKilometers, 0, null, "", userId));
        });
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

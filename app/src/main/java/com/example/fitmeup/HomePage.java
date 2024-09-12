package com.example.fitmeup;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomePage extends AppCompatActivity implements SensorEventListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private TextView stepCountTextView;
    private TextView distanceTextView;
    private int stepCount = 0;

    private static final float STEP_LENGTH_IN_METERS = 0.762f; // Average step length (in meters) - adjust as needed

    private ImageButton handshakeButton;
    private ImageButton home;
    private ImageButton targetButton;
    private ImageButton profile;
    private ImageButton training;
    private ImageButton reminder;
    private TextView dateTextView;
    private TextView date_year;
    private ProgressBar progressBar;
    private ImageButton increaseWater;
    private ImageButton dicreaseWater;
    private  TextView waterText;
    private  TextView Name;
    int waterCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        handshakeButton = findViewById(R.id.toolbar_handshake);
        home = findViewById(R.id.toolbar_home);
        targetButton = findViewById(R.id.toolbar_target);
        profile = findViewById(R.id.toolbar_profile);
        training = findViewById(R.id.toolbar_exercise);
        reminder = findViewById(R.id.reminderButton);
        stepCountTextView = findViewById(R.id.steps_counter);
        distanceTextView = findViewById(R.id.rot6kqp9h3a9); // Ensure this id matches your layout
        dateTextView = findViewById(R.id.Date);
        date_year = findViewById(R.id.dateText);
         progressBar = findViewById(R.id.circularProgressBar);
        increaseWater=findViewById(R.id.waterImageRight);
        dicreaseWater=findViewById(R.id.waterImageLeft);
        waterText=findViewById(R.id.waterText);
        Name=findViewById(R.id.Name);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

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

     

        reminder.setOnClickListener(v -> startActivity(new Intent(this, Reminder3Activity.class)));
        handshakeButton.setOnClickListener(v -> startActivity(new Intent(HomePage.this, community_activity.class)));
        training.setOnClickListener(v -> startActivity(new Intent(HomePage.this, WorkoutActivity.class)));
        profile.setOnClickListener(v -> startActivity(new Intent(HomePage.this, ProfilePageActivity.class)));
        targetButton.setOnClickListener(v -> startActivity(new Intent(this, Model_activity.class)));
      

      
        // Fetch the last workout type and time from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("WorkoutPrefs", MODE_PRIVATE);
        String lastWorkoutType = sharedPref.getString("LAST_WORKOUT_TYPE", "No workout recorded");
        String lastWorkoutTime = sharedPref.getString("LAST_WORKOUT_TIME", "00:00:00");

        // Find and set the text in the record workout section
        TextView recordWorkoutText = findViewById(R.id.textView2); // Assuming this is the "Record Workout" section
        recordWorkoutText.setText(String.format("Last Workout: %s\nTime: %s", lastWorkoutType, lastWorkoutTime));


        // Get the current date
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("E, d MMM yyyy", Locale.getDefault());
        String currentDate = dateFormat1.format(calendar1.getTime());

        // Set the formatted date to the TextView
        dateTextView.setText(currentDate);

        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String currentYearMonth = dateFormat2.format(calendar2.getTime());

        // Set the formatted year and month to the TextView
        date_year.setText(currentYearMonth);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        // Request permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_CODE);
        } else {
            registerStepCounterSensor();
        }
    }

@Override
public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
        stepCount = (int) event.values[0];
        stepCountTextView.setText(stepCount+"/90000");

        String stepText = stepCount + "/90000";
        stepCountTextView.setText(stepText);
        progressBar.setProgress(stepCount);


        // Calculate distance in kilometers
        float distanceInMeters = stepCount * STEP_LENGTH_IN_METERS;
        float distanceInKilometers = distanceInMeters / 1000;
        distanceTextView.setText(String.format("%.2f KM", distanceInKilometers));
    }
}


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
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
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void unregisterStepCounterSensor() {
        if (stepCounterSensor != null) {
            sensorManager.unregisterListener(this, stepCounterSensor);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerStepCounterSensor();
            }
        }
    }
}

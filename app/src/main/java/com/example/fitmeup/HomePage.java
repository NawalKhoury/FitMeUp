package com.example.fitmeup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.concurrent.Executors;
import android.widget.ProgressBar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.Date;

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
    private int waterCount = 0;
    private float predictedStepCountGoal = 0;
    private float predictedWaterIntakeGoalCups;
    private int caloriesBurnedCount =0;

    private TextView stepsCounterView;
    private TextView calorieCounterView;
    private TextView waterText;
    private RegisterUserDao registerUserDao;
    private Executor executor = Executors.newSingleThreadExecutor();  // Executor for background database operations

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
    private ImageButton decreaseWater;
    private TextView Name;
    private  String fitnessGoal;
    private WorkoutDao workoutDao;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initializeUIComponents();

        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = Integer.parseInt(sharedPref.getString("userId", "0"));
        // Initialize DAO to retrieve user data from the database
        RegisterUserDatabase db = RegisterUserDatabase.getInstance(getApplicationContext());
        registerUserDao = db.registerUserDao();
        workoutDao = db.WorkoutDao();
        // Load user details from the database in a background thread
        executor.execute(() -> {
            String userId = getUserId();  // Replace with actual user ID logic
            RegisterUser user = registerUserDao.getUserById(userId);

            if (user != null) {
                runOnUiThread(() -> {
                    // Pass the retrieved user data to the AI model
                    handleAIModelPrediction(user);
                });
            }
        });

        setUpButtonListeners();
        setCurrentDateAndYear();
        checkAndRequestPermissions();
        displayLastWorkoutDetails();
        loadCaloriesBurned();

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
    private void displayLastWorkoutDetails() {
        workoutDao.getLastWorkoutForUser(userId).observe(this, workout -> {
            if (workout != null) {
                // Fetch and display the last workout details
                String lastWorkoutType = workout.getWorkoutType() != null ? workout.getWorkoutType() : "No workout recorded";
                String lastWorkoutTime = TimeFormatUtil.formatTime(workout.getTotalNumberOfSeconds());

                TextView recordWorkoutText = findViewById(R.id.textView2); // Assuming this is the "Record Workout" section
                recordWorkoutText.setText(String.format("Last Workout: %s\nTime: %s", lastWorkoutType, lastWorkoutTime));

                // Update SharedPreferences with the latest workout details
                SharedPreferences sharedPref = getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("LAST_WORKOUT_TYPE", lastWorkoutType);
                editor.putString("LAST_WORKOUT_TIME", lastWorkoutTime);
                editor.apply();
            }
        });
    }



    // Method to handle AI model prediction
    private void handleAIModelPrediction(RegisterUser user) {
        float weight = Float.parseFloat(user.getWeight());  // Convert string to float
        float height = Float.parseFloat(user.getHeight());  // Convert string to float
        int age = calculateAge(user.getBirthDate());  // Calculate age

        // Map the fitness goal from String to numeric value
        String fitnessGoal = user.getFitnessGoal();
        int fitnessGoalNumeric = mapFitnessGoalToNumeric(fitnessGoal);


        // Calculate BMI
        float bmi = weight / ((height / 100) * (height / 100));  // height in cm

        // Prepare input data: bmi, age, weight, height, fitness goal
        float[] inputData = {bmi, age, weight, height, fitnessGoalNumeric};

        // Initialize AIModelHelper and load TensorFlow Lite model
        AIModelHelper aiModelHelper = new AIModelHelper(this);

        // Run inference to get predictions
        float[] predictions = aiModelHelper.predictGoals(inputData);


        // Get the predicted step count goal, calories, and water intake goal
        float predictedStepCountGoal = predictions[0];
        float predictedCalorieGoal = predictions[1];
        float predictedWaterIntakeGoalCups = predictions[2]/10 ;
        // Convert predicted water intake goal from liters to cups
        //float predictedWaterIntakeGoalCups = predictedWaterIntakeGoalLiters / 12; // convert it to cups

        // Apply different scaling depending on the user's fitness goal
        if (fitnessGoalNumeric == -1) {  // Lose weight
            predictedStepCountGoal = predictedStepCountGoal / 30;  // More steps to lose weight
            predictedCalorieGoal = predictedCalorieGoal / 30;      // Lower calorie goal for losing weight
        } else {  // Other goals (e.g., maintain weight or gain weight)
            predictedStepCountGoal = predictedStepCountGoal / 50;  // Default scaling
            predictedCalorieGoal = predictedCalorieGoal / 50;      // Default scaling
        }

// Adjust based on age if user is older
        if (age >= 50) {
            // Reduce goals by 20% for users aged 60 or above
            float ageReductionFactor = 0.8f; // reduce  by 20% for older users
            predictedStepCountGoal *= ageReductionFactor;
            predictedCalorieGoal *= ageReductionFactor;
        }

        // Save the predicted water intake goal to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        savePredictedWaterGoal(sharedPreferences, predictedWaterIntakeGoalCups);  // Save predicted water goal


        // Update the UI with the predicted values
        updateUIWithPredictions(predictedStepCountGoal, predictedCalorieGoal, predictedWaterIntakeGoalCups);

        // Close the AIModelHelper when done
        aiModelHelper.close();
    }

    // Method to retrieve user ID from SharedPreferences
    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", null);  // Replace "userId" with the actual key used to store the user ID
    }

    private void updateUIWithPredictions(float stepGoal, float calorieGoal, float waterGoalCups) {
        // Update steps counter
        stepCountTextView.setText(stepCount + "/" + Math.round(stepGoal));

        // Load total calories burned from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE);
        int totalCaloriesBurned = sharedPreferences.getInt("caloriesBurned", 0);  // Default to 0 if no data

        // Update calorie counter
        calorieCounterView.setText(totalCaloriesBurned + "/" + Math.round(calorieGoal) + " CAL");

        // Update water intake goal (in cups)
        waterText.setText(waterCount + " / " + Math.round(waterGoalCups) + " Cups");
    }

    private void initializeUIComponents() {
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
        increaseWater = findViewById(R.id.waterImageRight);
        decreaseWater = findViewById(R.id.waterImageLeft);
        waterText = findViewById(R.id.waterText);
        Name = findViewById(R.id.Name);
        calorieCounterView = findViewById(R.id.CaloriesTextView);

        // Set username from SharedPreferences if available
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        waterCount = sharedPreferences.getInt("waterCount", 0);  // Default to 0 if not set
        predictedWaterIntakeGoalCups = sharedPreferences.getFloat("predictedWaterGoal", 0);  // Load saved predicted goal


        if (username != null) {
            Name.setText("Hello, " + username);
        }

        waterText.setText(waterCount + "/ " + Math.round(predictedWaterIntakeGoalCups) + " Cups");

        increaseWater.setOnClickListener(v -> {
            waterCount++;
            waterText.setText(waterCount + "/ " + Math.round(predictedWaterIntakeGoalCups) + " Cups");

            saveWaterCount(sharedPreferences, waterCount);
        });

        decreaseWater.setOnClickListener(v -> {
            if (waterCount > 0) {
                waterCount--;
            }
            waterText.setText(waterCount + "/ " + Math.round(predictedWaterIntakeGoalCups) + " Cups");
            saveWaterCount(sharedPreferences, waterCount);
        });
    }


        private void loadCaloriesBurned() {
            SharedPreferences sharedPreferences = getSharedPreferences("WorkoutPrefs", Context.MODE_PRIVATE);

            // Get the last saved date
            String lastWorkoutDate = sharedPreferences.getString("lastWorkoutDate", "");
            String currentDate = String.valueOf(new Date());

            // Check if the current date is different from the last saved date
            if (!currentDate.equals(lastWorkoutDate)) {
                // It's a new day, so reset the calories burned
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("caloriesBurned", 0); // Reset to 0 for a new day
                editor.putString("lastWorkoutDate", currentDate); // Save the current date
                editor.apply();
            }

            // Load the total calories burned
            int totalCaloriesBurned = sharedPreferences.getInt("caloriesBurned", 0);

            // Update the calories burned text view
            calorieCounterView.setText(totalCaloriesBurned + " CAL");
    }


    // Save water count to SharedPreferences in a separate method
    private void saveWaterCount(SharedPreferences sharedPreferences, int waterCount) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("waterCount", waterCount);
        editor.apply();
    }

    // Save the predicted water intake goal (when updated)
    private void savePredictedWaterGoal(SharedPreferences sharedPreferences, float predictedWaterGoal) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("predictedWaterGoal", predictedWaterGoal);  // Save predicted goal
        editor.apply();
    }

    private int calculateAge(String birthDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date birthDateParsed = sdf.parse(birthDate);
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTime(birthDateParsed);
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < birthDay.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;  // Handle error properly
        }
    }

    private int mapFitnessGoalToNumeric(String fitnessGoal) {
        if ("Lose weight".equals(fitnessGoal)) {
            return -1;
        } else if ("Gain muscle mass".equals(fitnessGoal)) {
            return 1;
        } else {
            return 0;  // Maintain weight by default
        }
    }




    private void setCurrentDateAndYear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("E, d MMM yyyy", Locale.getDefault());
        String currentDate = dateFormat1.format(calendar.getTime());
        dateTextView.setText(currentDate);

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String currentYearMonth = dateFormat2.format(calendar.getTime());
        date_year.setText(currentYearMonth);
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_CODE);
        } else {
            registerStepCounterSensor();
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // Handle sensor changes for step counter
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            stepCountTextView.setText(stepCount+"/9000");

            String stepText = stepCount + "/9000";
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
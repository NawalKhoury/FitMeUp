package com.example.fitmeup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfilePageActivity extends AppCompatActivity {

    private TextView nameTextView, ageTextView, weightTextView, heightTextView, bmiTextView, levelTextView, scoreTextView;
    private ImageView settingsIcon, profileImageView;
    private LinearLayout settingsIcon2;
    private RegisterUserDao registerUserDao;
    private RegisterUser registerUser;
    private Executor executor = Executors.newSingleThreadExecutor();
    private ImageButton homeScreenButton;
    private ImageButton profileButton;
    private ImageButton targetButton;
    private ImageButton exerciseButton;
    private ImageButton handshakeButton;
    private ProgressBar circularProgressBarProfile, circularProgressBarLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Initialize toolbar buttons
        homeScreenButton = findViewById(R.id.toolbar_home);
        profileButton = findViewById(R.id.toolbar_profile);
        targetButton = findViewById(R.id.toolbar_target);
        exerciseButton = findViewById(R.id.toolbar_exercise);
        handshakeButton = findViewById(R.id.toolbar_handshake);

        // Set click listeners for toolbar buttons
        handshakeButton.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, community_activity.class)));
        homeScreenButton.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, HomePage.class)));
        exerciseButton.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, WorkoutActivity.class)));
        targetButton.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, Model_activity.class)));

        // Initialize views and CircularProgressBar for BMI and Level
        registerUserDao = RegisterUserDatabase.getInstance(this).registerUserDao();
        initViews();

        // Initialize CircularProgressBars
        circularProgressBarProfile = findViewById(R.id.circularProgressBarProfile);
        circularProgressBarLevel = findViewById(R.id.circularProgressBarLevel);

        // Retrieve and update user profile
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        if (userId != null) {
            executor.execute(() -> {
                registerUser = registerUserDao.getUserById(userId);
                if (registerUser != null) {
                    updateUserProfileInfo(registerUser);
                }
            });
        }

        // Set click listeners for settings icons
        settingsIcon.setOnClickListener(v -> {
            Toast.makeText(ProfilePageActivity.this, "Settings icon clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfilePageActivity.this, SettingPage.class);
            startActivity(intent);
        });

        settingsIcon2.setOnClickListener(v -> {
            Toast.makeText(ProfilePageActivity.this, "Settings icon 2 clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfilePageActivity.this, SettingPage.class);
            startActivity(intent);
        });

        // Retrieve profile image from Intent
        Intent intent = getIntent();
        String imageUriString = intent.getStringExtra("PROFILE_IMAGE_URI");
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri imageUri = Uri.parse(imageUriString);
            Glide.with(this).load(imageUri).into(profileImageView);
        }

        // Initialize level based on app usage
        initUserLevel();
    }

    private void updateUserProfileInfo(RegisterUser registerUser) {
        runOnUiThread(() -> {
            nameTextView.setText(registerUser.getUsername());

            String height = registerUser.getHeight();
            String weight = registerUser.getWeight();
            String birthday = registerUser.getBirthDate();

            if (height != null && weight != null) {
                heightTextView.setText(height);
                weightTextView.setText(weight);

                // Calculate BMI
                try {
                    double heightInMeters = Double.parseDouble(height) / 100;
                    double weightInKg = Double.parseDouble(weight);
                    double bmi = weightInKg / (heightInMeters * heightInMeters);
                    String bmiStatus = getBMIStatus(bmi);

                    // Update BMI and BMI status in TextViews
                    bmiTextView.setText(String.format(Locale.getDefault(), "%.1f", bmi));
                    // Set CircularProgressBar progress according to BMI
                    updateBmiProgressBar(bmi);
                } catch (NumberFormatException e) {
                    bmiTextView.setText("N/A");
                }
            }

            if (birthday != null) {
                int age = calculateAge(birthday);
                ageTextView.setText(age > 0 ? String.valueOf(age) : "Unknown");
            }
        });
    }

    private void updateBmiProgressBar(double bmi) {
        int progress;
        double minBmi = 18.5;
        double maxBmi = 24.9;

        if (bmi < minBmi) {
            progress = 0;
        } else if (bmi > maxBmi) {
            progress = 100;
        } else {
            progress = (int) (((bmi - minBmi) / (maxBmi - minBmi)) * 100);
        }

        circularProgressBarProfile.setMax(100);
        circularProgressBarProfile.setProgress(progress);
    }

    // Method to calculate age based on birthday
    private int calculateAge(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date birthDate;
        try {
            birthDate = sdf.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

        Calendar birthDay = Calendar.getInstance();
        birthDay.setTime(birthDate);
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDay.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    private String getBMIStatus(double bmi) {
        if (bmi < 18.5) {
            return "Below Average";
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            return "On Average";
        } else {
            return "Higher than Average";
        }
    }

    // Initialize the user's level based on how long they've been using the app
    private void initUserLevel() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String firstUseDate = sharedPreferences.getString("firstUseDate", null);

        if (firstUseDate == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String currentDate = getCurrentDate();
            editor.putString("firstUseDate", currentDate);
            editor.apply();
            firstUseDate = currentDate;
        }

        int daysUsed = calculateDaysSince(firstUseDate);
        int userLevel = calculateUserLevel(daysUsed);

        // Update the level TextView
        levelTextView.setText(String.format(Locale.getDefault(), "Level %d", userLevel));

        // Update the level progress bar
        updateLevelProgressBar(daysUsed);
    }

    private int calculateDaysSince(String firstUseDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date firstUse;
        try {
            firstUse = sdf.parse(firstUseDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

        Calendar firstUseCal = Calendar.getInstance();
        firstUseCal.setTime(firstUse);
        Calendar today = Calendar.getInstance();

        long differenceInMillis = today.getTimeInMillis() - firstUseCal.getTimeInMillis();
        return (int) (differenceInMillis / (1000 * 60 * 60 * 24));
    }

    private int calculateUserLevel(int daysUsed) {
        return (daysUsed / 7) + 1; // A new level every 7 days
    }

    private void updateLevelProgressBar(int daysUsed) {
        // Calculate progress within the current level (0-6 days out of the 7-day level)
        int progress = (daysUsed % 7) * 100 / 7;

        // Set the progress on the circular progress bar
        circularProgressBarLevel.setMax(100);
        circularProgressBarLevel.setProgress(progress);

        // Optionally, update the score text to show progress as a percentage
        scoreTextView.setText(String.format(Locale.getDefault(), "%d%%", progress));
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(Calendar.getInstance().getTime());
    }

    private void initViews() {
        nameTextView = findViewById(R.id.usernameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        bmiTextView = findViewById(R.id.bmiTextView);
        levelTextView = findViewById(R.id.level); // Updated TextView for displaying the level
        settingsIcon = findViewById(R.id.three_dots);
        settingsIcon2 = findViewById(R.id.behind_three_dots);
        profileImageView = findViewById(R.id.profile_image_view);
        scoreTextView = findViewById(R.id.scoreset);
    }
}

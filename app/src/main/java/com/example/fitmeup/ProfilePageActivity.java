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

    private TextView nameTextView, ageTextView, weightTextView, heightTextView, bmiTextView, bmiStatusTextView, scoreTextView;
    private ImageView settingsIcon, profileImageView;
    private ShapeableImageView shapeableImageView2, shapeableImageView3;
    private LinearLayout settingsIcon2;
    private RegisterUserDao registerUserDao;
    private RegisterUser registerUser;
    private Executor executor = Executors.newSingleThreadExecutor();
    private ImageButton homeScreenButton;
    private ImageButton profileButton;
    private ImageButton targetButton;
    private ImageButton exerciseButton;
    private ImageButton handshakeButton;

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


        registerUserDao = RegisterUserDatabase.getInstance(this).registerUserDao();

        initViews();

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

        // Set click listener for settings icon
        settingsIcon.setOnClickListener(v -> {
            Toast.makeText(ProfilePageActivity.this, "Settings icon clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfilePageActivity.this, SettingPage.class);
            startActivity(intent);
        });

        // Set click listener for the second settings icon
        settingsIcon2.setOnClickListener(v -> {
            Toast.makeText(ProfilePageActivity.this, "Settings icon 2 clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfilePageActivity.this, SettingPage.class);
            startActivity(intent);
        });

        // Retrieve profile image from Intent
        Intent intent = getIntent();
        String imageUriString = intent.getStringExtra("PROFILE_IMAGE_URI");

        // Set profile image if available
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri imageUri = Uri.parse(imageUriString);
            Glide.with(this).load(imageUri).into(profileImageView); // Load image with Glide
        }
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
                    double heightInMeters = Double.parseDouble(height) / 100;  // Convert height to meters
                    double weightInKg = Double.parseDouble(weight);             // Parse weight
                    double bmi = weightInKg / (heightInMeters * heightInMeters); // Calculate BMI
                    String bmiStatus = getBMIStatus(bmi);                      // Get BMI status

                    // Update BMI and BMI status in TextViews
                    bmiTextView.setText(String.format(Locale.getDefault(), "%.1f", bmi));  // Set BMI to 1 decimal place
                    bmiStatusTextView.setText(bmiStatus);                                // Set BMI status
                } catch (NumberFormatException e) {
                    bmiTextView.setText("N/A"); // Set as not available if error
                    bmiStatusTextView.setText("Unknown");
                }
            }

            // Set age if birthday is available
            if (birthday != null) {
                int age = calculateAge(birthday);
                if (age > 0) {
                    ageTextView.setText(String.valueOf(age));
                } else {
                    ageTextView.setText("Unknown");
                }
            }

            // Calculate and update score
            int score = calculateScore(registerUser);
            scoreTextView.setText(String.format(Locale.getDefault(), "%d%%", score));  // Set score in percentage format
        });
    }

    // Method to calculate age based on the birthday
    private int calculateAge(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date birthDate = null;
        try {
            birthDate = sdf.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle exception
        }

        if (birthDate == null) return 0; // Return 0 if parsing fails

        Calendar birthDay = Calendar.getInstance();
        birthDay.setTime(birthDate);
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDay.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    // Method to return the BMI status based on the BMI value
    private String getBMIStatus(double bmi) {
        if (bmi < 18.5) {
            return "Below Average";
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            return "On Average";
        } else {
            return "Higher than Average";
        }
    }

    // Method to calculate score based on user's performance or other data
    private int calculateScore(RegisterUser registerUser) {
        // Example logic to calculate a score percentage
        // Adjust this logic to match your actual scoring system

        // Assuming you are scoring based on user activity or performance
        int totalPossibleScore = 100;  // Example total score
        int userScore = 85;            // Example user score

        // Calculate score percentage
        return (userScore * 100) / totalPossibleScore;
    }

    // Initialize all views in the activity
    private void initViews() {
        nameTextView = findViewById(R.id.usernameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        bmiTextView = findViewById(R.id.bmiTextView);
        bmiStatusTextView = findViewById(R.id.bmiStatusTextView);
        settingsIcon = findViewById(R.id.three_dots);
        settingsIcon2 = findViewById(R.id.behind_three_dots);
        profileImageView = findViewById(R.id.profile_image_view);
        scoreTextView = findViewById(R.id.scoreset);  // Assuming this is the ID for the score TextView
    }


}

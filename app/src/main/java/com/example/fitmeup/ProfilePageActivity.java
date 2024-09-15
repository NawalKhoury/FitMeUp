package com.example.fitmeup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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
    private ProgressBar circularProgressBarProfile, circularProgressBarLevel, circularProgressBarBMI;
    private ImageDao imageDao;
    private LinearLayout joinButton;
    // Add BMI ProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Initialize Room database and WorkoutDao
        RegisterUserDatabase db = RegisterUserDatabase.getInstance(getApplicationContext());
         imageDao = db.imageDao();

        homeScreenButton = findViewById(R.id.toolbar_home);
        profileButton = findViewById(R.id.toolbar_profile);
        targetButton = findViewById(R.id.toolbar_target);
        exerciseButton = findViewById(R.id.toolbar_exercise);
        handshakeButton = findViewById(R.id.toolbar_handshake);
        joinButton = findViewById(R.id.joinButton);

        joinButton.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, community_activity.class)));

        handshakeButton.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, community_activity.class)));
        homeScreenButton.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, HomePage.class)));
        exerciseButton.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, WorkoutActivity.class)));
        targetButton.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, Model_activity.class)));
        profileButton.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, ProfilePageActivity.class)));

        registerUserDao = RegisterUserDatabase.getInstance(this).registerUserDao();
        initViews();
        loadProfileImage();

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (userId != null) {
            executor.execute(() -> {
                registerUser = registerUserDao.getUserById(userId);
                if (registerUser != null) {
                    updateUserProfileInfo(registerUser);
                }
            });
        } else {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfilePageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        settingsIcon.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, SettingPage.class)));
        settingsIcon2.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, SettingPage.class)));

        initUserLevel();

        Button editWeightButton = findViewById(R.id.editWeightButton);
        editWeightButton.setOnClickListener(v -> showEditWeightDialog());

        Button editheightButton = findViewById(R.id.editheightButton);
        editheightButton.setOnClickListener(v -> showEditheightDialog());

    }

    private void loadProfileImage() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        imageDao.getImageById(username).observe(this, imageUriString -> {
            if (imageUriString != null) {
                Uri imageUri = Uri.parse(imageUriString);
                Glide.with(this).load(imageUri).into(profileImageView);
            } else {
                // Load default image
                profileImageView.setImageResource(R.drawable.default_profile_image);
            }
        });
    }

    private void initViews() {
        nameTextView = findViewById(R.id.usernameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        bmiTextView = findViewById(R.id.bmiTextView);
        levelTextView = findViewById(R.id.level);
        settingsIcon = findViewById(R.id.three_dots);
        settingsIcon2 = findViewById(R.id.behind_three_dots);
        profileImageView = findViewById(R.id.profile_image_view);
        scoreTextView = findViewById(R.id.scoreset);

        circularProgressBarLevel = findViewById(R.id.circularProgressBarLevel);
        circularProgressBarBMI = findViewById(R.id.circularProgressBarProfile);  // Initialize BMI ProgressBar
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
                calculateAndSetBMI(height, weight);
            }

            if (birthday != null) {
                int age = calculateAge(birthday);
                ageTextView.setText(age > 0 ? String.valueOf(age) : "Unknown");
            }
        });
    }

    private void calculateAndSetBMI(String height, String weight) {
        try {
            double heightInMeters = Double.parseDouble(height) / 100;
            double weightInKg = Double.parseDouble(weight);
            double bmi = weightInKg / (heightInMeters * heightInMeters);
            bmiTextView.setText(String.format(Locale.getDefault(), "%.1f", bmi));

            // Update the BMI progress bar
            updateBMIProgressBar(bmi);
        } catch (NumberFormatException e) {
            bmiTextView.setText("N/A");
        }
    }

    private void updateBMIProgressBar(double bmi) {
        circularProgressBarBMI.setMax(40);  // Assume the maximum BMI is 40
        circularProgressBarBMI.setProgress((int) bmi);  // Set progress to the calculated BMI value
    }

    private int calculateAge(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            // Parse the birth date
            Date birthDate = sdf.parse(birthday);
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTime(birthDate);
            Calendar today = Calendar.getInstance();

            // Calculate the age
            int age = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < birthDay.get(Calendar.DAY_OF_YEAR)) {
                age--; // Adjust age if the birthday hasn't occurred yet this year
            }

            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Return 0 if there's an error
        }
    }

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

        levelTextView.setText(String.format(Locale.getDefault(), "Level %d", userLevel));
        updateLevelProgressBar(daysUsed);
    }

    private int calculateDaysSince(String firstUseDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            // Parse the first use date
            Date firstUse = sdf.parse(firstUseDate);
            Calendar firstUseCal = Calendar.getInstance();
            firstUseCal.setTime(firstUse);
            Calendar today = Calendar.getInstance();

            long diffInMillis = today.getTimeInMillis() - firstUseCal.getTimeInMillis();
            return (int) (diffInMillis / (1000 * 60 * 60 * 24)); // Convert milliseconds to days
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Return 0 if there's an error
        }
    }

    private int calculateUserLevel(int daysUsed) {
        return (daysUsed / 7) + 1;  // Simple logic to increase the level every 7 days of use
    }

    private void updateLevelProgressBar(int daysUsed) {
        int progress = (daysUsed % 7) * 100 / 7;
        circularProgressBarLevel.setMax(100); // Ensure the progress bar max is 100
        circularProgressBarLevel.setProgress(progress);
        scoreTextView.setText(String.format(Locale.getDefault(), "%d%%", progress));
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void showEditWeightDialog() {
        // Create a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Weight");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newWeight = input.getText().toString();
            if (!newWeight.isEmpty()) {
                updateWeightInDatabase(newWeight);
            } else {
                Toast.makeText(ProfilePageActivity.this, "Weight cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
    private void updateWeightInDatabase(String newWeight) {
        if (registerUser != null) {
            executor.execute(() -> {
                // Update the weight in the database using the DAO method
                registerUserDao.updateUserWeight(registerUser.getId(), newWeight);
                registerUser.setWeight(newWeight);

                runOnUiThread(() -> {
                    // Update the weight in the UI
                    weightTextView.setText(newWeight);
                    calculateAndSetBMI(registerUser.getHeight(), newWeight);
                    Toast.makeText(ProfilePageActivity.this, "Weight updated", Toast.LENGTH_SHORT).show();
                });
            });
        }
    }

        private void showEditheightDialog() {
            // Create a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Edit Height");

            // Set up the input
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Save", (dialog, which) -> {
                String newHeight = input.getText().toString();
                if (!newHeight.isEmpty()) {
                    updateHeightInDatabase(newHeight);
                } else {
                    Toast.makeText(ProfilePageActivity.this, "Height cannot be empty", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        }
        private void updateHeightInDatabase(String newHeight){
            if (registerUser != null) {
                executor.execute(() -> {
                    // Update the height in the database using the DAO method
                    registerUserDao.updateUserHeight(registerUser.getId(), newHeight);
                    registerUser.setHeight(newHeight);

                    runOnUiThread(() -> {
                        // Update the weight in the UI
                        heightTextView.setText(newHeight);
                        calculateAndSetBMI(registerUser.getHeight(), newHeight);
                        Toast.makeText(ProfilePageActivity.this, "Height updated", Toast.LENGTH_SHORT).show();
                    });
                });
            }
        }

}

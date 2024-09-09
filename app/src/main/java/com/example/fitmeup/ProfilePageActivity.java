package com.example.fitmeup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

public class ProfilePageActivity extends AppCompatActivity {

    private TextView nameTextView, ageTextView, weightTextView, heightTextView, bmiTextView, scoreTextView, bmiStatusTextView;
    private ImageView settingsIcon, profileImageView;
    private ShapeableImageView shapeableImageView2, shapeableImageView3;
    private LinearLayout settingsIcon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        // Initialize the views
        initViews();

        // Set the click listener for the settings icon
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfilePageActivity.this, "Settings icon clicked", Toast.LENGTH_SHORT).show();
                // Open the Settings page
                Intent intent = new Intent(ProfilePageActivity.this, SettingPage.class);
                startActivity(intent);
            }
        });

        // Set the click listener for the second settings icon
        settingsIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfilePageActivity.this, "Settings icon 2  clicked", Toast.LENGTH_SHORT).show();
                // Open the Settings page
                Intent intent = new Intent(ProfilePageActivity.this, SettingPage.class);
                startActivity(intent);
            }
        });

        // Retrieve the intent data for the profile image
        Intent intent = getIntent();
        String imageUriString = intent.getStringExtra("PROFILE_IMAGE_URI");
        String name = intent.getStringExtra("USER_NAME");
        String height = intent.getStringExtra("USER_HEIGHT");
        String weight = intent.getStringExtra("USER_WEIGHT");
        String birthday = intent.getStringExtra("USER_BIRTHDAY");

        // If the image URI is not null, display it
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri imageUri = Uri.parse(imageUriString);
            Glide.with(this).load(imageUri).into(profileImageView); // Load image with Glide
        }

        // Set the name, height, weight, and age to TextViews
        if (name != null) {
            nameTextView.setText(name);
        }

        if (height != null && weight != null) {
            heightTextView.setText(height);
            weightTextView.setText(weight);

            // Calculate and display BMI
            double heightInMeters = Double.parseDouble(height) / 100;
            double weightInKg = Double.parseDouble(weight);
            double bmi = weightInKg / (heightInMeters * heightInMeters);

            String bmiStatus;
            if (bmi < 18.5) {
                bmiStatus = "Below Average";
            } else if (bmi >= 18.5 && bmi <= 24.9) {
                bmiStatus = "On Average";
            } else {
                bmiStatus = "Higher than Average";
            }

            bmiTextView.setText(String.format("%.1f", bmi)); // Format BMI to 1 decimal place
            bmiStatusTextView.setText(bmiStatus); // Display BMI status
        }

        // Set age based on birthday
        if (birthday != null) {
            int age = calculateAge(birthday);
            ageTextView.setText(String.valueOf(age));
        }
    }

    private void initViews() {
        // Initialize views by finding their IDs
        nameTextView = findViewById(R.id.usernameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        bmiTextView = findViewById(R.id.bmiTextView);
        bmiStatusTextView = findViewById(R.id.bmiStatusTextView);
        settingsIcon = findViewById(R.id.three_dots);
        settingsIcon2 = findViewById(R.id.behind_three_dots);
        profileImageView = findViewById(R.id.profile_image_view); // Initialize the profile image view
    }

    // Method to calculate the age based on the birthday
    private int calculateAge(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = null;
        try {
            birthDate = sdf.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle exception
        }
        if (birthDate == null) return 0; // If parsing fails

        Calendar birthDay = Calendar.getInstance();
        birthDay.setTime(birthDate);
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDay.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }
}

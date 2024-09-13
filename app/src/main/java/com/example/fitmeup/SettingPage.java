package com.example.fitmeup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SettingPage extends AppCompatActivity {

    private ImageView profileImageView;
    private ImageView selectProfileImageButton;
    private ImageView backtoprofile1; // Declare the backtoprofile1 ImageView
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    private LinearLayout privacyPolicyLayout, reviewButton, helpSupportButton, logoutButton, resetPassword;
    private TextView nameSetting, emailSetting;

    private RegisterUserDao registerUserDao;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page); // Settings layout XML

        // Initialize database DAO
        registerUserDao = RegisterUserDatabase.getInstance(this).registerUserDao();

        // Initialize Views
        profileImageView = findViewById(R.id.settings_profile_image_view);
        selectProfileImageButton = findViewById(R.id.select_profile_image_button);
        backtoprofile1 = findViewById(R.id.backtoprofile1); // Initialize backtoprofile1
        privacyPolicyLayout = findViewById(R.id.privacypolicylayout);
        reviewButton = findViewById(R.id.reviewbutton);
        helpSupportButton = findViewById(R.id.helpsupportbutton);
        logoutButton = findViewById(R.id.logoutButton);
        nameSetting = findViewById(R.id.namesetting);
        emailSetting = findViewById(R.id.emailsetting);
        resetPassword = findViewById(R.id.resetPassword);

        // Load user's name and email from the database using userId
        loadUserInfo();

        // Set up listeners for buttons and image selector
        setupListeners();
    }

    // Load user information from shared preferences and database
    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        if (!userId.isEmpty()) {
            executor.execute(() -> {
                RegisterUser registerUser = registerUserDao.getUserById(userId);
                runOnUiThread(() -> {
                    if (registerUser != null) {
                        nameSetting.setText("Hey, " + registerUser.getUsername());
                        emailSetting.setText(registerUser.getEmail());
                    } else {
                        nameSetting.setText("User not found");
                        emailSetting.setText("No email available");
                    }
                });
            });
        } else {
            nameSetting.setText("User not found");
            emailSetting.setText("No email available");
        }
    }

    // Setup listeners for various UI elements
    private void setupListeners() {
        // Back to profile button listener
        backtoprofile1.setOnClickListener(v -> {
            Intent intent = new Intent(SettingPage.this, ProfilePageActivity.class); // Go back to ProfilePageActivity
            startActivity(intent);
        });

        // Help and Support
        helpSupportButton.setOnClickListener(v -> startActivity(new Intent(this, HelpSupportActivity.class)));

        // Logout Button
        logoutButton.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        // Reset Password Button
        resetPassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));

        // Review Button
        reviewButton.setOnClickListener(v -> startActivity(new Intent(this, ReviewPageActivity.class)));

        // Privacy Policy Button
        privacyPolicyLayout.setOnClickListener(v -> startActivity(new Intent(this, PrivacyPolicyActivity.class)));

        // Profile Image Button (Select Image)
        selectProfileImageButton.setOnClickListener(v -> {
            checkAndRequestPermission(); // Check permission before accessing gallery
        });
    }

    // Check for the necessary permissions (based on Android version) and open the gallery
    private void checkAndRequestPermission() {
        String permission;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and above
            permission = android.Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            // For Android 12 and below
            permission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        // If permission is not granted, request it
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            // Permission granted, open gallery
            openGallery();
        }
    }

    // Open the device's gallery for selecting an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result from permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                openGallery();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied to access media", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handle the result from the gallery intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get selected image URI and display it in the profileImageView
            selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri); // Display the selected image
        }
    }
}

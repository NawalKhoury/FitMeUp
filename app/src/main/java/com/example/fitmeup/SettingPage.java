package com.example.fitmeup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SettingPage extends AppCompatActivity {

    private ImageView profileImageView;
    private ImageView selectProfileImageButton;
    private ImageView backtoprofile1;
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
        setContentView(R.layout.activity_setting_page);

        registerUserDao = RegisterUserDatabase.getInstance(this).registerUserDao();

        profileImageView = findViewById(R.id.default_profile_image);
        selectProfileImageButton = findViewById(R.id.select_profile_image_button);
        backtoprofile1 = findViewById(R.id.backtoprofile1);
        privacyPolicyLayout = findViewById(R.id.privacypolicylayout);
        reviewButton = findViewById(R.id.reviewbutton);
        helpSupportButton = findViewById(R.id.helpsupportbutton);
        logoutButton = findViewById(R.id.logoutButton);
        nameSetting = findViewById(R.id.namesetting);
        emailSetting = findViewById(R.id.emailsetting);
        resetPassword = findViewById(R.id.resetPassword);

        loadUserInfo();
       loadProfileImage();
        setupListeners();
    }

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

    private void loadProfileImage() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String imageUriString = sharedPreferences.getString("profileImageUri", null);

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            Glide.with(this).load(imageUri).into(profileImageView);
        } else {
            // Load default image
            profileImageView.setImageResource(R.drawable.default_profile_image);
        }
    }

    private void setupListeners() {
        backtoprofile1.setOnClickListener(v -> {
            Intent intent = new Intent(SettingPage.this, ProfilePageActivity.class);
            startActivity(intent);
        });

        helpSupportButton.setOnClickListener(v -> startActivity(new Intent(this, HelpSupportActivity.class)));
        logoutButton.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        resetPassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
        reviewButton.setOnClickListener(v -> startActivity(new Intent(this, ReviewPageActivity.class)));
        privacyPolicyLayout.setOnClickListener(v -> startActivity(new Intent(this, PrivacyPolicyActivity.class)));

        selectProfileImageButton.setOnClickListener(v -> checkAndRequestPermission());
    }

    private void checkAndRequestPermission() {
        String permission;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                Glide.with(this).load(selectedImageUri).into(profileImageView);

                // Save the image URI in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("profileImageUri", selectedImageUri.toString());
                editor.apply();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}

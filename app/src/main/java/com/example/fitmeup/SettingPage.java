package com.example.fitmeup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SettingPage extends AppCompatActivity {

    private ImageView profileImageView;
    private ImageView selectProfileImageButton;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private ImageView backtoprofile;
    private LinearLayout privacyPolicyLayout;
    private LinearLayout reviewButton, helpSupportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page); //settings layout XML

        backtoprofile = findViewById(R.id.backtoprofile1);
        selectProfileImageButton = findViewById(R.id.select_profile_image_button);
        profileImageView = findViewById(R.id.settings_profile_image_view);
        privacyPolicyLayout = findViewById(R.id.privacypolicylayout);
        reviewButton = findViewById(R.id.reviewbutton);
        helpSupportButton = findViewById(R.id.helpsupportbutton);

        helpSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingPage.this, HelpSupportActivity.class);
                startActivity(intent);
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingPage.this, ReviewPageActivity.class);
                startActivity(intent);
            }
        });

        privacyPolicyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingPage.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });


        // Image selection button click listener
        selectProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(SettingPage.this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SettingPage.this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
                    } else {
                        openGallery();
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(SettingPage.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SettingPage.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    } else {
                        openGallery();
                    }
                }
            }
        });

        backtoprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Settings page
                Intent intent = new Intent(SettingPage.this, ProfilePageActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to open the gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied to access media", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            // Display the selected image in the profileImageView in the settings page
            profileImageView.setImageURI(selectedImageUri);

            // Pass the image URI to the ProfilePageActivity
            Intent intent = new Intent(SettingPage.this, ProfilePageActivity.class);
            intent.putExtra("PROFILE_IMAGE_URI", selectedImageUri.toString()); // Pass the URI as a string
            startActivity(intent);
        }
    }
}

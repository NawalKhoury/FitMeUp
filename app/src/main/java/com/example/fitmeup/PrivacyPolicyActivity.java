package com.example.fitmeup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private Button backToSettingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);// Link the layout for the privacy policy
        // Initialize the back to settings button
        backToSettingsButton = findViewById(R.id.back_to_settings_button);

        // Set click listener for the button
        backToSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the SettingPage activity
                Intent intent = new Intent(PrivacyPolicyActivity.this, SettingPage.class);
                startActivity(intent);
                finish(); // Optionally finish the current activity to remove it from the back stack
            }
        });
    }


}

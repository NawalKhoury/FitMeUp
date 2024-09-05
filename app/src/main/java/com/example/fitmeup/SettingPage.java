package com.example.fitmeup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;



public class SettingPage extends AppCompatActivity{
    private ImageView backToProfileImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page); // Your settings layout XML

        backToProfileImageView = findViewById(R.id.r5fi6k83s4og);
        // Set the click listener to go back to the ProfilePageActivity
        backToProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the ProfilePageActivity
                Intent intent = new Intent(SettingPage.this, ProfilePageActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish to close the SettingPage activity
            }
        });


    }
}

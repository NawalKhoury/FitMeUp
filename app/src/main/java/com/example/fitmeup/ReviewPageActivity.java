package com.example.fitmeup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ReviewPageActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private EditText feedbackEditText;
    private Button submitButton;
    private SharedPreferences sharedPreferences;
    private Button backToSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);

        // Initialize views
        ratingBar = findViewById(R.id.ratingBar);
        feedbackEditText = findViewById(R.id.feedbackEditText);
        submitButton = findViewById(R.id.submitButton);
        backToSettingsButton = findViewById(R.id.back_to_settings_button);

        // Initialize SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String feedback = feedbackEditText.getText().toString().trim();

                if (feedback.isEmpty()) {
                    Toast.makeText(ReviewPageActivity.this, "Please provide your feedback", Toast.LENGTH_SHORT).show();
                } else {
                    //save the rating and feedback
                    saveReviewData(rating, feedback);
                    // Notify the user that the feedback has been saved
                    Toast.makeText(ReviewPageActivity.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                    //clear the fields after submission
                    feedbackEditText.setText("");
                    ratingBar.setRating(0);
                }
            }
        });

        // Set up button click listener for back to settings
        backToSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the SettingPage activity
                Intent intent = new Intent(ReviewPageActivity.this, SettingPage.class);
                startActivity(intent);
                finish();  //finish the review activity
            }
        });
    }

    // Method to save rating and feedback using SharedPreferences
    private void saveReviewData(float rating, String feedback) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("USER_RATING", rating);
        editor.putString("USER_FEEDBACK", feedback);
        editor.apply();  // Use apply() to save asynchronously
    }

}

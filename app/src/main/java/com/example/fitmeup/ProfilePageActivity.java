package com.example.fitmeup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfilePageActivity extends AppCompatActivity {

    private TextView nameTextView, ageTextView, weightTextView, heightTextView, bmiTextView, scoreTextView;
    private ImageView imageView1, imageView2, imageView3, settingsIcon;
    private ShapeableImageView shapeableImageView1, shapeableImageView2, shapeableImageView3;
    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;

    //1yqdxvb4b1o
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page); // Ensure your XML is named `activity_profile_page.xml`
        // Initialize the views
        initViews();

        // Set the click listener for the settings icon
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the SettingsActivity
                Intent intent = new Intent(ProfilePageActivity.this, SettingPage.class);
                startActivity(intent);
            }


        });

        // Example: Load profile data (e.g., from a backend or local storage)
        loadProfileData();

        // Example: Set up listeners for views if necessary
        setupListeners();
    }

    private void initViews() {
        // Find all your views by their IDs
        nameTextView = findViewById(R.id.rwqg4jf4gjm9);
        ageTextView = findViewById(R.id.rd24t617aakm);
        weightTextView = findViewById(R.id.r3bdudmfdflm);
        heightTextView = findViewById(R.id.rafzbfx5hp7);
        bmiTextView = findViewById(R.id.rkdqrc8csk2);
        scoreTextView = findViewById(R.id.ruqscp0ns0qe);

        settingsIcon = findViewById(R.id.rk7kyita9y1);
        shapeableImageView1 = findViewById(R.id.rh5jiv8vhh4t);
        shapeableImageView2 = findViewById(R.id.r86k3c9i0tg);
        shapeableImageView3 = findViewById(R.id.rn2mf63cl0hf);

        linearLayout1 = findViewById(R.id.r21hk3kx3wvm);
        linearLayout2 = findViewById(R.id.r9f0qxa3gnms);
        linearLayout3 = findViewById(R.id.riweph8u1koh);
        linearLayout4 = findViewById(R.id.rbeidnvkzs2c);

        // Set default text or images as an example
        nameTextView.setText("Dima Haj");
        ageTextView.setText("23");
        weightTextView.setText("53");
        heightTextView.setText("163");
        bmiTextView.setText("20.4");
        scoreTextView.setText("72");

        // Load an image into ImageView using Glide
        Glide.with(this)
                .load("R.drawable.profileframe") // Replace with actual image URL
                .into(shapeableImageView1);

        // Set additional images if needed
        Glide.with(this)
                .load("graphpie.png") // Replace with actual image URL
                .into(shapeableImageView2);

        Glide.with(this)
                .load("graphpie.png") // Replace with actual image URL
                .into(shapeableImageView3);
    }

    private void loadProfileData() {
        // If you have a user model, load the data into the views
        // Example: User user = getUserData();
        // nameTextView.setText(user.getName());
        // ageTextView.setText(user.getAge());
        // weightTextView.setText(user.getWeight() + " kg");
        // heightTextView.setText(user.getHeight() + " cm");
        // bmiTextView.setText(user.getBmi());
        // scoreTextView.setText(user.getScore());
        // Glide.with(this).load(user.getProfileImageUrl()).into(profileImageView);
    }

    private void setupListeners() {
        // Example: Set up listeners for interactive elements
        linearLayout1.setOnClickListener(v -> {
            // Handle click event for the first LinearLayout
        });

        shapeableImageView1.setOnClickListener(v -> {
            // Handle click event for changing profile picture
            changeProfilePicture();
        });
    }

    private void changeProfilePicture() {
        // Handle the action to change the profile picture
        // Example: open gallery to pick an image
        // Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    // Optionally override onActivityResult to handle the result of selecting a profile picture
    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //     super.onActivityResult(requestCode, resultCode, data);
    //     if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
    //         // Handle the selected image and update the profile picture
    //         Uri selectedImage = data.getData();
    //         Glide.with(this).load(selectedImage).into(profileImageView);
    //     }
    // }
}


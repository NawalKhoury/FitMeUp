package com.example.fitmeup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class community_activity extends AppCompatActivity {

    private List<String> posts;
    private ArrayAdapter<String> adapter;
    private EditText input;
    private ListView listView;
    private ImageButton handshakeButton;
    private ImageButton home;
    private ImageButton workout;
    private ImageButton profile;
    private ImageButton training;
    private ImageButton reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        initializeViews();
        setupListView();
        setupPostButton();
        setupToolbarButtons();
    }

    private void initializeViews() {
        input = findViewById(R.id.text_input);
        listView = findViewById(R.id.listView);

        // Initialize ImageButtons after setContentView
        handshakeButton = findViewById(R.id.toolbar_handshake);
        home = findViewById(R.id.toolbar_home);
        workout = findViewById(R.id.toolbar_target);
        profile = findViewById(R.id.toolbar_profile);
        training = findViewById(R.id.toolbar_exercise);
        reminder = findViewById(R.id.reminderButton);

        // Show keyboard when EditText is clicked
        input.setOnClickListener(v -> showKeyboard(input));
    }

    private void setupListView() {
        posts = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, posts);
        listView.setAdapter(adapter);
    }

    private void setupPostButton() {
        Button postButton = findViewById(R.id.Postbutton);
        postButton.setOnClickListener(v -> handlePostButtonClick());
    }

    private void setupToolbarButtons() {
        home.setOnClickListener(v -> startActivity(new Intent(community_activity.this, HomePage.class)));
        handshakeButton.setOnClickListener(v -> startActivity(new Intent(community_activity.this, community_activity.class)));
        training.setOnClickListener(v -> startActivity(new Intent(community_activity.this, Timer_activity.class))); // Correct reference
        profile.setOnClickListener(v -> startActivity(new Intent(community_activity.this, ProfilePageActivity.class)));
        workout.setOnClickListener(v -> startActivity(new Intent(community_activity.this, WorkoutActivity.class))); // Fixed training -> workout
    }

    private void handlePostButtonClick() {
        String newPost = input.getText().toString().trim();
        if (newPost.isEmpty()) {
            Toast.makeText(this, "Post cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            addNewPost(newPost);
        }
    }

    private void addNewPost(String post) {
        posts.add(post);
        adapter.notifyDataSetChanged();
        input.setText(""); // Clear the input field
        listView.smoothScrollToPosition(posts.size() - 1); // Scroll to the latest post
        hideKeyboard(input); // Hide the keyboard after posting
    }

    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

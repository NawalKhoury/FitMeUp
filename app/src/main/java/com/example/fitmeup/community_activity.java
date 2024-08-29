package com.example.fitmeup;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        initializeViews();
        setupListView();
        setupPostButton();
    }


    private void initializeViews() {
        input = findViewById(R.id.text_input);
        Button postButton = findViewById(R.id.Postbutton);
        listView = findViewById(R.id.listView);

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

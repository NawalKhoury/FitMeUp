package com.example.fitmeup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    // Local Room database and DAO
    private PostDao postDao;

    // Retrofit API client
    private PostApi postApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        initializeViews();
        setupListView();
        setupPostButton();
        setupToolbarButtons();

        // Initialize Room database
        RegisterUserDatabase db = RegisterUserDatabase.getInstance(getApplicationContext());

        postDao = db.PostDao();

        // Initialize Retrofit for API interaction
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.27:8080/") // Set your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        postApi = retrofit.create(PostApi.class);

        // Load posts from local DB and remote API
        loadPosts();
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
        training.setOnClickListener(v -> startActivity(new Intent(community_activity.this, WorkoutActivity.class))); // Correct reference
        profile.setOnClickListener(v -> startActivity(new Intent(community_activity.this, ProfilePageActivity.class)));
    }

    private void handlePostButtonClick() {
        String newPost = input.getText().toString().trim();
        if (newPost.isEmpty()) {
            Toast.makeText(this, "Post cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            Post post = new Post(newPost, new Date());

            // Save post locally and sync with server
            addNewPost(post);
        }
    }

    private void addNewPost(Post post) {
        // Run database insertion in a background thread
        new Thread(() -> {
            // Add to local database
            postDao.insert(post);

            // After inserting, update the UI on the main thread
            runOnUiThread(() -> {
                // Add to local view
                posts.add(post.getDescription());
                adapter.notifyDataSetChanged();
                input.setText(""); // Clear input field
                listView.smoothScrollToPosition(posts.size() - 1); // Scroll to latest post
                hideKeyboard(input); // Hide keyboard after posting
            });
        }).start();

        // Sync with server (optional)
        postApi.createPost(post).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(community_activity.this, "Post synced with server", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(community_activity.this, "Failed to sync post with server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(community_activity.this, "Network error while syncing post", Toast.LENGTH_SHORT).show();
                Log.e("RetrofitError", "Error: " + t.getMessage(), t);
            }

        });
    }


    private void loadPosts() {
        // Use a background thread for database operations
        new Thread(() -> {
            List<Post> localPosts = postDao.getAllPosts();
            runOnUiThread(() -> {
                for (Post post : localPosts) {
                    posts.add(post.getDescription());
                }
                adapter.notifyDataSetChanged();
            });
        }).start();

        // Optionally load posts from server using Retrofit
        postApi.getAllPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> remotePosts = response.body();
                    if (remotePosts != null) {
                        for (Post post : remotePosts) {
                            if (!posts.contains(post.getDescription())) {
                                posts.add(post.getDescription());
                                postDao.insert(post); // Save remote posts locally
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                Toast.makeText(community_activity.this, "Network error while syncing post", Toast.LENGTH_SHORT).show();
                Log.e("RetrofitError", "Error: " + t.getMessage(), t);
            }

        });
    }


    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

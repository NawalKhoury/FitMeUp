package com.example.fitmeup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;
    private RegisterUserDao registerUserDao;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the DAO
        registerUserDao = RegisterUserDatabase.getInstance(this).registerUserDao();

        // Find views by ID
        EditText usernameInput = findViewById(R.id.username_input);
        EditText passwordInput = findViewById(R.id.password_input);
        ImageView togglePasswordVisibility = findViewById(R.id.rm032iqzqkza);
        TextView registerNow = findViewById(R.id.r1t5cvzq2qv5);
        TextView forgotPassword = findViewById(R.id.rvpdl4ik3hh);
        Button loginButton = findViewById(R.id.rgngo6kcmde5);

        // Toggle password visibility
        togglePasswordVisibility.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                togglePasswordVisibility.setImageResource(R.drawable.showpass);
                isPasswordVisible = false;
            } else {
                passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                togglePasswordVisibility.setImageResource(R.drawable.showpass);
                isPasswordVisible = true;
            }
            passwordInput.setSelection(passwordInput.getText().length());
        });

        // Set OnClickListener on the Register TextView to open RegisterActivity
        registerNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener on the Forgot Password TextView to open ForgotPasswordActivity
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener on the Login button to validate credentials
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perform the database query in the background
            executor.execute(() -> {
                RegisterUser user = registerUserDao.getUserByUsernameAndPassword(username, password);
                runOnUiThread(() -> {
                    if (user != null) {
                        // Credentials are correct, open HomeActivity
                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
                        intent.putExtra("username", username); // Add the username to the Intent
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId", user.getId() + "");
                        editor.putString("username", user.getUsername());
                        editor.apply();
                        startActivity(intent);
                        finish(); // Optional: Close LoginActivity
                    } else {
                        // Credentials are incorrect, show an error message
                        Toast.makeText(LoginActivity.this, "Invalid username or password, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });


    }
}
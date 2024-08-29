package com.example.fitmeup;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ForgotPasswordActivity extends AppCompatActivity {

    private RegisterUserDao registerUserDao;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize the DAO
        registerUserDao = RegisterUserDatabase.getInstance(this).registerUserDao();

        // Find views by ID
        EditText usernameInput = findViewById(R.id.username_input);
        Spinner securityQuestionSpinner = findViewById(R.id.security_question_spinner);
        EditText securityAnswerInput = findViewById(R.id.security_answer_input);
        EditText newPasswordInput = findViewById(R.id.new_password_input);
        EditText confirmPasswordInput = findViewById(R.id.confirm_password_input);
        Button doneButton = findViewById(R.id.done_button);

        // Set up the security question spinner programmatically
        String[] securityQuestions = {
                "What is your favorite food?",
                "What is the name of your favorite movie?",
                "Where did you go on your first vacation?",
                "What was the name of your first teacher?",
                "What is the name of your first pet?"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, securityQuestions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        securityQuestionSpinner.setAdapter(adapter);

        // Set OnClickListener on the Done button
        doneButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String securityQuestion = securityQuestionSpinner.getSelectedItem().toString();
            String securityAnswer = securityAnswerInput.getText().toString().trim();
            String newPassword = newPasswordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (username.isEmpty() || securityAnswer.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(ForgotPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perform the database query in the background
            executor.execute(() -> {
                RegisterUser user = registerUserDao.getUserBySecurityQuestionAndAnswer(securityQuestion, securityAnswer);
                runOnUiThread(() -> {
                    if (user != null && user.getUsername().equals(username)) {
                        // Update the password
                        user.setPassword(newPassword);
                        executor.execute(() -> registerUserDao.update(user));
                        Toast.makeText(ForgotPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity and navigate back to login
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Incorrect answer or username, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }
}

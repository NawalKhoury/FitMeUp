
package com.example.fitmeup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    private RegisterUserDao registerUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        registerUserDao = RegisterUserDatabase.getInstance(this).registerUserDao();

        // Gender Spinner setup
        Spinner genderSpinner = findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Health Problems Spinner setup
        Spinner healthProblemsSpinner = findViewById(R.id.health_problems_spinner);
        ArrayAdapter<CharSequence> healthProblemsAdapter = ArrayAdapter.createFromResource(this,
                R.array.health_problems_options, android.R.layout.simple_spinner_item);
        healthProblemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        healthProblemsSpinner.setAdapter(healthProblemsAdapter);

        // Security Question Spinner setup
        Spinner securityQuestionSpinner = findViewById(R.id.security_question_spinner);
        ArrayAdapter<CharSequence> securityQuestionAdapter = ArrayAdapter.createFromResource(this,
                R.array.security_questions, android.R.layout.simple_spinner_item);
        securityQuestionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        securityQuestionSpinner.setAdapter(securityQuestionAdapter);

        // Date picker setup for birth date
        EditText birthDateInput = findViewById(R.id.birthdate_input);
        birthDateInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RegisterActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        birthDateInput.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Setup for password visibility toggle
        EditText passwordInput = findViewById(R.id.password_input);
        ImageView togglePasswordVisibility = findViewById(R.id.password_toggle_icon);

        togglePasswordVisibility.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                togglePasswordVisibility.setImageResource(R.drawable.showpass); // Change to 'show' icon
                isPasswordVisible = false;
            } else {
                passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                togglePasswordVisibility.setImageResource(R.drawable.showpass); // Change to 'hide' icon
                isPasswordVisible = true;
            }
            passwordInput.setSelection(passwordInput.getText().length());
        });

        // Setup for confirm password visibility toggle
        EditText confirmPasswordInput = findViewById(R.id.confirm_password_input);
        ImageView toggleConfirmPasswordVisibility = findViewById(R.id.confirm_password_toggle_icon);

        toggleConfirmPasswordVisibility.setOnClickListener(v -> {
            if (isConfirmPasswordVisible) {
                confirmPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                toggleConfirmPasswordVisibility.setImageResource(R.drawable.showpass); // Change to 'show' icon
                isConfirmPasswordVisible = false;
            } else {
                confirmPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                toggleConfirmPasswordVisibility.setImageResource(R.drawable.showpass); // Change to 'hide' icon
                isConfirmPasswordVisible = true;
            }
            confirmPasswordInput.setSelection(confirmPasswordInput.getText().length());
        });

        // Setup the registration button click listener
        TextView registerButton = findViewById(R.id.rbtmyggw0cj8);
        registerButton.setOnClickListener(v -> {
            EditText usernameInput = findViewById(R.id.username_input);
            EditText emailInput = findViewById(R.id.email_input);
            EditText confirmEmailInput = findViewById(R.id.confirm_email_input);
            EditText passwordInputField = findViewById(R.id.password_input);
            EditText confirmPasswordInputField = findViewById(R.id.confirm_password_input);
            EditText birthDateInputField = findViewById(R.id.birthdate_input);
            EditText weightInputField = findViewById(R.id.weight_input);
            EditText heightInputField = findViewById(R.id.height_input);
            EditText securityAnswerInput = findViewById(R.id.security_answer_input);

            String username = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String confirmEmail = confirmEmailInput.getText().toString().trim();
            String password = passwordInputField.getText().toString().trim();
            String confirmPassword = confirmPasswordInputField.getText().toString().trim();
            String birthDate = birthDateInputField.getText().toString().trim();
            String weight = weightInputField.getText().toString().trim();
            String height = heightInputField.getText().toString().trim();
            String securityAnswer = securityAnswerInput.getText().toString().trim();
            String selectedGender = genderSpinner.getSelectedItem().toString();
            String selectedSecurityQuestion = securityQuestionSpinner.getSelectedItem().toString();
            String selectedHealthProblems = healthProblemsSpinner.getSelectedItem().toString();

            // Validate inputs
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(RegisterActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(RegisterActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            } else if (!email.equals(confirmEmail)) {
                Toast.makeText(RegisterActivity.this, "Emails do not match", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (selectedGender.equals("Select Gender")) {
                Toast.makeText(RegisterActivity.this, "Please select your gender", Toast.LENGTH_SHORT).show();
            } else if (selectedHealthProblems.equals("Select an option")) {
                Toast.makeText(RegisterActivity.this, "Please specify if you have health problems", Toast.LENGTH_SHORT).show();
            } else if (selectedSecurityQuestion.equals("Select a security question")) {
                Toast.makeText(RegisterActivity.this, "Please select a security question", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(birthDate)) {
                Toast.makeText(RegisterActivity.this, "Please select your birth date", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(weight)) {
                Toast.makeText(RegisterActivity.this, "Please enter your weight", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(height)) {
                Toast.makeText(RegisterActivity.this, "Please enter your height", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(securityAnswer)) {
                Toast.makeText(RegisterActivity.this, "Please provide an answer to the security question", Toast.LENGTH_SHORT).show();
            } else {
                RegisterUser newUser = new RegisterUser(
                        username, email, confirmEmail, password, confirmPassword, birthDate,
                        selectedGender, selectedHealthProblems, selectedSecurityQuestion, securityAnswer, weight, height
                );

                Intent intent1 = new Intent(RegisterActivity.this, ProfilePageActivity.class);
                intent1.putExtra("USER_HEIGHT", height);
                intent1.putExtra("USER_WEIGHT", weight);
                intent1.putExtra("USER_BIRTHDAY", birthDate);
                intent1.putExtra("USER_NAME", username);
                startActivity(intent1);




                Log.d("DB", "Inserting new user...");
                new Thread(() -> {
                    try {
                        registerUserDao.insert(newUser);
                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    } catch (Exception e) {
                        Log.e("DB", "Error inserting new user", e);
                    }
                }).start();

            }
        });
    }


}
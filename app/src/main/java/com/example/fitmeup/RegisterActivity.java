
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

    public static class Timer_activity extends AppCompatActivity {

        private ImageButton handshakeButton;
        private ImageButton home;
        private ImageButton workout;
        private ImageButton profile;
        private ImageButton training;

        private Handler handler = new Handler();
        private int seconds = 0;
        private boolean isRunning = false;
        private ImageView startPauseButton; // Consolidated start/pause button
        private TextView hoursTextView, minutesTextView, secondsTextView;
        private LinearLayout indicatorLayout;
        private ViewPager2 viewPager;
        private int pageCount = 2; // Set this to the number of pages you have

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_timer);

            handshakeButton = findViewById(R.id.toolbar_handshake);
            home = findViewById(R.id.toolbar_home);
            workout = findViewById(R.id.toolbar_target);
            profile = findViewById(R.id.toolbar_profile);
            training = findViewById(R.id.toolbar_exercise);

            handshakeButton.setOnClickListener(v -> startActivity(new Intent(Timer_activity.this, community_activity.class)));
            home.setOnClickListener(v -> startActivity(new Intent(Timer_activity.this, HomePage.class)));

            viewPager = findViewById(R.id.viewPager);
            indicatorLayout = findViewById(R.id.indicatorLayout);

            // Setup ViewPager2 and its adapter
            int[] layouts = {R.layout.layout_time1, R.layout.layout_time2};
            ViewPagerAdapter adapter = new ViewPagerAdapter(this, layouts);
            viewPager.setAdapter(adapter);

            // Create indicator dots
            createIndicators(pageCount);

            // Register a callback for when the page changes
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    updateIndicators(position);
                    // Accessing Views in the currently displayed page
                    RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);

                    if (viewHolder != null) {
                        View pageView = viewHolder.itemView;

                        // Set up NumberPickers
                        NumberPicker numberPicker1 = pageView.findViewById(R.id.numberPicker1);
                        NumberPicker numberPicker2 = pageView.findViewById(R.id.numberPicker2);

                        if (numberPicker1 != null && numberPicker2 != null) {
                            numberPicker1.setMinValue(0);
                            numberPicker1.setMaxValue(10);
                            numberPicker1.setValue(0);

                            numberPicker2.setMinValue(0);
                            numberPicker2.setMaxValue(59);
                            numberPicker2.setFormatter(value -> String.format("%02d", value));
                            numberPicker2.setValue(0);
                        }

                        // Set up timer related views
                        startPauseButton = pageView.findViewById(R.id.start_pause_button); // Combined start/pause button
                        hoursTextView = pageView.findViewById(R.id.hours);
                        minutesTextView = pageView.findViewById(R.id.minutes);
                        secondsTextView = pageView.findViewById(R.id.seconds);

                        if (startPauseButton != null) {
                            startPauseButton.setOnClickListener(v -> toggleTimer());
                        }

                        // Update the UI if the timer is running
                        if (isRunning && hoursTextView != null && minutesTextView != null && secondsTextView != null) {
                            updateTime();  // Immediately update the UI with the current timer value
                        }
                    }
                }
            });

            // Continue running the timer if it was already started before
            if (isRunning) {
                handler.postDelayed(runnable, 1000);
            }
        }

        private void toggleTimer() {
            if (isRunning) {
                stopTimerSafely();
            } else {
                startTimer();
            }
        }

        private void startTimer() {
            isRunning = true;
            if (startPauseButton != null) {
                startPauseButton.setImageResource(R.drawable.play); // Change to play
            }
            handler.postDelayed(runnable, 1000);
        }

        private void stopTimerSafely() {
            isRunning = false;
            if (startPauseButton != null) {
                startPauseButton.setImageResource(R.drawable.pause); // Change to pause
            }
            handler.removeCallbacks(runnable);
        }

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                seconds++;
                updateTime();
                if (isRunning) {
                    handler.postDelayed(this, 1000);
                }
            }
        };

        private void updateTime() {
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60;

            if (hoursTextView != null) {
                hoursTextView.setText(String.format("%02d", hours));
            }
            if (minutesTextView != null) {
                minutesTextView.setText(String.format("%02d", minutes));
            }
            if (secondsTextView != null) {
                secondsTextView.setText(String.format("%02d", secs));
            }
        }

        private void createIndicators(int count) {
            for (int i = 0; i < count; i++) {
                View dot = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        getResources().getDimensionPixelSize(R.dimen.indicator_size),
                        getResources().getDimensionPixelSize(R.dimen.indicator_size));
                params.setMargins(8, 0, 8, 0);
                dot.setLayoutParams(params);
                dot.setBackgroundResource(R.drawable.indicator_inactive); // Drawable for inactive state
                indicatorLayout.addView(dot);
            }
        }

        private void updateIndicators(int position) {
            for (int i = 0; i < indicatorLayout.getChildCount(); i++) {
                View dot = indicatorLayout.getChildAt(i);
                if (i == position) {
                    dot.setBackgroundResource(R.drawable.indicator_active); // Drawable for active state
                } else {
                    dot.setBackgroundResource(R.drawable.indicator_inactive);
                }
            }
        }
    }
}
package com.example.fitmeup;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReminderActivity extends AppCompatActivity {

    private LinearLayout reminderContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        // Get the container for the reminders
        reminderContainer = findViewById(R.id.reminderContainer);

        // Floating Action Button for adding reminders
        FloatingActionButton addReminderButton = findViewById(R.id.addReminderButton);

        // Set a click listener on the button to add a new reminder card
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewReminder();
            }
        });
    }

    private void addNewReminder() {
        // Create a new CardView programmatically
        CardView newCard = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(12, 8, 12, 8); // Add margins
        newCard.setLayoutParams(cardParams);
        newCard.setCardElevation(4);
        newCard.setRadius(12);
        newCard.setPadding(16, 16, 16, 16); // Set padding for content

        // Create a LinearLayout to hold the content inside the CardView
        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);

        // Add a TextView for the reminder title that the user can click to edit
        TextView reminderTitleView = new TextView(this);
        reminderTitleView.setText("Reminder Title");
        reminderTitleView.setTextSize(20);
        reminderTitleView.setTextColor(getResources().getColor(R.color.black));
        reminderTitleView.setPadding(0, 8, 0, 8); // Padding for the TextView
        cardContent.addView(reminderTitleView);

        // Create an EditText for the user to type the title (hidden by default)
        EditText reminderTitleEdit = new EditText(this);
        reminderTitleEdit.setHint("Enter Reminder Title");
        reminderTitleEdit.setTextSize(20);
        reminderTitleEdit.setTextColor(getResources().getColor(R.color.black));
        reminderTitleEdit.setVisibility(View.GONE); // Hidden by default
        cardContent.addView(reminderTitleEdit);

        // Add a LinearLayout for the reminder details and switch
        LinearLayout detailsLayout = new LinearLayout(this);
        detailsLayout.setOrientation(LinearLayout.HORIZONTAL);
        detailsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        detailsLayout.setPadding(0, 8, 0, 0); // Add top padding

        // Add an EditText for the reminder details
        EditText reminderDetails = new EditText(this);
        reminderDetails.setHint("Enter Reminder Details");
        reminderDetails.setTextSize(15);
        reminderDetails.setTextColor(getResources().getColor(R.color.black));
        LinearLayout.LayoutParams detailParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        reminderDetails.setLayoutParams(detailParams);
        detailsLayout.addView(reminderDetails);

        // Add a Switch to turn the reminder on/off
        Switch reminderSwitch = new Switch(this);
        detailsLayout.addView(reminderSwitch);

        // Add the details layout to the card content
        cardContent.addView(detailsLayout);

        // Add the card content to the CardView
        newCard.addView(cardContent);

        // Add the new card to the reminder container (the ScrollView content)
        reminderContainer.addView(newCard);

        // Set up the click listener for the title TextView to show the EditText
        reminderTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderTitleView.setVisibility(View.GONE); // Hide the TextView
                reminderTitleEdit.setVisibility(View.VISIBLE); // Show the EditText
                reminderTitleEdit.requestFocus(); // Set focus to the EditText
            }
        });

        // Handle when the user finishes editing the title (e.g., pressing enter or losing focus)
        reminderTitleEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) { // When the EditText loses focus
                    String newTitle = reminderTitleEdit.getText().toString().trim();
                    if (!newTitle.isEmpty()) {
                        reminderTitleView.setText(newTitle);
                    }
                    reminderTitleEdit.setVisibility(View.GONE); // Hide the EditText
                    reminderTitleView.setVisibility(View.VISIBLE); // Show the TextView
                }
            }
        });
    }
}

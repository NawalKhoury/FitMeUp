package com.example.fitmeup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HelpSupportActivity extends AppCompatActivity {

    private Button contactSupportButton;
    private Button faqButton;
    private Button emailSupportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        // Initialize buttons
        contactSupportButton = findViewById(R.id.contact_support_button);
        faqButton = findViewById(R.id.faq_button);
        emailSupportButton = findViewById(R.id.email_support_button);

        // Set click listeners
        contactSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open dialer to call customer support
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:+0547766508")); // Replace with your support phone number
                startActivity(dialIntent);
            }
        });

        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open a webpage with FAQs
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://yourwebsite.com/faqs"));
                startActivity(browserIntent);
            }
        });

        emailSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Example: Send an email to support
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@fitmeapp.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help & Support Request");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Describe your issue here...");
                startActivity(emailIntent);
            }
        });
    }
}

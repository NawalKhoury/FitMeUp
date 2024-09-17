package com.example.fitmeup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class Model_activity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout indicatorLayout;
    private ImageView[] dots;
    private BodyPagerAdapter adapter;
    private ImageButton backButton;
    private ExerciseDao exerciseDao; // Use ExerciseDao

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_model);

        // Initialize ExerciseDao from ExerciseDatabase instead of FitMeUpDatabase
        exerciseDao = ExerciseDatabase.getInstance(this).exerciseDao();

        // Initialize ViewPager, indicator layout, and toolbar buttons
        viewPager = findViewById(R.id.viewPager);
        indicatorLayout = findViewById(R.id.indicatorLayout);

        setupViewPagerAndDots(); // Set up the ViewPager and dots

        // Set up the back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> ToolBarService.navigateToHomeScreen(this));

        // Set up toolbar buttons and their click listeners
        setupToolbarButtons();
    }

    // Method to set up the toolbar buttons
    private void setupToolbarButtons() {
        ImageButton homeButton = findViewById(R.id.toolbar_home);
        ImageButton communityButton = findViewById(R.id.toolbar_handshake);
        ImageButton workoutButton = findViewById(R.id.toolbar_exercise);
        ImageButton modelButton = findViewById(R.id.toolbar_target);
        ImageButton profileButton = findViewById(R.id.toolbar_profile);

        homeButton.setOnClickListener(v -> ToolBarService.navigateToHomeScreen(this));
        communityButton.setOnClickListener(v -> ToolBarService.navigateToCommunityScreen(this));
        workoutButton.setOnClickListener(v -> ToolBarService.navigateToWorkoutScreen(this));
        modelButton.setOnClickListener(v -> ToolBarService.navigateToModelScreen(this));
        profileButton.setOnClickListener(v -> ToolBarService.navigateToProfileScreen(this));
    }

    // Method to set up the ViewPager and the dots indicator
    private void setupViewPagerAndDots() {
        // Initialize the adapter and pass exerciseDao to it
        adapter = new BodyPagerAdapter(this, exerciseDao);
        viewPager.setAdapter(adapter);
        setupIndicatorDots(adapter.getCount());

        // Add a page change listener to update dots when the page changes
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                updateIndicatorDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    // Method to set up the indicator dots
    private void setupIndicatorDots(int count) {
        dots = new ImageView[count];
        indicatorLayout.removeAllViews(); // Clear previous dots if any

        for (int i = 0; i < count; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.indicator_inactive); // Inactive dot drawable
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            indicatorLayout.addView(dots[i], params);
        }

        // Activate the first dot by default
        if (dots.length > 0) {
            dots[0].setImageResource(R.drawable.indicator_active);
        }
    }

    // Method to update the indicator dots when the page changes
    private void updateIndicatorDots(int position) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setImageResource(i == position ? R.drawable.indicator_active : R.drawable.indicator_inactive);
        }
    }
}

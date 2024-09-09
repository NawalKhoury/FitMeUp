package com.example.fitmeup;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class Model_activity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout indicatorLayout;
    private ImageView[] dots;
    private BodyPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_model);

        // Initialize ViewPager, indicator layout, and toolbar buttons
        viewPager = findViewById(R.id.viewPager);
        indicatorLayout = findViewById(R.id.indicatorLayout);


        // Set up adapter for ViewPager
        adapter = new BodyPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Set up indicator dots
        setupIndicatorDots(adapter.getCount());

        // ViewPager page change listener to update dots
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Not used
            }

            @Override
            public void onPageSelected(int position) {
                updateIndicatorDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Not used
            }
        });

        ToolBarService.navigateToHomeScreen(this);
        ToolBarService.navigateToCommunityScreen(this);
        ToolBarService.navigateToWorkoutScreen(this);
        ToolBarService.navigateToModelScreen(this);
        ToolBarService.navigateToProfileScreen(this);



    }

    // Method to initialize the indicator dots
    private void setupIndicatorDots(int count) {
        dots = new ImageView[count];
        indicatorLayout.removeAllViews(); // Clear previous dots if any

        for (int i = 0; i < count; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.indicator_inactive); // Inactive dot drawable
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0); // Spacing between dots
            indicatorLayout.addView(dots[i], params);
        }

        // Set the first dot as active initially
        if (dots.length > 0) {
            dots[0].setImageResource(R.drawable.indicator_active); // Active dot drawable
        }
    }

    // Method to update the active dot
    private void updateIndicatorDots(int position) {
        for (int i = 0; i < dots.length; i++) {
            if (i == position) {
                dots[i].setImageResource(R.drawable.indicator_active); // Active dot drawable
            } else {
                dots[i].setImageResource(R.drawable.indicator_inactive); // Inactive dot drawable
            }
        }
    }
}

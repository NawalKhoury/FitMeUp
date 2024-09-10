package com.example.fitmeup;

import android.content.Context;
import android.content.Intent;

public class ToolBarService {

    public static void navigateToHomeScreen(Context context) {
        Intent intent = new Intent(context, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void navigateToProfileScreen(Context context) {
        // Create an Intent to navigate to the ProfileActivity
        Intent intent = new Intent(context, ProfilePageActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToWorkoutScreen(Context context) {
        // Create an Intent to navigate to the TargetActivity
        Intent intent = new Intent(context, WorkoutActivity.class);
        context.startActivity(intent);
    }
//2
    public static void navigateToCommunityScreen(Context context) {
        // Create an Intent to navigate to the CommunityActivity
        Intent intent = new Intent(context, community_activity.class);
        context.startActivity(intent);
    }
    public static void navigateToModelScreen(Context context) {
        // Create an Intent to navigate to the Model
        Intent intent = new Intent(context, Model_activity.class);
        context.startActivity(intent);
    }

}

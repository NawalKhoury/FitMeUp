package com.example.fitmeup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class BodyPagerAdapter extends PagerAdapter {

    private Context context;
    private ExerciseDao exerciseDao; // Added ExerciseDao as a field

    // Updated constructor to accept ExerciseDao
    public BodyPagerAdapter(Context context, ExerciseDao exerciseDao) {
        this.context = context;
        this.exerciseDao = exerciseDao; // Initialize ExerciseDao
    }

    @Override
    public int getCount() {
        return 2; // Two pages: front and back body models
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;

        if (position == 0) {
            // Inflate front_body.xml layout
            view = LayoutInflater.from(context).inflate(R.layout.front_body, container, false);

            Button forearmButton = view.findViewById(R.id.button_Forearm);
            forearmButton.setOnClickListener(v -> showExerciseOptions("Forearm"));
            Button chestButton = view.findViewById(R.id.chest);
            chestButton.setOnClickListener(v -> showExerciseOptions("chest"));
            Button legsButton = view.findViewById(R.id.button_legs);
            legsButton.setOnClickListener(v -> showExerciseOptions("legs"));
            Button sevenPacksButton = view.findViewById(R.id.button_seven_packs);
            sevenPacksButton.setOnClickListener(v -> showExerciseOptions("seven packs"));
        } else {
            // Inflate back_body.xml layout
            view = LayoutInflater.from(context).inflate(R.layout.back_body, container, false);

            Button backHandButton = view.findViewById(R.id.back_hand);
            backHandButton.setOnClickListener(v -> showExerciseOptions("back hand"));
            Button backButton = view.findViewById(R.id.back);
            backButton.setOnClickListener(v -> showExerciseOptions("back"));
            Button shoulderButton = view.findViewById(R.id.shoulder);
            shoulderButton.setOnClickListener(v -> showExerciseOptions("shoulder"));
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    // Method to show exercise options for a specific muscle group
    private void showExerciseOptions(String muscle) {
        // Exercise details for each muscle group
        String[][] exerciseDetails = new String[0][0];

        // Determine the appropriate drawable based on the muscle or other conditions
        int drawableId = R.drawable.frontbody; // Default to frontbody
        if (muscle.equals("back") || muscle.equals("back hand") || muscle.equals("shoulder")) {
            drawableId = R.drawable.backbody; // Use backbody for back muscles
        }

        // Convert the selected drawable to a byte array
        byte[] imageBytes = ImageUtils.drawableToByteArray(context, drawableId);

        // Setting up exercise details based on the muscle group
        if (muscle.equals("Forearm")) {
            exerciseDetails = new String[][]{
                    {"Standing elbow bend with W bar: 10 reps, 2 sets (15 seconds rest between sets).", "https://www.youtube.com/watch?v=hUXmNnGMotg&t=14s&ab_channel=ONEBODY.co.il"},
                    {"Elbow bend in priest's chair: 10 repetitions, 3 sets (40-50 seconds rest).", "https://www.youtube.com/watch?v=RgN216Cumtw&ab_channel=Bodybuilding.com"},
                    {"Seated Hammers with body tilt: 6-8 reps, 4 sets (drop set).", "https://www.youtube.com/watch?v=zV1qAOpuaL0&embeds_referring_euri=https%3A%2F%2Fwww.onebody.co.il"}
            };
        } else if (muscle.equals("seven packs")) {
            exerciseDetails = new String[][]{
                    {"Body bending while pulling a cable in a cable cross: 10-15 repetitions, 3 sets.", "https://www.youtube.com/watch?v=oA1twhyI8vc&embeds_referring_euri=https%3A%2F%2Fwww.onebody.co.il%2F&source_ve_path=Mjg2NjY"},
                    {"Pulling a cable to the side of the head in a cable cross: 10-15 repetitions, 3 sets.", "https://www.youtube.com/watch?v=AjZ8lnGd-mE&ab_channel=ONEBODY.co.il%7C%D7%95%D7%95%D7%90%D7%9F%D7%91%D7%95%D7%93%D7%99"},
                    {"Leg raises on a bench: 45 seconds, 3 sets.", "https://www.youtube.com/watch?v=RLrz6gA22EU&ab_channel=ONEBODY.co.il%7C%D7%95%D7%95%D7%90%D7%9F%D7%91%D7%95%D7%93%D7%99"}
            };
        }
        else if (muscle.equals("legs")) {
            exerciseDetails = new String[][]{
                    {"Bar squat: 8 reps, 4 sets (60 seconds rest).", "https://www.youtube.com/watch?v=g9xe7PPON6c&ab_channel=ONEBODY.co.il%7C%D7%95%D7%95%D7%90%D7%9F%D7%91%D7%95%D7%93%D7%99"},
                    {"Bulgarian squat: 10 repetitions, 3 sets (40-50 seconds rest and slow descent).", "https://www.youtube.com/watch?v=2C-uNgKwPLE&ab_channel=ScottHermanFitness"},
                    {"Static lunge with dumbbells in hand and front step: 12 repetitions, 3 sets (no rest between legs).", "https://www.youtube.com/watch?v=xY_tL22TeAc&ab_channel=ONEBODY.co.il%7C%D7%95%D7%95%D7%90%D7%9F%D7%91%D7%95%D7%93%D7%99"}
            };
        }
        else if (muscle.equals("chest")) {
            exerciseDetails = new String[][]{
                    {"Upper chest press with a bar: 10 repetitions, 3 sets (40-50 seconds rest)", "https://www.youtube.com/watch?v=SnKUTSxZg_o&ab_channel=ONEBODY.co.il%7C%D7%95%D7%95%D7%90%D7%9F%D7%91%D7%95%D7%93%D7%99"},
                    {"Free weight chest press: 10 repetitions, 3 sets (40-50 seconds rest)", "https://www.youtube.com/watch?v=fnDM2jJ2yeI&ab_channel=ONEBODY.co.il%7C%D7%95%D7%95%D7%90%D7%9F%D7%91%D7%95%D7%93%D7%99"},
                    {"Chest press with a neutral incline bar: 8 reps, 3 sets slow down to the chest and power push (40-50 seconds rest).", "https://www.youtube.com/watch?v=RsobeWfbBcY&ab_channel=Bodybuilding.com"}
            };
        }
        else if (muscle.equals("shoulder")) {
            exerciseDetails = new String[][]{
                    {"Standing seated shoulder press with a bar: 8 reps, 4 sets (40-50 seconds rest)", "https://www.youtube.com/watch?v=-cRm_jHV0xc&ab_channel=ONEBODY.co.il%7C%D7%95%D7%95%D7%90%D7%9F%D7%91%D7%95%D7%93%D7%99"},
                    {"Seated shoulder press with free weights: 8 reps, 3 sets (40-50 seconds rest)", "https://www.youtube.com/watch?v=rBqG3whwYbA&ab_channel=ONEBODY.co.il%7C%D7%95%D7%95%D7%90%D7%9F%D7%91%D7%95%D7%93%D7%99"},
                    {"Shoulder abduction to the sides in cable cross free weights: 6-8 repetitions, 3 sets (drop set)", "https://www.youtube.com/watch?v=Aor7Bq-lhoI&ab_channel=ONEBODY.co.il%7C%D7%95%D7%95%D7%90%D7%9F%D7%91%D7%95%D7%93%D7%99"}
            };
        }
        else if (muscle.equals("back")) {
            exerciseDetails = new String[][]{
                    {"Tension increases: maximum repetitions, 2 sets (one with weight and one without).", "https://www.youtube.com/watch?v=WXMKjV11lAk&ab_channel=Bodybuilding.com"},
                    {"Wide grip upper poly: 8 reps, 4 sets (40-50 seconds rest).", "https://www.youtube.com/watch?v=lueEJGjTuPQ&ab_channel=Bodybuilding.com"},
                    {"Wide lower poly row: 8 reps, 3 sets (40-50 seconds rest to concentrate on shoulder blades).", "https://www.youtube.com/watch?v=YKAeU55CkVk&ab_channel=ScottHermanFitness"}
            };
        }
        else if (muscle.equals("back hand")) {
            exerciseDetails = new String[][]{
                    {"Elbow lunge behind the head with a barbell: 10 repetitions, 3 sets (40-50 seconds rest)", "https://www.youtube.com/watch?v=h82D70i7MPE&embeds_referring_euri=https%3A%2F%2Fwww.onebody.co.il%2F&source_ve_path=Mjg2NjY"},
                    {"Elbow drop in cable cross with rope: 6-8 repetitions, 4 sets (drop set)", "https://www.youtube.com/watch?v=8fm_4LWtgVM&ab_channel=ONEBODY.co.il%7C%D7%95%D7%95%D7%90%D7%9F%D7%91%D7%95%D7%93%D7%99"},
                    {"Jacksons: 6-8 reps, 3 sets.", "https://www.youtube.com/watch?v=VSnskYq3KWo&ab_channel=ONEBODY.co.il%7C%D7%95%D7%95%D7%90%D7%9F%D7%91%D7%95%D7%93%D7%99"}
            };
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.exercise_list_container, null);
        LinearLayout exerciseContainer = dialogView.findViewById(R.id.exercise_container);

        LayoutInflater inflater = LayoutInflater.from(context);

        for (String[] exercise : exerciseDetails) {
            // Create a new Exercise object, including the selected image
            Exercise newExercise = new Exercise(muscle, exercise[0], exercise[1], imageBytes);

            // Insert the new exercise in the database
            new Thread(() -> {
                List<Exercise> existingExercises = exerciseDao.getExerciseByDetails(muscle, exercise[0], exercise[1]);
                if (existingExercises.isEmpty()) {
                    exerciseDao.insertExercise(newExercise);
                }
            }).start();

            // Inflate the exercise_item.xml layout
            View exerciseView = inflater.inflate(R.layout.exercise_name, exerciseContainer, false);

            // Set exercise name
            TextView exerciseName = exerciseView.findViewById(R.id.exercise_name);
            exerciseName.setText(exercise[0]);

            // Set the video link
            TextView videoLink = exerciseView.findViewById(R.id.video_link);
            if (!exercise[1].isEmpty()) {
                videoLink.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(exercise[1]));
                    context.startActivity(intent);
                });
            } else {
                videoLink.setVisibility(View.GONE); // Hide if no video link is provided
            }

            // Add the exerciseView to the exerciseContainer
            exerciseContainer.addView(exerciseView);
        }

        // Show the AlertDialog with the list of exercises
        builder.setView(dialogView)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}

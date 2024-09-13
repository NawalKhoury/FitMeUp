package com.example.fitmeup;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;


public interface WorkoutApi {
    // Get all workouts from the server
    @GET("api/workouts")
    Call<List<Workout>> getAllWorkouts();

    // Get workout by ID from the server
    @GET("api/workouts/{id}")
    Call<Workout> getWorkoutById(@Path("id") long id);

    // Add a new workout to the server
    @POST("api/workouts")
    Call<Workout> createWorkout(@Body Workout workout);
}

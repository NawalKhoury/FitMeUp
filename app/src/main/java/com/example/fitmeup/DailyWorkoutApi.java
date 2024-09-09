package com.example.fitmeup;


import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DailyWorkoutApi {
    @POST("/api/daily-workouts")
    Call<Void> createDailyWorkout(@Body DailyWorkout dailyWorkout);

    @GET("/api/daily-workouts/{id}")
    Call<DailyWorkout> getWorkoutById(@Path("id") int id);

    @GET("/api/daily-workouts")
    Call<List<DailyWorkout>> getAllWorkouts();

    @PUT("/api/daily-workouts")
    Call<Void> updateDailyWorkout(@Body DailyWorkout dailyWorkout);

    @DELETE("/api/daily-workouts/{id}")
    Call<Void> deleteDailyWorkout(@Path("id") int id);

    @GET("/api/daily-workouts/date")
    Call<DailyWorkout> getWorkoutByDate(@Query("date") Date date);
}

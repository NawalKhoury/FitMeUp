package com.example.fitmeup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface RegisterUserApi {
    @POST("/api/users")
    Call<Void> createUser(@Body RegisterUser registerUser);

    @GET("/api/users/login")
    Call<RegisterUser> getUserByUsernameAndPassword(@Query("username") String username, @Query("password") String password);

    @GET("/api/users/security")
    Call<RegisterUser> getUserBySecurityQuestionAndAnswer(@Query("securityQuestion") String securityQuestion, @Query("securityAnswer") String securityAnswer);

    @PUT("/api/users")
    Call<Void> updateUser(@Body RegisterUser registerUser);

    @GET("/api/users")
    Call<List<RegisterUser>> getAllUsers();
}

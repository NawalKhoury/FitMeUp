package com.example.fitmeup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostApi {

    @POST("/api/posts")
    Call<Void> createPost(@Body Post post);

    @GET("/api/posts/{id}")
    Call<Post> getPostById(@Path("id") long id);

    @GET("/api/posts")
    Call<List<Post>> getAllPosts();

    @PUT("/api/posts")
    Call<Void> updatePost(@Body Post post);

    @DELETE("/api/posts/{id}")
    Call<Void> deletePost(@Path("id") long id);

    @GET("/api/posts/search")
    Call<List<Post>> getPostsByKeyword(@Query("keyword") String keyword);
}

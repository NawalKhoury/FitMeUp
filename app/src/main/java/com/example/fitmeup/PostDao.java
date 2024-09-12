package com.example.fitmeup;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {

    // Insert a new Post
    @Insert
    void insert(Post post);

    // Get a Post by ID
    @Query("SELECT * FROM post WHERE id = :id LIMIT 1")
    Post getPostById(int id);

    // Get all Posts
    @Query("SELECT * FROM post ORDER BY date DESC")
    List<Post> getAllPosts();

    // Update an existing Post
    @Update
    void update(Post post);

    // Delete a Post
    @Delete
    void delete(Post post);

    // Get Posts by description keyword
    @Query("SELECT * FROM post WHERE description LIKE '%' || :keyword || '%' ORDER BY date DESC")
    List<Post> getPostsByKeyword(String keyword);
}


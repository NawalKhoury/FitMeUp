package com.example.fitmeup;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface ImageDao {
    @Insert
    void insert(ImageEntity imageEntity);

    @Query("SELECT imageUrl FROM image_user WHERE userId = :id LIMIT 1")
    LiveData<String> getImageById(String id);

}

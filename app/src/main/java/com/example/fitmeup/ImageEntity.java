package com.example.fitmeup;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_user")
public class ImageEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userId;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

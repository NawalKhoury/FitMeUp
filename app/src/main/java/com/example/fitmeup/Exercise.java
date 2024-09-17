package com.example.fitmeup;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String muscle;
    private String description;
    private String videoLink;
    private byte[] image; // New field to store image as a byte array

    // Default constructor required by Room
    public Exercise() {
    }

    // Constructor including the image
    @Ignore
    public Exercise(String muscle, String description, String videoLink, byte[] image) {
        this.muscle = muscle;
        this.description = description;
        this.videoLink = videoLink;
        this.image = image;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

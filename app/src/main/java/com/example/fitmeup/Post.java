package com.example.fitmeup;

import android.content.SharedPreferences;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "post")
public class Post {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date;
    private String description;


    public Post(String description, Date date) {
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }




    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }


}

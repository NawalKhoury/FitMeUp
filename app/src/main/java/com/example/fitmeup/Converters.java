package com.example.fitmeup;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class Converters {

    // Date Converters
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    // ArrayList<Workout> Converters using Gson
    @TypeConverter
    public String fromWorkoutList(ArrayList<Workout> workoutDailyHistory) {
        if (workoutDailyHistory == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Workout>>() {}.getType();
        return gson.toJson(workoutDailyHistory, type);
    }

    @TypeConverter
    public ArrayList<Workout> toWorkoutList(String workoutDailyHistoryString) {
        if (workoutDailyHistoryString == null) {
            return null;
        }
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Workout>>() {}.getType();
            return gson.fromJson(workoutDailyHistoryString, type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();  // Return an empty list or handle the error as necessary
        }
    }
}

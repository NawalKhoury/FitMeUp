import androidx.room.TypeConverter;

import com.example.fitmeup.Workout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public static String fromWorkoutList(ArrayList<Workout> workouts) {
        if (workouts == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Workout>>() {}.getType();
        return gson.toJson(workouts, type);
    }

    @TypeConverter
    public static ArrayList<Workout> toWorkoutList(String workoutString) {
        if (workoutString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Workout>>() {}.getType();
        return gson.fromJson(workoutString, type);
    }
}

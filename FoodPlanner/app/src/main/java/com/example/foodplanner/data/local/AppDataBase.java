package com.example.foodplanner.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.meals.MealDao;

@Database(entities = {Meal.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance = null;
    public abstract MealDao getProductDAO();
    public static AppDataBase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,"mealDb").build();
        }
        return instance;
    }
}

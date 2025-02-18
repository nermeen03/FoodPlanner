package com.example.foodplanner.data.meals;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MealDao {
    @Query("Select * from meals_table where user=:name")
    LiveData<List<Meal>> getAllProducts(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertProduct(Meal product);

    @Delete
    void deleteProduct(Meal product);
}

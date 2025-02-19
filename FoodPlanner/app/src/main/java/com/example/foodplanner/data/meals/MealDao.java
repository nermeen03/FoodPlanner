package com.example.foodplanner.data.meals;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface MealDao {

    @Query("SELECT * FROM meals_table WHERE user = :name")
    Observable<List<Meal>> getAllProducts(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertProduct(Meal product);

    @Delete
    Completable deleteProduct(Meal product);
}

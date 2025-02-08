package com.example.foodplanner.data.local;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.data.meals.Meal;

import java.util.List;

public interface MealsLocalDataSourceInt {
    public void insetProd(Meal meal);
    public void deleteProd(Meal meal);
    public LiveData<List<Meal>> getStoredMeals();
}

package com.example.foodplanner.data.local;

import io.reactivex.rxjava3.core.Observable;

import com.example.foodplanner.data.meals.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface MealsLocalDataSourceInt {
    public void insetProd(Meal meal);
    public void deleteProd(Meal meal);
    public Observable<List<Meal>> getStoredMeals(String name);
}

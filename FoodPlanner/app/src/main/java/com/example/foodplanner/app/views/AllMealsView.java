package com.example.foodplanner.app.views;

import com.example.foodplanner.data.meals.Meal;

import java.util.List;

public interface AllMealsView {
    public void showData(List<Meal> meals);
    public void showError(String error);
    public void onClick(Meal meal);
}

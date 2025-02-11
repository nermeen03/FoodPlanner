package com.example.foodplanner.app.views.viewhelpers;

import com.example.foodplanner.data.meals.Meal;

import java.util.List;

public interface AllMealsView<T> {
    public void showData(List<T> data);
    public void showError(String error);
    public void onClick(Meal meal);
}

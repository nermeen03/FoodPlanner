package com.example.foodplanner.presenter;

import com.example.foodplanner.data.meals.Meal;

public interface Listener {
    public default void onAddClick(Meal meal){}
    public default void onRemoveClick(Meal meal){}
}

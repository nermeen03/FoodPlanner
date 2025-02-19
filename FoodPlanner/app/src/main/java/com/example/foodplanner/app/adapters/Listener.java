package com.example.foodplanner.app.adapters;

import com.example.foodplanner.data.local.plans.MealPlan;
import com.example.foodplanner.data.meals.Meal;

public interface Listener {
    public default void onAddClick(Meal meal) {
    }

    public default void onRemoveClick(Meal meal) {
    }

    default void onRemoveMealPlan(MealPlan mealPlan) {
    }
}


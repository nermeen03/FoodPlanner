package com.example.foodplanner.data.meals;

import java.util.List;

public class MealInfoResponse {
    private List<MealInfo> meals;
    public List<MealInfo> getMeal() {
        return meals;
    }
    public void setMeal(List<MealInfo> meals) {
        this.meals = meals;
    }
}

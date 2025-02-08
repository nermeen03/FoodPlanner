package com.example.foodplanner.data.meals;

import java.util.List;

public class MealResponse {
    private List<Meal> meals;
    public List<Meal> getProducts() {
        return meals;
    }
    public void setProducts(List<Meal> meals) {
        this.meals = meals;
    }
}

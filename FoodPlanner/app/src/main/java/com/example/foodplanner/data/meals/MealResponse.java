package com.example.foodplanner.data.meals;

import java.io.Serializable;
import java.util.List;

public class MealResponse implements Serializable {
    private List<Meal> meals;
    public List<Meal> getProducts() {
        return meals;
    }
    public void setProducts(List<Meal> meals) {
        this.meals = meals;
    }
}

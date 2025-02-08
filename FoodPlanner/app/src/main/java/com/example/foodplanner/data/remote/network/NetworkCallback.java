package com.example.foodplanner.data.remote.network;

import com.example.foodplanner.data.meals.Meal;

import java.util.List;

public interface NetworkCallback {
    public void onSuccessResult(List<Meal> movies);
    public void onFailureResult(String errorMsg);
}

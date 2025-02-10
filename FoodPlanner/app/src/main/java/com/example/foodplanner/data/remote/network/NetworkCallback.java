package com.example.foodplanner.data.remote.network;

import com.example.foodplanner.data.meals.Meal;

import java.util.List;

public interface NetworkCallback<T>{
    public void onSuccessResult(List<T> movies);
    public void onFailureResult(String errorMsg);
}

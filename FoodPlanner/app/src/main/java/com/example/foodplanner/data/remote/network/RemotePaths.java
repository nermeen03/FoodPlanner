package com.example.foodplanner.data.remote.network;

import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.meals.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RemotePaths{
    @GET("search.php?s=")
    Call<MealResponse> getProducts();
}

package com.example.foodplanner.data.remote.network;

import com.example.foodplanner.data.meals.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemotePaths{
    @GET("search.php")
    Call<MealResponse> getProductsByLetter(@Query("f") String name);

    @GET("search.php")
    Call<MealResponse> getProductsByName(@Query("s") String name);

    @GET("list.php?a=list")
    Call<MealResponse> getAreas();

    @GET("list.php?i=list")
    Call<MealResponse> getIngredients();

    @GET("list.php?c=list")
    Call<MealResponse> getCategories();
    @GET("random.php")
    Call<MealResponse> getRecommend();

}

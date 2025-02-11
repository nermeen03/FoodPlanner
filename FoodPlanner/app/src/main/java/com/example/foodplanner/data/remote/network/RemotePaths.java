package com.example.foodplanner.data.remote.network;

import com.example.foodplanner.data.meals.MealInfoResponse;
import com.example.foodplanner.data.meals.MealResponse;
import com.example.foodplanner.data.pojos.CategoriesResponse;
import com.example.foodplanner.data.pojos.CountriesResponse;
import com.example.foodplanner.data.pojos.IngredientResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemotePaths{
    @GET("search.php")
    Call<MealResponse> getProductsByLetter(@Query("f") String name);

    @GET("search.php")
    Call<MealInfoResponse> getProductsByName(@Query("s") String name);

    @GET("list.php?a=list")
    Call<CountriesResponse> getAreas();

    @GET("list.php?i=list")
    Call<IngredientResponse> getIngredients();

    @GET("list.php?c=list")
    Call<CategoriesResponse> getCategories();
    @GET("random.php")
    Call<MealResponse> getRecommend();
    @GET("filter.php")
    Call<MealResponse> filterByIngredient(@Query("i") String name);
    @GET("filter.php")
    Call<MealResponse> filterByCategory(@Query("c") String name);
    @GET("filter.php")
    Call<MealResponse> filterByArea(@Query("a") String name);
}

package com.example.foodplanner.data.remote.network;

import com.example.foodplanner.data.meals.MealInfoResponse;
import com.example.foodplanner.data.meals.MealResponse;
import com.example.foodplanner.data.pojos.CategoriesResponse;
import com.example.foodplanner.data.pojos.CountriesResponse;
import com.example.foodplanner.data.pojos.IngredientResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemotePaths{
    @GET("search.php")
    Observable<MealResponse> getProductsByLetter(@Query("f") String name);

    @GET("search.php")
    Observable<MealInfoResponse> getProductsByName(@Query("s") String name);

    @GET("list.php?a=list")
    Observable<CountriesResponse> getAreas();

    @GET("list.php?i=list")
    Observable<IngredientResponse> getIngredients();

    @GET("list.php?c=list")
    Observable<CategoriesResponse> getCategories();
    @GET("random.php")
    Observable<MealResponse> getRecommend();
    @GET("filter.php")
    Observable<MealResponse> filterByIngredient(@Query("i") String name);
    @GET("filter.php")
    Observable<MealResponse> filterByCategory(@Query("c") String name);
    @GET("filter.php")
    Observable<MealResponse> filterByArea(@Query("a") String name);
}

package com.example.foodplanner.data.repo;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.NetworkCallback;

import java.util.List;

public interface MealRepositoryInt {
    public LiveData<List<Meal>> getProducts();
    public void getProducts(NetworkCallback networkCallback,String type,String name);
    public void insertOneProduct(Meal meal);
    public void getRecommend(NetworkCallback networkCallback);
    public void deleteProduct(Meal meal);
}

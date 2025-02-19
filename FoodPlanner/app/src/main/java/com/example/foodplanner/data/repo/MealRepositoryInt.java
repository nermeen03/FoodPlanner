package com.example.foodplanner.data.repo;

import io.reactivex.rxjava3.core.Observable;

import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.NetworkCallback;

import java.util.List;

public interface MealRepositoryInt {
    public Observable<List<Meal>> getProducts(String name);
    public void getProducts(NetworkCallback networkCallback,String type,String name);
    public void insertOneProduct(Meal meal);
    public void getRecommend(NetworkCallback networkCallback);
    public void deleteProduct(Meal meal);
}

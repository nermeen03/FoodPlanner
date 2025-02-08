package com.example.foodplanner.presenter;

import com.example.foodplanner.app.views.AllMealsView;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.NetworkCallback;
import com.example.foodplanner.data.repo.MealRepositoryInt;

import java.util.List;

public class HomePresenter implements NetworkCallback {
    AllMealsView allProductsView;
    MealRepositoryInt mealRepository;

    public HomePresenter(AllMealsView allProductsView, MealRepositoryInt productRepository) {
        this.allProductsView = allProductsView;
        this.mealRepository = productRepository;
    }
    public void getProducts(){
        mealRepository.getProducts(this);
    }
    public void addFav(Meal meal){
        mealRepository.insertOneProduct(meal);
    }
    @Override
    public void onSuccessResult(List<Meal> meals) {
        allProductsView.showData(meals);
    }

    @Override
    public void onFailureResult(String errorMsg) {
        allProductsView.showError(errorMsg);
    }
}

package com.example.foodplanner.presenter;

import com.example.foodplanner.app.views.viewhelpers.AllMealsView;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.NetworkCallback;
import com.example.foodplanner.data.repo.MealRepositoryInt;

import java.util.List;

public class HomePresenter implements NetworkCallback<Meal> {
    AllMealsView allProductsView;
    MealRepositoryInt mealRepository;

    public HomePresenter(AllMealsView allProductsView, MealRepositoryInt productRepository) {
        this.allProductsView = allProductsView;
        this.mealRepository = productRepository;
    }

    public void getProducts(String type, String name) {
        mealRepository.getProducts(this, type, name);
    }

    public void getRecommend() {
        mealRepository.getRecommend(this);
    }

    public void addFav(Meal meal) {
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

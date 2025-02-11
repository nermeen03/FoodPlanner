package com.example.foodplanner.app.views.viewhelpers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodplanner.data.meals.Meal;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<Meal>> mealsList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Meal>> recommendList = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Meal>> getMealsList() {
        return mealsList;
    }

    public LiveData<List<Meal>> getRecommendList() {
        return recommendList;
    }

    public void updateMealsList(List<Meal> newMeals) {
        List<Meal> currentMeals = mealsList.getValue();
        if (currentMeals == null) {
            currentMeals = new ArrayList<>();
        }
        currentMeals.addAll(newMeals);
        mealsList.setValue(currentMeals);
    }

    public void updateRecommendList(List<Meal> newMeals) {
        List<Meal> currentReco = recommendList.getValue();
        if (currentReco == null) {
            currentReco = new ArrayList<>();
        }
        for (Meal meal : newMeals) {
            if (!currentReco.contains(meal) && currentReco.size() < 5) {
                currentReco.add(meal);
            }
        }
        recommendList.setValue(currentReco);
    }
}

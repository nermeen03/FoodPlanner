package com.example.foodplanner.app.views.viewhelpers;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;

import com.example.foodplanner.data.meals.Meal;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    // BehaviorSubject ensures that the latest list of meals is always emitted to new subscribers
    private final BehaviorSubject<List<Meal>> mealsListSubject = BehaviorSubject.createDefault(new ArrayList<>());
    private final BehaviorSubject<List<Meal>> recommendListSubject = BehaviorSubject.createDefault(new ArrayList<>());

    public Observable<List<Meal>> getMealsList() {
        return mealsListSubject;
    }

    public Observable<List<Meal>> getRecommendList() {
        return recommendListSubject;
    }

    public void updateMealsList(List<Meal> newMeals) {
        // Get the current list or an empty list if it's null
        List<Meal> currentMeals = mealsListSubject.getValue();
        if (currentMeals == null) {
            currentMeals = new ArrayList<>();
        }

        // Add new meals to the current list
        currentMeals.addAll(newMeals);

        // Emit the updated list
        mealsListSubject.onNext(currentMeals);
    }

    public void updateRecommendList(List<Meal> newMeals) {
        // Get the current recommendation list or an empty list if it's null
        List<Meal> currentReco = recommendListSubject.getValue();
        if (currentReco == null) {
            currentReco = new ArrayList<>();
        }

        // Add new meals to the recommendation list, ensuring no duplicates and a max size of 5
        for (Meal meal : newMeals) {
            if (!currentReco.contains(meal) && currentReco.size() < 5) {
                currentReco.add(meal);
            }
        }

        // Emit the updated recommendation list
        recommendListSubject.onNext(currentReco);
    }
}

package com.example.foodplanner.app.views.viewhelpers;

import androidx.lifecycle.ViewModel;

import com.example.foodplanner.data.meals.Meal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class HomeViewModel extends ViewModel {
    private final BehaviorSubject<List<Meal>> mealsListSubject = BehaviorSubject.createDefault(new ArrayList<>());
    private final BehaviorSubject<List<Meal>> recommendListSubject = BehaviorSubject.createDefault(new ArrayList<>());

    public Observable<List<Meal>> getMealsList() {
        return mealsListSubject;
    }

    public Observable<List<Meal>> getRecommendList() {
        return recommendListSubject;
    }

    public void updateMealsList(List<Meal> newMeals) {
        List<Meal> currentMeals = mealsListSubject.getValue();
        if (currentMeals == null) {
            currentMeals = new ArrayList<>();
        }

        currentMeals.addAll(newMeals);
        mealsListSubject.onNext(currentMeals);
    }

    public void updateRecommendList(List<Meal> newMeals) {
        List<Meal> currentReco = recommendListSubject.getValue();
        if (currentReco == null) {
            currentReco = new ArrayList<>();
        }
        for (Meal meal : newMeals) {
            if (!currentReco.contains(meal) && currentReco.size() < 5) {
                currentReco.add(meal);
            }
        }
        recommendListSubject.onNext(currentReco);
    }
}

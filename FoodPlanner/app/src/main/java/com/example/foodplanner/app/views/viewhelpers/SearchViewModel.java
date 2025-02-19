package com.example.foodplanner.app.views.viewhelpers;

import android.util.Log;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import androidx.lifecycle.ViewModel;

import com.example.foodplanner.data.pojos.Data;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private final BehaviorSubject<List<Data>> categoriesList = BehaviorSubject.createDefault(new ArrayList<>());
    private final BehaviorSubject<List<Data>> ingredientsList = BehaviorSubject.createDefault(new ArrayList<>());
    private final BehaviorSubject<List<Data>> countriesList = BehaviorSubject.createDefault(new ArrayList<>());

    private final BehaviorSubject<List<String>> allNames = BehaviorSubject.createDefault(new ArrayList<>());
    private final BehaviorSubject<List<String>> filteredNames = BehaviorSubject.createDefault(new ArrayList<>());

    // Getters for observers
    public Observable<List<Data>> getCategoriesList() {
        return categoriesList;
    }

    public Observable<List<Data>> getIngredientsList() {
        return ingredientsList;
    }

    public Observable<List<Data>> getCountriesList() {
        return countriesList;
    }

    public Observable<List<String>> getAllNames() {
        return allNames;
    }

    public Observable<List<String>> getFilteredNames() {
        return filteredNames;
    }

    // Use onNext to emit new values for each list
    public void updateCategories(List<Data> newCategories) {
        categoriesList.onNext(newCategories);  // Use onNext to emit the new list
    }

    public void updateIngredients(List<Data> newIngredients) {
        ingredientsList.onNext(newIngredients);  // Use onNext to emit the new list
    }

    public void updateCountries(List<Data> newCountries) {
        Log.d("TAG", "updateCountries: update");
        countriesList.onNext(newCountries);  // Use onNext to emit the new list
    }

    public void updateAllNames(List<String> newNames) {
        allNames.onNext(newNames);  // Use onNext to emit the new list of names
    }

    public void updateFilteredNames(List<String> newFiltered) {
        filteredNames.onNext(newFiltered);  // Use onNext to emit the filtered names list
    }
}

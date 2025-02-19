package com.example.foodplanner.app.views.viewhelpers;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.foodplanner.data.pojos.Data;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class SearchViewModel extends ViewModel {

    private final BehaviorSubject<List<Data>> categoriesList = BehaviorSubject.createDefault(new ArrayList<>());
    private final BehaviorSubject<List<Data>> ingredientsList = BehaviorSubject.createDefault(new ArrayList<>());
    private final BehaviorSubject<List<Data>> countriesList = BehaviorSubject.createDefault(new ArrayList<>());

    private final BehaviorSubject<List<String>> allNames = BehaviorSubject.createDefault(new ArrayList<>());
    private final BehaviorSubject<List<String>> filteredNames = BehaviorSubject.createDefault(new ArrayList<>());

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
    public void updateCategories(List<Data> newCategories) {
        categoriesList.onNext(newCategories);
    }

    public void updateIngredients(List<Data> newIngredients) {
        ingredientsList.onNext(newIngredients);
    }

    public void updateCountries(List<Data> newCountries) {
        Log.d("TAG", "updateCountries: update");
        countriesList.onNext(newCountries);
    }

    public void updateAllNames(List<String> newNames) {
        allNames.onNext(newNames);
    }

    public void updateFilteredNames(List<String> newFiltered) {
        filteredNames.onNext(newFiltered);
    }
}

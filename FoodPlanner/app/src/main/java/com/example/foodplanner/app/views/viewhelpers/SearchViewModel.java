package com.example.foodplanner.app.views.viewhelpers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodplanner.app.views.fragments.SearchFragment;
import com.example.foodplanner.data.pojos.Data;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {
    private final MutableLiveData<List<Data>> categoriesList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Data>> ingredientsList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Data>> countriesList = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<List<String>> allNames = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> filteredNames = new MutableLiveData<>(new ArrayList<>());

    // Getters for observers
    public LiveData<List<Data>> getCategoriesList() {
        return categoriesList;
    }

    public LiveData<List<Data>> getIngredientsList() {
        return ingredientsList;
    }

    public LiveData<List<Data>> getCountriesList() {
        return countriesList;
    }

    public LiveData<List<String>> getAllNames() {
        return allNames;
    }

    public LiveData<List<String>> getFilteredNames() {
        return filteredNames;
    }
    public void updateCategories(List<Data> newCategories) {
        List<Data> current = categoriesList.getValue();
        if (current == null) {
            current = new ArrayList<>();
        }
        current.addAll(newCategories);
        categoriesList.setValue(current);
    }

    public void updateIngredients(List<Data> newIngredients) {
        List<Data> current = ingredientsList.getValue();
        if (current == null) {
            current = new ArrayList<>();
        }
        current.addAll(newIngredients);
        ingredientsList.setValue(current);
    }

    public void updateCountries(List<Data> newCountries) {
        List<Data> current = countriesList.getValue();
        if (current == null) {
            current = new ArrayList<>();
        }
        current.addAll(newCountries);
        countriesList.setValue(current);
    }

    public void updateAllNames(List<String> newNames) {
        List<String> current = allNames.getValue();
        if (current == null) {
            current = new ArrayList<>();
        }
        current.addAll(newNames);
        allNames.setValue(current);
    }

    public void updateFilteredNames(List<String> newFiltered) {
        filteredNames.setValue(newFiltered);
    }
}


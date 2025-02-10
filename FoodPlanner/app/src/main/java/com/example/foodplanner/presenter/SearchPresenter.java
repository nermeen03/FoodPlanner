package com.example.foodplanner.presenter;

import com.example.foodplanner.app.views.AllDataView;
import com.example.foodplanner.app.views.AllMealsView;
import com.example.foodplanner.data.pojos.Countries;
import com.example.foodplanner.data.pojos.Data;
import com.example.foodplanner.data.remote.network.NetworkCallback;
import com.example.foodplanner.data.repo.SearchRepositoryInt;

import java.util.List;

public class SearchPresenter implements NetworkCallback<Data> {
    AllDataView allCountries;
    SearchRepositoryInt searchRepositoryInt;

    public SearchPresenter(AllDataView allCountries, SearchRepositoryInt searchRepositoryInt) {
        this.allCountries = allCountries;
        this.searchRepositoryInt = searchRepositoryInt;
    }
    public void getCategories(String type, String name){
        searchRepositoryInt.getCategories(this,type,name);
    }
    public void getIngredient(String type, String name){
        searchRepositoryInt.getIngredient(this,type,name);
    }
    public void getCountries(String type, String name){
        searchRepositoryInt.getCountries(this,type,name);
    }
    @Override
    public void onSuccessResult(List<Data> countries) {
        allCountries.showData(countries);
    }
    @Override
    public void onFailureResult(String errorMsg) {
        allCountries.showError(errorMsg);
    }
}

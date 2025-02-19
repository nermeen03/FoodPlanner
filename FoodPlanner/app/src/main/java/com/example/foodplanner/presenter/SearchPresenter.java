package com.example.foodplanner.presenter;

import com.example.foodplanner.app.views.viewhelpers.AllDataView;
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

    public void getProducts(String type, String name) {
        searchRepositoryInt.getProducts(this, type, name);
    }

    public void getCategories(String type, String name) {
        searchRepositoryInt.getCategories(this, type, name);
    }

    public void getIngredient(String type, String name) {
        searchRepositoryInt.getIngredient(this, type, name);
    }

    public void getCountries(String type, String name) {
        searchRepositoryInt.getCountries(this, type, name);
    }


    @Override
    public void onSuccessResult(List<Data> data) {
        allCountries.showData(data);
    }

    @Override
    public void onFailureResult(String errorMsg) {
        allCountries.showError(errorMsg);
    }
}

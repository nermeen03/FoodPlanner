package com.example.foodplanner.data.repo;

import com.example.foodplanner.data.remote.network.MealRemoteDataSourceInt;
import com.example.foodplanner.data.remote.network.NetworkCallback;

public class SearchRepository implements SearchRepositoryInt {
    private static SearchRepository repo = null;
    MealRemoteDataSourceInt productRemoteDataSource;

    public SearchRepository(MealRemoteDataSourceInt productRemoteDataSource) {
        this.productRemoteDataSource = productRemoteDataSource;
    }

    public static SearchRepository getInstance(MealRemoteDataSourceInt productRemoteDataSource) {
        if (repo == null) {
            repo = new SearchRepository(productRemoteDataSource);
        }
        return repo;
    }

    @Override
    public void getProducts(NetworkCallback networkCallback, String type, String name) {
        productRemoteDataSource.makeNetworkCall(networkCallback, type, name);
    }

    @Override
    public void getCategories(NetworkCallback networkCallback, String type, String name) {
        productRemoteDataSource.makeNetworkCall(networkCallback, type, name);
    }

    @Override
    public void getIngredient(NetworkCallback networkCallback, String type, String name) {
        productRemoteDataSource.makeNetworkCall(networkCallback, type, name);
    }

    @Override
    public void getCountries(NetworkCallback networkCallback, String type, String name) {
        productRemoteDataSource.makeNetworkCall(networkCallback, type, name);
    }
}

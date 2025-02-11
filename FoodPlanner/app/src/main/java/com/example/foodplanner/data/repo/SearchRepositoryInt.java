package com.example.foodplanner.data.repo;

import com.example.foodplanner.data.remote.network.NetworkCallback;

public interface SearchRepositoryInt {
    public void getCategories(NetworkCallback networkCallback, String type, String name);
    public void getIngredient(NetworkCallback networkCallback, String type, String name);
    public void getCountries(NetworkCallback networkCallback, String type, String name);
    public void getProducts(NetworkCallback networkCallback, String type, String name);
}

package com.example.foodplanner.data.repo;

import android.view.View;

import com.example.foodplanner.data.remote.network.NetworkCallback;

public interface RemoteMealRepositoryInt {
    public void getMeal(NetworkCallback networkCallback, String type, String name);
    public void getIngredients(NetworkCallback networkCallback, String type, String name);
    public void getCategories(NetworkCallback networkCallback, String type, String name);
    public void getCountries(NetworkCallback networkCallback, String type, String name);

}

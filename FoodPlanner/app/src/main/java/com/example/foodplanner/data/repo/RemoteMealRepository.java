package com.example.foodplanner.data.repo;

import android.util.Log;

import com.example.foodplanner.data.remote.network.MealRemoteDataSourceInt;
import com.example.foodplanner.data.remote.network.NetworkCallback;

public class RemoteMealRepository implements RemoteMealRepositoryInt{
    MealRemoteDataSourceInt productRemoteDataSource;
    private static RemoteMealRepository repo = null;

    public RemoteMealRepository(MealRemoteDataSourceInt productRemoteDataSource) {
        this.productRemoteDataSource = productRemoteDataSource;
    }
    public static RemoteMealRepository getInstance(MealRemoteDataSourceInt productRemoteDataSource){
        if(repo==null){
            repo = new RemoteMealRepository(productRemoteDataSource);
        }
        return repo;
    }
    @Override
    public void getMeal(NetworkCallback networkCallback, String type, String name) {
        Log.d("TAG", "getMeal: hfhhg"+name);
        productRemoteDataSource.makeNetworkCall(networkCallback,type,name);
    }

    @Override
    public void getIngredients(NetworkCallback networkCallback, String type, String name) {
        productRemoteDataSource.makeNetworkCall(networkCallback,type,name);
    }

    @Override
    public void getCategories(NetworkCallback networkCallback, String type, String name) {
        productRemoteDataSource.makeNetworkCall(networkCallback,type,name);
    }

    @Override
    public void getCountries(NetworkCallback networkCallback, String type, String name) {
        productRemoteDataSource.makeNetworkCall(networkCallback,type,name);
    }
}

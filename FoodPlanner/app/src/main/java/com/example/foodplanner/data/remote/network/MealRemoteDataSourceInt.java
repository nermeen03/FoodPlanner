package com.example.foodplanner.data.remote.network;

public interface MealRemoteDataSourceInt {
    public void makeNetworkCall(NetworkCallback networkCallback,String type,String name);
}

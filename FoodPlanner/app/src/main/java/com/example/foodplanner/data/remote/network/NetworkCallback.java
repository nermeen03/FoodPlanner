package com.example.foodplanner.data.remote.network;

import java.util.List;

public interface NetworkCallback<T>{
    public void onSuccessResult(List<T> movies);
    public void onFailureResult(String errorMsg);
}

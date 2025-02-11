package com.example.foodplanner.data.remote.network;

import android.util.Log;

import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.meals.MealInfoResponse;
import com.example.foodplanner.data.meals.MealResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRemoteDataSource implements MealRemoteDataSourceInt{
    private static final String TAG = "ApiCalling";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private RemotePaths remotePaths;
    private static MealRemoteDataSource mealRemoteDataSource = null;

    private MealRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        remotePaths = retrofit.create(RemotePaths.class);

    }

    public static synchronized MealRemoteDataSource getInstance() {
        if (mealRemoteDataSource == null) {
            mealRemoteDataSource = new MealRemoteDataSource();
        }
        return mealRemoteDataSource;
    }

    public void makeNetworkCall(NetworkCallback networkCallback,String type,String name) {
        Call<MealResponse> call = null;
        if(type.equals("letter")) {
            call = remotePaths.getProductsByLetter(name);
        }else if(type.equals("recommend")){
            call = remotePaths.getRecommend();
        }else if(type.equals("meal")){
            getMeal(networkCallback,type,name);
            return;
        }

        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", new Gson().toJson(response.body()));
                    networkCallback.onSuccessResult(response.body().getProducts());
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.e(TAG, "Network error: ", t);
                networkCallback.onFailureResult(t.getMessage());
            }
        });
    }
    private void getMeal(NetworkCallback networkCallback,String type,String name){
        Call<MealInfoResponse> call = remotePaths.getProductsByName(name);
        call.enqueue(new Callback<MealInfoResponse>() {
            @Override
            public void onResponse(Call<MealInfoResponse> call, Response<MealInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", new Gson().toJson(response.body()));
                    networkCallback.onSuccessResult(response.body().getMeal());
                }
            }

            @Override
            public void onFailure(Call<MealInfoResponse> call, Throwable t) {
                Log.e(TAG, "Network error: ", t);
                networkCallback.onFailureResult(t.getMessage());
            }
        });
    }


}

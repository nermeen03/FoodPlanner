package com.example.foodplanner.data.remote.network;

import android.util.Log;

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
        }else if(type.equals("name")) {
            call = remotePaths.getProductsByName(name);
        }else if(type.equals("countries")) {
            call = remotePaths.getAreas();
        }else if(type.equals("ingredients")) {
            call = remotePaths.getIngredients();
        }else if(type.equals("categories")) {
            call = remotePaths.getCategories();
        }else if(type.equals("recommend")){
            call = remotePaths.getRecommend();
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


}

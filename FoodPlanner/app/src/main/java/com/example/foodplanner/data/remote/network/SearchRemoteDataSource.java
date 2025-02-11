package com.example.foodplanner.data.remote.network;

import android.util.Log;

import com.example.foodplanner.data.meals.MealInfoResponse;
import com.example.foodplanner.data.meals.MealResponse;
import com.example.foodplanner.data.pojos.CategoriesResponse;
import com.example.foodplanner.data.pojos.CountriesResponse;
import com.example.foodplanner.data.pojos.IngredientResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchRemoteDataSource implements MealRemoteDataSourceInt{
    private static final String TAG = "ApiCalling";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private RemotePaths remotePaths;
    private static SearchRemoteDataSource searchRemoteDataSource = null;

    private SearchRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        remotePaths = retrofit.create(RemotePaths.class);

    }

    public static synchronized SearchRemoteDataSource getInstance() {
        if (searchRemoteDataSource == null) {
            searchRemoteDataSource = new SearchRemoteDataSource();
        }
        return searchRemoteDataSource;
    }

    public void makeNetworkCall(NetworkCallback networkCallback,String type,String name) {
        if(type.equals("letter")) {
            getMeals(networkCallback,name);
        }else if(type.equals("countries")) {
            getCountries(networkCallback);
        }else if(type.equals("ingredients")) {
            getIngredients(networkCallback);
        }else if(type.equals("categories")) {
            getCategories(networkCallback);
        }else if(type.equals("name")){
            getMeal(networkCallback,name);
        }

    }
    public void getMeal(NetworkCallback networkCallback,String name) {
        remotePaths.getProductsByName(name).enqueue(new Callback<MealInfoResponse>() {
            @Override
            public void onResponse(Call<MealInfoResponse> call, Response<MealInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", new Gson().toJson(response.body()));
                    networkCallback.onSuccessResult(response.body().getMeal());
                }
            }
            @Override
            public void onFailure(Call<MealInfoResponse> call, Throwable t) {
                networkCallback.onFailureResult(t.getMessage());
            }
        });
    }
    public void getMeals(NetworkCallback networkCallback,String name) {
        remotePaths.getProductsByLetter(name).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", new Gson().toJson(response.body()));
                    networkCallback.onSuccessResult(response.body().getProducts());
                }
            }
            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                networkCallback.onFailureResult(t.getMessage());
            }
        });
    }
    public void getCountries(NetworkCallback networkCallback) {
        remotePaths.getAreas().enqueue(new Callback<CountriesResponse>() {
            @Override
            public void onResponse(Call<CountriesResponse> call, Response<CountriesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", new Gson().toJson(response.body()));
                    networkCallback.onSuccessResult(response.body().getCountries());
                }
            }
            @Override
            public void onFailure(Call<CountriesResponse> call, Throwable t) {
                networkCallback.onFailureResult(t.getMessage());
            }
        });
    }
    public void getIngredients(NetworkCallback networkCallback) {
        remotePaths.getIngredients().enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", new Gson().toJson(response.body()));
                    networkCallback.onSuccessResult(response.body().getIngredients());
                }
            }
            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                networkCallback.onFailureResult(t.getMessage());
            }
        });
    }
    public void getCategories(NetworkCallback networkCallback) {
        remotePaths.getCategories().enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", new Gson().toJson(response.body()));
                    networkCallback.onSuccessResult(response.body().getCategories());
                }
            }
            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                networkCallback.onFailureResult(t.getMessage());
            }
        });
    }



}

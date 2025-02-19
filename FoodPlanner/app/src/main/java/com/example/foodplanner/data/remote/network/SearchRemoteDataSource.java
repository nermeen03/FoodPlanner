package com.example.foodplanner.data.remote.network;


import android.util.Log;

import com.google.gson.Gson;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchRemoteDataSource implements MealRemoteDataSourceInt {
    private static final String TAG = "ApiCalling";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private final RemotePaths remotePaths;
    private static SearchRemoteDataSource searchRemoteDataSource = null;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private SearchRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        remotePaths = retrofit.create(RemotePaths.class);
    }

    public static synchronized SearchRemoteDataSource getInstance() {
        if (searchRemoteDataSource == null) {
            searchRemoteDataSource = new SearchRemoteDataSource();
        }
        return searchRemoteDataSource;
    }

    public void makeNetworkCall(NetworkCallback networkCallback, String type, String name) {
        switch (type) {
            case "letter":
                getMeals(networkCallback, name);
                break;
            case "countries":
                getCountries(networkCallback);
                break;
            case "ingredients":
                getIngredients(networkCallback);
                break;
            case "categories":
                getCategories(networkCallback);
                break;
            case "name":
                getMeal(networkCallback, name);
                break;
        }
    }

    public void getMeal(NetworkCallback networkCallback, String name) {
        disposable.add(remotePaths.getProductsByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            Log.d("API Response", new Gson().toJson(response));
                            networkCallback.onSuccessResult(response.getMeal());
                        },
                        throwable -> {
                            Log.e(TAG, "Network error: ", throwable);
                            networkCallback.onFailureResult(throwable.getMessage());
                        }
                ));
    }

    public void getMeals(NetworkCallback networkCallback, String name) {
        disposable.add(remotePaths.getProductsByLetter(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            Log.d("API Response", new Gson().toJson(response));
                            networkCallback.onSuccessResult(response.getProducts());
                        },
                        throwable -> {
                            Log.e(TAG, "Network error: ", throwable);
                            networkCallback.onFailureResult(throwable.getMessage());
                        }
                ));
    }

    public void getCountries(NetworkCallback networkCallback) {
        disposable.add(remotePaths.getAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            Log.d("API Response", new Gson().toJson(response));
                            networkCallback.onSuccessResult(response.getCountries());
                        },
                        throwable -> {
                            Log.e(TAG, "Network error: ", throwable);
                            networkCallback.onFailureResult(throwable.getMessage());
                        }
                ));
    }

    public void getIngredients(NetworkCallback networkCallback) {
        disposable.add(remotePaths.getIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            Log.d("API Response", new Gson().toJson(response));
                            networkCallback.onSuccessResult(response.getIngredients());
                        },
                        throwable -> {
                            Log.e(TAG, "Network error: ", throwable);
                            networkCallback.onFailureResult(throwable.getMessage());
                        }
                ));
    }

    public void getCategories(NetworkCallback networkCallback) {
        disposable.add(remotePaths.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            Log.d("API Response", new Gson().toJson(response));
                            networkCallback.onSuccessResult(response.getCategories());
                        },
                        throwable -> {
                            Log.e(TAG, "Network error: ", throwable);
                            networkCallback.onFailureResult(throwable.getMessage());
                        }
                ));
    }

}

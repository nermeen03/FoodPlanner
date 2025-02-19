package com.example.foodplanner.data.remote.network;



import android.util.Log;

import com.example.foodplanner.data.meals.MealResponse;
import com.google.gson.Gson;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRemoteDataSource implements MealRemoteDataSourceInt {
    private static final String TAG = "ApiCalling";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private final RemotePaths remotePaths;
    private static MealRemoteDataSource mealRemoteDataSource = null;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private MealRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        remotePaths = retrofit.create(RemotePaths.class);
    }

    public static synchronized MealRemoteDataSource getInstance() {
        if (mealRemoteDataSource == null) {
            mealRemoteDataSource = new MealRemoteDataSource();
        }
        return mealRemoteDataSource;
    }

    public void makeNetworkCall(NetworkCallback networkCallback, String type, String name) {
        Observable<MealResponse> observable = null;

        switch (type) {
            case "letter":
                observable = remotePaths.getProductsByLetter(name);
                break;
            case "recommend":
                observable = remotePaths.getRecommend();
                break;
            case "meal":
                getMeal(networkCallback, name);
                return;
            case "categories":
                getCategories(networkCallback, name);
                return;
            case "countries":
                getCountries(networkCallback, name);
                return;
            case "ingredients":
                getIngredients(networkCallback, name);
                return;
        }

        if (observable != null) {
            disposable.add(observable
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
    }

    private void getMeal(NetworkCallback networkCallback, String name) {
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

    private void getCategories(NetworkCallback networkCallback, String name) {
        disposable.add(remotePaths.filterByCategory(name)
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

    private void getCountries(NetworkCallback networkCallback, String name) {
        disposable.add(remotePaths.filterByArea(name)
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

    private void getIngredients(NetworkCallback networkCallback, String name) {
        disposable.add(remotePaths.filterByIngredient(name)
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
}

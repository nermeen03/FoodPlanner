package com.example.foodplanner.app;

import android.util.Log;

import com.example.foodplanner.app.meals.Meal;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ApiCalling {
    private static final String TAG = "ApiCalling";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private ApiService apiService;
    private static ApiCalling apiCalling = null;

    private ApiCalling() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

    }

    public static synchronized ApiCalling getInstance() {
        if (apiCalling == null) {
            apiCalling = new ApiCalling();
        }
        return apiCalling;
    }

    public void makeNetworkCall(NetworkCallback networkCallback) {
        Call<Meal> call = apiService.getProducts();

        call.enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(Call<Meal> call, Response<Meal> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //here is the json
                    Log.d("API Response", new Gson().toJson(response.body()));
                    networkCallback.onSuccessResult(response.body().getProducts());
                } else {
                    Log.e("API Error", "Response: " + response.errorBody());
                    networkCallback.onFailureResult("Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<Meal> call, Throwable t) {
                Log.e(TAG, "Network error: ", t);
                networkCallback.onFailureResult(t.getMessage());
            }
        });
    }
    interface ApiService {
        @GET("search.php?s=")
        Call<Meal> getProducts();
    }
    public interface NetworkCallback {
        public void onSuccessResult(List<Meal> movies);

        public void onFailureResult(String errorMsg);
    }
}

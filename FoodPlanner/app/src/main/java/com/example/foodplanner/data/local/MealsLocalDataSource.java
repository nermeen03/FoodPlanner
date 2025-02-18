package com.example.foodplanner.data.local;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.meals.MealDao;

import java.util.List;

public class MealsLocalDataSource implements MealsLocalDataSourceInt {
    private MealDao dao;
    private static MealsLocalDataSource local = null;
    private LiveData<List<Meal>> storedMeals;
    private MealsLocalDataSource(Context context){
        AppDataBase db = AppDataBase.getInstance(context.getApplicationContext());
        dao = db.getProductDAO();
    }
    public static MealsLocalDataSource getInstance(Context context){
        if(local==null){
            local = new MealsLocalDataSource(context);
        }
        return local;
    }
    public void insetProd(Meal meal){
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insertProduct(meal);
                Log.d("TAG", "run: fav"+meal);
            }
        }).start();
    }
    public void deleteProd(Meal meal){
        new Thread(() -> {
            dao.deleteProduct(meal);
        }).start();
    }
    public LiveData<List<Meal>> getStoredMeals(String name){
        return dao.getAllProducts(name);
    }

}




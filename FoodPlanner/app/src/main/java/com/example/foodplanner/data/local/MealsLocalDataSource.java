package com.example.foodplanner.data.local;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.rxjava3.core.Observable;

import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.meals.MealDao;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsLocalDataSource implements MealsLocalDataSourceInt {
    private MealDao dao;
    private static MealsLocalDataSource local = null;
    private Observable<List<Meal>> storedMeals;
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
        Disposable disposable = dao.insertProduct(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d("TAG", "insetProd: insert successfully ");
                }, throwable -> {
                    Log.d("TAG", "insetProd: error in insert ");
                });
    }
    public void deleteProd(Meal meal){
        Disposable disposable = dao.deleteProduct(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d("TAG", "deleteProd: delete successfully ");
                }, throwable -> {
                    Log.d("TAG", "deleteProd: error in delete ");
                });

    }
    public Observable<List<Meal>> getStoredMeals(String name){
        return dao.getAllProducts(name);
    }

}




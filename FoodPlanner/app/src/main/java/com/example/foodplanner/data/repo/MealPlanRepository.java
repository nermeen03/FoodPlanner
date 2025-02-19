package com.example.foodplanner.data.repo;


import android.app.Application;
import android.util.Log;

import com.example.foodplanner.data.local.AppDataBase;
import com.example.foodplanner.data.local.plans.MealPlan;
import com.example.foodplanner.data.local.plans.MealPlanDao;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealPlanRepository {
    private MealPlanDao mealPlanDao;
    private Executor executor;

    public MealPlanRepository(Application application) {
        AppDataBase db = AppDataBase.getInstance(application);
        mealPlanDao = db.getMealPlanDao();
        executor = Executors.newSingleThreadExecutor();
    }
    public Observable<List<MealPlan>> getMealPlansForDay(long startTime, long endTime) {
        return mealPlanDao.getMealPlansForDay(startTime, endTime);
    }

    public void insertMealPlanIfNotExists(final MealPlan mealPlan) {
        executor.execute(() -> {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(mealPlan.getDate());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            long startTime = cal.getTimeInMillis();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            long endTime = cal.getTimeInMillis();

            // Check how many meal plans with the same name exist on that day.
            int count = mealPlanDao.countMealPlansForDay(mealPlan.getMealName(), startTime, endTime);
            if (count == 0) {
                Disposable disposable = mealPlanDao.insertMealPlan(mealPlan).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                ()->{
                                    Log.d("TAG", "insertMealPlanIfNotExists: added successfully");
                                }
                        );
            }
        });
    }
    public void deleteMealPlan(final MealPlan mealPlan) {
        Disposable disposable = mealPlanDao.deleteMealPlan(mealPlan).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d("TAG", "deleteMealPlan: delete");
                }, throwable -> {
                    Log.e("TAG", "Error fetching allNames", throwable);
                });;
    }
}
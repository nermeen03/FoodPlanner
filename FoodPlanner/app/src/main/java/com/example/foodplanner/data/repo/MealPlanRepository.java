package com.example.foodplanner.data.repo;


import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

import com.example.foodplanner.app.activity.MainActivity;
import com.example.foodplanner.data.local.AppDataBase;
import com.example.foodplanner.data.local.plans.MealPlan;
import com.example.foodplanner.data.local.plans.MealPlanDao;

public class MealPlanRepository {
    private MealPlanDao mealPlanDao;
    private Executor executor;

    public MealPlanRepository(Application application) {
        AppDataBase db = AppDataBase.getInstance(application);
        mealPlanDao = db.getMealPlanDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<MealPlan>> getMealPlansForWeek(long startDate, long endDate) {
        return mealPlanDao.getMealPlansForWeek(startDate, endDate);
    }
    public LiveData<List<MealPlan>> getMealPlansForDay(long startTime, long endTime) {
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
                mealPlanDao.insertMealPlan(mealPlan);
            }
        });
    }
    public void deleteMealPlan(final MealPlan mealPlan) {
        executor.execute(() -> mealPlanDao.deleteMealPlan(mealPlan));
    }
    public LiveData<List<MealPlan>> getAllMealPlans() {
        return mealPlanDao.getAllMealPlans();
    }
}
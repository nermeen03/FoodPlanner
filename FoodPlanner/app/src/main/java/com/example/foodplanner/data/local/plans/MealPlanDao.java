package com.example.foodplanner.data.local.plans;
import io.reactivex.rxjava3.core.Observable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;


@Dao
public interface MealPlanDao {
    @Query("SELECT * FROM meal_plans")
    Observable<List<MealPlan>> getAllMealPlans();
    @Query("SELECT * FROM meal_plans WHERE date BETWEEN :startDate AND :endDate")
    Observable<List<MealPlan>> getMealPlansForWeek(long startDate, long endDate);

    @Query("SELECT * FROM meal_plans WHERE date BETWEEN :startTime AND :endTime")
    Observable<List<MealPlan>> getMealPlansForDay(long startTime, long endTime);

    @Query("SELECT COUNT(*) FROM meal_plans WHERE mealName = :mealName AND date BETWEEN :startTime AND :endTime")
    int countMealPlansForDay(String mealName, long startTime, long endTime);

    @Insert
    Completable insertMealPlan(MealPlan mealPlan);

    @Update
    Completable updateMealPlan(MealPlan mealPlan);

    @Delete
    Completable deleteMealPlan(MealPlan mealPlan);
}

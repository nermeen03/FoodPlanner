package com.example.foodplanner.data.local.plans;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;


@Dao
public interface MealPlanDao {
    @Query("SELECT * FROM meal_plans")
    LiveData<List<MealPlan>> getAllMealPlans();
    @Query("SELECT * FROM meal_plans WHERE date BETWEEN :startDate AND :endDate")
    LiveData<List<MealPlan>> getMealPlansForWeek(long startDate, long endDate);

    @Query("SELECT * FROM meal_plans WHERE date BETWEEN :startTime AND :endTime")
    LiveData<List<MealPlan>> getMealPlansForDay(long startTime, long endTime);

    @Query("SELECT COUNT(*) FROM meal_plans WHERE mealName = :mealName AND date BETWEEN :startTime AND :endTime")
    int countMealPlansForDay(String mealName, long startTime, long endTime);

    @Insert
    void insertMealPlan(MealPlan mealPlan);

    @Update
    void updateMealPlan(MealPlan mealPlan);

    @Delete
    void deleteMealPlan(MealPlan mealPlan);
}

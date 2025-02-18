package com.example.foodplanner.data.local.plans;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meal_plans")
public class MealPlan {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mealId;
    private String mealName;
    private long date;

    private long calendarEventId;

    public MealPlan(String mealId,String mealName, long date, long calendarEventId) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.date = date;
        this.calendarEventId = calendarEventId;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMealName() { return mealName; }
    public long getDate() { return date; }

    public long getCalendarEventId() {
        return calendarEventId;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }
}

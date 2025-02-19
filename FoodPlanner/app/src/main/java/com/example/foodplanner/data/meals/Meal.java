package com.example.foodplanner.data.meals;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.foodplanner.data.pojos.Data;

import java.io.Serializable;

@Entity(tableName = "meals_table")
public class Meal implements Data, Serializable {

    @PrimaryKey
    @NonNull
    private String idMeal;

    private String strMeal;
    private String strMealThumb;

    private String user;

    public Meal() {
    }

    @Ignore
    public Meal(@NonNull String idMeal, String strMeal, String strMealThumb, String user) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
        this.user = user;
    }

    @NonNull
    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(@NonNull String idMeal) {
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

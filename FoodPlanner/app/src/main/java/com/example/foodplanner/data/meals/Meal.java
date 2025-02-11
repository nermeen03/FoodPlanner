package com.example.foodplanner.data.meals;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
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

    // Default constructor (required for Room)
    public Meal() {}

    // Custom constructor
    public Meal(@NonNull String idMeal, String strMeal, String strMealThumb) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
    }

    // Getter methods
    @NonNull
    public String getIdMeal() {
        return idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }
    //setter


    public void setIdMeal(@NonNull String idMeal) {
        this.idMeal = idMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

}

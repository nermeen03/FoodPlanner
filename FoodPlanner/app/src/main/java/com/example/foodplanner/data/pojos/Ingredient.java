package com.example.foodplanner.data.pojos;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Data{
    @SerializedName("strIngredient")
    private String ingredient;
    private int imageResId;
    public String getIngredient() {
        return ingredient;
    }
    public Ingredient(){}
    public Ingredient(String ingredient, int imageResId) {
        this.ingredient = ingredient;
        this.imageResId = imageResId;
    }

    public int getImageResId() {
        return imageResId;
    }
}

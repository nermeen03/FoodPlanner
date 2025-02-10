package com.example.foodplanner.data.pojos;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Data{
    @SerializedName("strIngredient")
    private String ingredient;

    public String getInfo() {
        return ingredient;
    }

    public void setInfo(String ingredient) {
        this.ingredient = ingredient;
    }
}

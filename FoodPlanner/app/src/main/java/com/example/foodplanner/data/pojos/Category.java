package com.example.foodplanner.data.pojos;

import com.google.gson.annotations.SerializedName;

public class Category implements Data {
    @SerializedName("strCategory")
    private String category;

    public String getIngredient() {
        return category;
    }

    public void setInfo(String category) {
        this.category = category;
    }
}

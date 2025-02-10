package com.example.foodplanner.data.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesResponse {
    @SerializedName("meals")
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }
}

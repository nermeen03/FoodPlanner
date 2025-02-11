package com.example.foodplanner.data.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Countries implements Data{
    @SerializedName("strArea")
    private String area;

    public String getIngredient() {
        return area;
    }

    public void setInfo(String area) {
        this.area = area;
    }
}

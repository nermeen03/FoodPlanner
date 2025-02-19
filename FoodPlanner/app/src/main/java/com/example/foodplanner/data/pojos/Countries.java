package com.example.foodplanner.data.pojos;

import com.google.gson.annotations.SerializedName;


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

package com.example.foodplanner.data.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountriesResponse {
    @SerializedName("meals")
    private List<Countries> countries;

    public List<Countries> getCountries() {
        return countries;
    }
}

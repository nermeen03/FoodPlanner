package com.example.foodplanner.app.navigation;

import com.example.foodplanner.data.pojos.Data;

import java.util.ArrayList;

public interface NavigationHandler {
    void navigateToMealFragment(ArrayList<Data> data);
}

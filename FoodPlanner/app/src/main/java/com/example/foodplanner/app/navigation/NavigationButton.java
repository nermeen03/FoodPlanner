package com.example.foodplanner.app.navigation;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationButton {
    public static void navigationOnClick(BottomNavigationView bottomNavigationView, View view) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.page_1) {
                    Navigation.findNavController(view).navigate(R.id.homeFragment);
                    return true;
                } else if (itemId == R.id.page_2) {
                    Navigation.findNavController(view).navigate(R.id.searchFragment);
                    return true;
                } else if (itemId == R.id.page_3) {
                    Navigation.findNavController(view).navigate(R.id.favoriteFragment);
                    return true;
                } else if (itemId == R.id.page_4) {
                    Navigation.findNavController(view).navigate(R.id.calenderFragment);
                    return true;
                } else if (itemId == R.id.page_5) {
                    // Handle Logout click here
                    return true;
                }
                return false;
            }
        });
    }


}

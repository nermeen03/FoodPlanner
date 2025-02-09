package com.example.foodplanner.app.navigation;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

public class NavigationButton {
    public static void navClicked(View view){
        @SuppressLint("RestrictedApi") BottomNavigationItemView home = view.findViewById(R.id.page_1);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.nav_home);
            }
        });
        @SuppressLint("RestrictedApi") BottomNavigationItemView search = view.findViewById(R.id.page_2);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.searchFragment);
            }
        });
        @SuppressLint("RestrictedApi") BottomNavigationItemView fav = view.findViewById(R.id.page_3);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.favoriteFragment);
            }
        });
        @SuppressLint("RestrictedApi") BottomNavigationItemView cal = view.findViewById(R.id.page_4);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.calenderFragment);
            }
        });

    }
}

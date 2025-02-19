package com.example.foodplanner.app.navigation;

import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.data.user.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationButton {
    public static void navigationOnClick(BottomNavigationView bottomNavigationView, View view) {
        SessionManager sessionManager = new SessionManager(view.getContext());
        NetworkUtils networkUtils = new NetworkUtils(view.getContext(), new NetworkUtils.NetworkStateListener() {
            @Override
            public void onNetworkAvailable() {
                Log.d("TAG", "Network is available");
            }

            @Override
            public void onNetworkLost() {
                Log.d("TAG", "Network is lost");
            }
        });
        networkUtils.registerNetworkCallback();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                String savedUserId = sessionManager.getUserId();
                if (itemId == R.id.page_1) {
                    Navigation.findNavController(view).navigate(R.id.homeFragment);
                    return true;
                } else if (itemId == R.id.page_2) {
                    Navigation.findNavController(view).navigate(R.id.searchFragment);
                    return true;
                } else if (itemId == R.id.page_3) {
                    if ("guest".equals(savedUserId) && networkUtils.isNetworkAvailable(view.getContext())) {
                        showLoginDialog(view);
                    } else if (!"guest".equals(savedUserId)) {
                        Navigation.findNavController(view).navigate(R.id.favoriteFragment);
                    }
                    return true;
                } else if (itemId == R.id.page_4) {
                    if ("guest".equals(savedUserId) && networkUtils.isNetworkAvailable(view.getContext())) {
                        showLoginDialog(view);
                    } else if (!"guest".equals(savedUserId)) {
                        Navigation.findNavController(view).navigate(R.id.calenderFragment);
                    }
                    return true;
                } else if (itemId == R.id.page_5) {
                    if ("guest".equals(savedUserId) && networkUtils.isNetworkAvailable(view.getContext())) {
                        showLoginDialog(view);
                    } else if (!"guest".equals(savedUserId)) {
                        Navigation.findNavController(view).navigate(R.id.profileFragment);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public static void showLoginDialog(View view) {
        new android.app.AlertDialog.Builder(view.getContext())
                .setTitle("Guest User")
                .setMessage("You are currently logged in as a guest. Would you like to log in?")
                .setPositiveButton("Login", (dialog, which) -> {
                    Navigation.findNavController(view).navigate(R.id.signInFragment);
                    BottomNavigationView bottomNavigationView = ((Activity) view.getContext()).findViewById(R.id.bottom_navigation);
                    bottomNavigationView.setVisibility(View.GONE);
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}

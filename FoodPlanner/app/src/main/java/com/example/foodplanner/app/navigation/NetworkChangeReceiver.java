package com.example.foodplanner.app.navigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.foodplanner.app.adapters.Listener;
import com.example.foodplanner.app.views.fragments.HomeFragment;
import com.example.foodplanner.app.views.fragments.SearchFragment;
import com.example.foodplanner.app.views.viewhelpers.AllMealsView;
import com.example.foodplanner.data.meals.Meal;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private ScrollView scrollable;
    private Listener fragment;
    public NetworkChangeReceiver() {
    }

    public NetworkChangeReceiver(ScrollView scrollable,Listener fragment) {
        this.scrollable = scrollable;
        this.fragment = fragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable(context)) {
            scrollable.setVisibility(View.VISIBLE);
            if (fragment != null && fragment instanceof HomeFragment) {
                HomeFragment homeFragment = (HomeFragment)fragment;
                homeFragment.refreshPage();
            }
            if (fragment != null && fragment instanceof SearchFragment) {
                SearchFragment searchFragment = (SearchFragment)fragment;
                searchFragment.refreshPage();
            }
        } else {
            scrollable.setVisibility(View.GONE);
            Toast.makeText(context, "Network disconnected", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            boolean isInternetAllowed = sharedPreferences.getBoolean("InternetAccess", true);

            return networkInfo != null && networkInfo.isConnected() && isInternetAllowed;
        }

        return false;
    }

}


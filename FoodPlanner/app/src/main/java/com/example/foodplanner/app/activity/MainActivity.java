package com.example.foodplanner.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplanner.R;
import com.example.foodplanner.app.navigation.NavigationButton;
import com.example.foodplanner.app.navigation.NetworkUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    private NetworkUtils networkUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        if (bottomNavigationView == null) {
            throw new IllegalStateException("BottomNavigationView not found. Check your layout!");
        }

        View navHostView = findViewById(R.id.nav_host_fragment);
        NavigationButton.navigationOnClick(bottomNavigationView, navHostView);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR},
                    1001);
        }
//        networkUtils = new NetworkUtils(this, new NetworkUtils.NetworkStateListener() {
//            @Override
//            public void onNetworkAvailable() {
//                runOnUiThread(() -> {
//                    Toast.makeText(MainActivity.this, " internet Connection is back", Toast.LENGTH_SHORT).show();
//                });
//            }
//
//            @Override
//            public void onNetworkLost() {
//                runOnUiThread(() -> {
//                    Toast.makeText(MainActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
//                });
//            }
//        });
//
//        networkUtils.registerNetworkCallback();

    }

}
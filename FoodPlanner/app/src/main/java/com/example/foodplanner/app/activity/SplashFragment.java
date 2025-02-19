package com.example.foodplanner.app.activity;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.app.adapters.CardAdapter;
import com.example.foodplanner.app.navigation.NetworkUtils;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.NetworkCallback;
import com.example.foodplanner.data.user.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SplashFragment extends Fragment {
    private ImageView boyImage, foodImage;
    private CardAdapter mealAdapter;

    private List<Meal> productList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        boyImage = view.findViewById(R.id.boyImage);
        foodImage = view.findViewById(R.id.foodImage);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        ObjectAnimator boyAnimator = ObjectAnimator.ofFloat(boyImage, "translationX", 0f, screenWidth); // Boy runs to the right
        boyAnimator.setDuration(3500);
        boyAnimator.setInterpolator(new LinearInterpolator());
        boyAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        boyAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                boyImage.setTranslationX(-boyImage.getWidth());
                boyAnimator.start(); // Restart the animation
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        // Create ObjectAnimator for food's animation
        ObjectAnimator foodAnimator = ObjectAnimator.ofFloat(foodImage, "translationX", 0f, screenWidth); // Food runs to the right
        foodAnimator.setDuration(3000); // Set the duration for the animation
        foodAnimator.setInterpolator(new LinearInterpolator());
        foodAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        foodAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                // Reset the food's position to the left edge
                foodImage.setTranslationX(-foodImage.getWidth());
                foodAnimator.start(); // Restart the animation
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        boyAnimator.start();
        foodAnimator.start();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager sessionManager = new SessionManager(requireContext());

        // Initialize NetworkUtils with a listener
        NetworkUtils networkUtils = new NetworkUtils(requireContext(), new NetworkUtils.NetworkStateListener() {
            @Override
            public void onNetworkAvailable() {
                Log.d("TAG", "Network is available");
            }

            @Override
            public void onNetworkLost() {
                Log.d("TAG", "Network is lost");
            }
        });

        new Handler().postDelayed(() -> {
            if (networkUtils.isNetworkAvailable(requireContext())) {
                String savedUserId = sessionManager.getUserId();
                Log.d("TAG", "onViewCreated: saved" + savedUserId);

                if (savedUserId != null && !savedUserId.isEmpty()) {
                    Log.d("TAG", "User is logged in.");
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment);
                } else {
                    Log.d("TAG", "No saved user. Redirecting to login.");
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_mainFragment);
                }
            } else {
                Log.d("TAG", "No Internet, navigating to HomeFragment anyway.");
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment);
            }
        }, 1000);
    }

}
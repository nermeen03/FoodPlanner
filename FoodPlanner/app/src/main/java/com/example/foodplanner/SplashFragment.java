package com.example.foodplanner;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class SplashFragment extends Fragment {
    private ImageView boyImage, foodImage;

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
                // Reset the boy's position to the left edge
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

        // Start both animations
        boyAnimator.start();
        foodAnimator.start();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide the toolbar when the splash screen is displayed
        if (getActivity() != null) {
            getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        }

        // After 8 seconds, navigate to the home fragment
        new Handler().postDelayed(() -> {
            // Navigate to the next fragment (e.g., Main screen)
            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_mainFragment);

            // Restore the toolbar visibility after navigation
            if (getActivity() != null) {
                getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
            }
        }, 1000); // Delay of 8 seconds
    }
}
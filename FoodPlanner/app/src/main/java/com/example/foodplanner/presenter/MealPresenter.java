package com.example.foodplanner.presenter;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.app.views.fragments.HomeFragmentDirections;
import com.example.foodplanner.app.views.fragments.MealFragment;
import com.example.foodplanner.data.meals.MealInfo;
import com.example.foodplanner.data.pojos.Data;
import com.example.foodplanner.data.remote.network.NetworkCallback;
import com.example.foodplanner.data.repo.RemoteMealRepositoryInt;

import java.util.ArrayList;
import java.util.List;

public class MealPresenter implements NetworkCallback<Data> {
    private MealFragment mealFragment;  // Use MealFragment (or AllMealsView) as the view
    private RemoteMealRepositoryInt remoteMealRepositoryInt;
    View view;
    //private NavigationHandler navigationHandler;

    // Pass MealFragment in the constructor
    public MealPresenter(MealFragment mealFragment, RemoteMealRepositoryInt remoteMealRepositoryInt) {
        this.mealFragment = mealFragment;
        this.remoteMealRepositoryInt = remoteMealRepositoryInt;
    }

    public void getMeal(String type, String name, View view) {
        remoteMealRepositoryInt.getMeal(this, type, name);
        this.view = view;
    }

    @Override
    public void onSuccessResult(List<Data> data) {
        if(view!=null){
            if(Navigation.findNavController(view).getCurrentDestination().getId() == R.id.homeFragment){
                Log.d("TAG", "onSuccessResult: fuuuu");
                MealInfo dataArray = (MealInfo) data.get(0);
                Navigation.findNavController(view).navigate(HomeFragmentDirections.actionHomeFragmentToMealFragment(dataArray));
                //HomeFragmentDirections.actionHomeFragmentToFavoriteFragment();
                mealFragment.showData(data);
            }
        }
    }

    @Override
    public void onFailureResult(String errorMsg) {
        mealFragment.showError(errorMsg);
    }
}

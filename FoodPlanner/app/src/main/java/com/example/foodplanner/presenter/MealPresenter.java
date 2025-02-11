package com.example.foodplanner.presenter;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.app.views.fragments.FavoriteFragmentDirections;
import com.example.foodplanner.app.views.fragments.FilterFragment;
import com.example.foodplanner.app.views.fragments.FilterFragmentDirections;
import com.example.foodplanner.app.views.fragments.HomeFragmentDirections;
import com.example.foodplanner.app.views.fragments.MealFragment;
import com.example.foodplanner.app.views.fragments.SearchFragmentDirections;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.meals.MealInfo;
import com.example.foodplanner.data.meals.MealResponse;
import com.example.foodplanner.data.pojos.Data;
import com.example.foodplanner.data.remote.network.NetworkCallback;
import com.example.foodplanner.data.repo.RemoteMealRepositoryInt;

import java.util.ArrayList;
import java.util.List;

public class MealPresenter implements NetworkCallback<Data> {
    private MealFragment mealFragment;  // Use MealFragment (or AllMealsView) as the view
    private RemoteMealRepositoryInt remoteMealRepositoryInt;
    View view;
    public MealPresenter(MealFragment mealFragment, RemoteMealRepositoryInt remoteMealRepositoryInt) {
        this.mealFragment = mealFragment;
        this.remoteMealRepositoryInt = remoteMealRepositoryInt;
    }

    public void getMeal(String type, String name, View view) {
        remoteMealRepositoryInt.getMeal(this, type, name);
        this.view = view;
    }
    public void getIngredients(String type, String name, View view) {
        remoteMealRepositoryInt.getIngredients(this, type, name);
        this.view = view;
    }
    public void getCategories(String type, String name, View view) {
        remoteMealRepositoryInt.getCategories(this, type, name);
        this.view = view;
    }
    public void getCountries(String type, String name, View view) {
        remoteMealRepositoryInt.getCountries(this, type, name);
        this.view = view;
    }

    @Override
    public void onSuccessResult(List<Data> data) {
        if(view!=null){
            if(Navigation.findNavController(view).getCurrentDestination().getId() == R.id.homeFragment){
                Log.d("TAG", "onSuccessResult: fuuuu");
                MealInfo dataArray = (MealInfo) data.get(0);
                Navigation.findNavController(view).navigate(HomeFragmentDirections.actionHomeFragmentToMealFragment(dataArray));
                mealFragment.showData(data);
            }else if(Navigation.findNavController(view).getCurrentDestination().getId() == R.id.favoriteFragment){
                Log.d("TAG", "onSuccessResult: fuuuu");
                MealInfo dataArray = (MealInfo) data.get(0);
                Navigation.findNavController(view).navigate(FavoriteFragmentDirections.actionFavoriteFragmentToMealFragment(dataArray));
                mealFragment.showData(data);
            }else if(Navigation.findNavController(view).getCurrentDestination().getId() == R.id.searchFragment){
                Log.d("TAG", "onSuccessResult: fuuuu");
                if(data.get(0) instanceof MealInfo) {
                    MealInfo dataArray = (MealInfo) data.get(0);
                    Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToMealFragment(dataArray));
                    mealFragment.showData(data);
                }else if(data.get(0) instanceof Meal){
                    Log.i("TAG", "onSuccessResult: iii"+data);
                    List<Meal> meals = new ArrayList<>();
                    for (Data d : data) {
                        if (d instanceof Meal) {
                            meals.add((Meal) d);
                        }
                    }
                    MealResponse mealResponse = new MealResponse();
                    mealResponse.setProducts(meals);
                    Navigation.findNavController(view).navigate(SearchFragmentDirections.actionSearchFragmentToFilterFragment(mealResponse));
                }else {
                    Log.i("TAG", "onSuccessResult: hoop"+data);
                }
            }else if(Navigation.findNavController(view).getCurrentDestination().getId() == R.id.filterFragment) {
                Log.d("TAG", "onSuccessResult: hoooo"+data);
                MealInfo dataArray = (MealInfo) data.get(0);
                Navigation.findNavController(view).navigate(FilterFragmentDirections.actionFilterFragmentToMealFragment(dataArray));
                mealFragment.showData(data);
            }
        }
    }

    @Override
    public void onFailureResult(String errorMsg) {
        mealFragment.showError(errorMsg);
    }
}

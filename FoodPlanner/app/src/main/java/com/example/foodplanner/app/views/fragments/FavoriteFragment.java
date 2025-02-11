package com.example.foodplanner.app.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.app.adapters.FavAdapter;
import com.example.foodplanner.app.views.viewhelpers.AllMealsView;
import com.example.foodplanner.data.local.MealsLocalDataSource;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.MealRemoteDataSource;
import com.example.foodplanner.data.repo.MealRepository;
import com.example.foodplanner.presenter.FavPresenter;
import com.example.foodplanner.app.adapters.Listener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment implements AllMealsView<Meal>, Listener {
    private RecyclerView recyclerView;
    FavAdapter cardAdapter;
    FavPresenter presenter;
    private LiveData<List<Meal>> mealsList;
    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.fav_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        cardAdapter = new FavAdapter(getContext(), this);
        recyclerView.setAdapter(cardAdapter);

        presenter = new FavPresenter(this, MealRepository.getInstance(MealsLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()));
        mealsList = presenter.getProducts();
        mealsList.observe(getViewLifecycleOwner(), meals -> {
            if (meals != null) {
                cardAdapter.setMeals(meals);
            }
        });

        return view;
    }
    @Override
    public void showData(List<Meal> meals) {
        cardAdapter.setMeals(meals);
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Meal meal) {
        presenter.addFav(meal);
    }
    @Override
    public void onAddClick(Meal meal){
        onClick(meal);
    }
}
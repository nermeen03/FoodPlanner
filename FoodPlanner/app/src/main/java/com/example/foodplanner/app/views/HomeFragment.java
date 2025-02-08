package com.example.foodplanner.app.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.data.local.MealsLocalDataSource;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.MealRemoteDataSource;
import com.example.foodplanner.data.repo.MealRepository;
import com.example.foodplanner.presenter.CardAdapter;
import com.example.foodplanner.presenter.HomePresenter;
import com.example.foodplanner.presenter.Listener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements AllMealsView, Listener {

    private RecyclerView recommend_recyclerView;
    private RecyclerView meal_recyclerView;
    CardAdapter cardAdapter;
    HomePresenter presenter;
    private List<Meal> mealsList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recommend_recyclerView = view.findViewById(R.id.recommend_recyclerView);
        recommend_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        meal_recyclerView = view.findViewById(R.id.meal_recyclerView);
        meal_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        cardAdapter = new CardAdapter(mealsList,getContext(),this);
        presenter = new HomePresenter(this, MealRepository.getInstance(MealsLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()));
        recommend_recyclerView.setAdapter(cardAdapter);
        meal_recyclerView.setAdapter(cardAdapter);
        presenter.getProducts();
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
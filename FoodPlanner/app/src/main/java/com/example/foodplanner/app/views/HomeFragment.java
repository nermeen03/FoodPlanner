package com.example.foodplanner.app.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.app.navigation.NavigationButton;
import com.example.foodplanner.data.local.MealsLocalDataSource;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.MealRemoteDataSource;
import com.example.foodplanner.data.repo.MealRepository;
import com.example.foodplanner.app.adapters.CardAdapter;
import com.example.foodplanner.presenter.HomePresenter;
import com.example.foodplanner.presenter.Listener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements AllMealsView<Meal>, Listener {

    private RecyclerView recommend_recyclerView;
    private RecyclerView meal_recyclerView;
    CardAdapter mealsAdapter;
    CardAdapter recommendAdapter;
    HomePresenter presenter;
    private List<Meal> mealsList = new ArrayList<>();
    private List<Meal> recommendList = new ArrayList<>();
    private List<String> letters;
    private int currentLetterIndex = 0;
    private boolean isLoading = false;
    private boolean isRecommend = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recommend_recyclerView = view.findViewById(R.id.recommend_recyclerView);
        recommend_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        meal_recyclerView = view.findViewById(R.id.meal_recyclerView);
        meal_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mealsAdapter = new CardAdapter(mealsList,getContext(),this);
        recommendAdapter = new CardAdapter(recommendList,getContext(),this);
        presenter = new HomePresenter(this, MealRepository.getInstance(MealsLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()));

        recommend_recyclerView.setAdapter(recommendAdapter);
        meal_recyclerView.setAdapter(mealsAdapter);

        letters = Arrays.asList("a", "b", "c", "d", "e", "f");
        presenter.getProducts("letter","a");
        presenter.getRecommend();

        recyclerListener(meal_recyclerView);

        recyclerListener(recommend_recyclerView);


        return view;
    }

    @Override
    public void showData(List<Meal> meals) {
        isLoading = false;
        isRecommend = false;
        if(meals.size()>1) {
            if (meals != null && !meals.isEmpty()) {
                mealsList.addAll(meals);
                mealsAdapter.notifyDataSetChanged();
            }
        }else{
            for(Meal meal:meals) {
                if (!recommendList.contains(meal) && recommendList.size() < 5) {
                    recommendList.addAll(meals);
                    recommendAdapter.notifyDataSetChanged();
                }
            }
        }
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

    private void recyclerListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager == null) return;

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (recyclerView == meal_recyclerView && !isLoading && (visibleItemCount + firstItemPosition) >= totalItemCount && firstItemPosition >= 0) {
                    loadData();
                }
                else if(!isRecommend && (visibleItemCount + firstItemPosition) >= totalItemCount && firstItemPosition >= 0) {
                    Log.i("TAG", "onScrolled: reco");
                    loadRecommend();

                }
            }
        });
    }

    private void loadData() {
        if (currentLetterIndex < letters.size()) {
            String nextLetter = letters.get(currentLetterIndex);
            isLoading = true;
            presenter.getProducts("letter", nextLetter);
            currentLetterIndex++;
        } else {
            Toast.makeText(getContext(), "No more data to load", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadRecommend() {
        isRecommend = true;
        Log.i("TAG", "loadRecommend: ");
        presenter.getRecommend();
    }
}
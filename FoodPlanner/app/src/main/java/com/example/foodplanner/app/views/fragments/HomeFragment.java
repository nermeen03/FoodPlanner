package com.example.foodplanner.app.views.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.app.views.viewhelpers.AllMealsView;
import com.example.foodplanner.app.views.viewhelpers.HomeViewModel;
import com.example.foodplanner.data.local.MealsLocalDataSource;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.MealRemoteDataSource;
import com.example.foodplanner.data.repo.MealPlanRepository;
import com.example.foodplanner.data.repo.MealRepository;
import com.example.foodplanner.app.adapters.CardAdapter;
import com.example.foodplanner.data.repo.RemoteMealRepository;
import com.example.foodplanner.presenter.HomePresenter;
import com.example.foodplanner.app.adapters.Listener;
import com.example.foodplanner.presenter.MealPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements AllMealsView<Meal>, Listener {
    private HomeViewModel viewModel;
    private RecyclerView recommend_recyclerView;
    private RecyclerView meal_recyclerView;
    CardAdapter mealsAdapter;
    CardAdapter recommendAdapter;
    HomePresenter presenter;
    private List<Meal> mealsList = new ArrayList<>();
    private List<Meal> recommendList = new ArrayList<>();
    private List<String> letters = new ArrayList<>();
    private int currentLetterIndex;
    private boolean isLoading = false;
    private boolean isRecommend = false;
    private MealPlanRepository mealPlanRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        recommend_recyclerView = view.findViewById(R.id.recommend_recyclerView);
        recommend_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        meal_recyclerView = view.findViewById(R.id.meal_recyclerView);
        meal_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        MealPlanRepository repository = new MealPlanRepository(getActivity().getApplication());
        MealFragment mealFragment = new MealFragment();
        MealPresenter mealPresenter = new MealPresenter(
                mealFragment,
                RemoteMealRepository.getInstance(MealRemoteDataSource.getInstance())
        );

        //MealPresenter mealPresenter = new MealPresenter(this, RemoteMealRepository.getInstance(MealRemoteDataSource.getInstance()));
        mealsAdapter = new CardAdapter(mealsList,getContext(),this,repository,mealPresenter,view);
        recommendAdapter = new CardAdapter(recommendList,getContext(),this,repository,mealPresenter,view);
        presenter = new HomePresenter(this, MealRepository.getInstance(MealsLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()));

        recommend_recyclerView.setAdapter(recommendAdapter);
        meal_recyclerView.setAdapter(mealsAdapter);

        recyclerListener(meal_recyclerView);

        recyclerListener(recommend_recyclerView);

        viewModel.getMealsList().observe(getViewLifecycleOwner(), meals -> {
            mealsAdapter.updateData(meals);
        });
        viewModel.getRecommendList().observe(getViewLifecycleOwner(), recommends -> {
            recommendAdapter.updateData(recommends);
        });
        if (viewModel.getMealsList().getValue().isEmpty()) {
            presenter.getProducts("letter", "a");
            presenter.getRecommend();
        }

        return view;
    }


    @Override
    public void showData(List<Meal> meals) {
        isLoading = false;
        isRecommend = false;
        Log.i("TAG", "showData: here "+meals);
        if (meals.size() > 1) {
            if (meals != null && !meals.isEmpty()) {
                viewModel.updateMealsList(meals);
            }
        } else {
            if(meals.get(0) instanceof Meal) {
                viewModel.updateRecommendList(meals);
            }
            else{
                Log.i("TAG", "showData: hahda"+meals);
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
        }
    }
    private void loadRecommend() {
        isRecommend = true;
        Log.i("TAG", "loadRecommend: ");
        presenter.getRecommend();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted – you can now access the calendar
            } else {
                // Permission denied – inform the user that the feature is unavailable.
                Toast.makeText(getContext(), "Calendar permission is required to add meals.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
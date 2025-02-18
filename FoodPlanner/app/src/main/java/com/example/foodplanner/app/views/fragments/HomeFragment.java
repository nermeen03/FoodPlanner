package com.example.foodplanner.app.views.fragments;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.app.navigation.NetworkChangeReceiver;
import com.example.foodplanner.app.register.FirebaseHelper;
import com.example.foodplanner.app.views.viewhelpers.AllMealsView;
import com.example.foodplanner.app.views.viewhelpers.HomeViewModel;
import com.example.foodplanner.data.local.MealsLocalDataSource;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.MealRemoteDataSource;
import com.example.foodplanner.data.repo.MealPlanRepository;
import com.example.foodplanner.data.repo.MealRepository;
import com.example.foodplanner.app.adapters.CardAdapter;
import com.example.foodplanner.data.repo.RemoteMealRepository;
import com.example.foodplanner.presenter.FavPresenter;
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
    private FavPresenter favPresenter;
    private NetworkChangeReceiver networkChangeReceiver;
    private ScrollView scrollable;
    private ProgressBar recLoadingProgress;
    private ProgressBar mealLoadingProgress;

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

        scrollable = view.findViewById(R.id.scrollable);
        recLoadingProgress = view.findViewById(R.id.reco_loading_progress);
        mealLoadingProgress = view.findViewById(R.id.meal_loading_progress);

        networkChangeReceiver = new NetworkChangeReceiver(scrollable,this);

        // Check the network connection initially
        if (!networkChangeReceiver.isNetworkAvailable(getContext())) {
            scrollable.setVisibility(View.GONE);
        } else {
            scrollable.setVisibility(View.VISIBLE);
            recLoadingProgress.setVisibility(View.VISIBLE);
            mealLoadingProgress.setVisibility(View.VISIBLE);
        }

        getActivity().registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        for (char c = 'A'; c <= 'Z'; c++) {
            letters.add(String.valueOf(c));
        }

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
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        String user = firebaseHelper.fetchUserDetails();
        Log.d("TAG", "onCreateView: "+user);
        //MealPresenter mealPresenter = new MealPresenter(this, RemoteMealRepository.getInstance(MealRemoteDataSource.getInstance()));
        favPresenter = new FavPresenter(this, MealRepository.getInstance(MealsLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()));

        mealsAdapter = new CardAdapter(mealsList,getContext(),this,repository,mealPresenter,view, favPresenter.getProducts(user));
        recommendAdapter = new CardAdapter(recommendList,getContext(),this,repository,mealPresenter,view, favPresenter.getProducts(user));
        presenter = new HomePresenter(this, MealRepository.getInstance(MealsLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()));

        recommend_recyclerView.setAdapter(recommendAdapter);
        meal_recyclerView.setAdapter(mealsAdapter);

        recommend_recyclerView.setVisibility(View.GONE);
        meal_recyclerView.setVisibility(View.GONE);

        recyclerListener(meal_recyclerView);

        recyclerListener(recommend_recyclerView);

        viewModel.getMealsList().observe(getViewLifecycleOwner(), meals -> {
            mealsAdapter.updateData(meals);
        });
        viewModel.getRecommendList().observe(getViewLifecycleOwner(), recommends -> {
            recommendAdapter.updateData(recommends);
        });
//        if (viewModel.getMealsList().getValue().isEmpty()&&networkChangeReceiver.isNetworkAvailable(getContext())) {
//            presenter.getProducts("letter", "a");
//            presenter.getRecommend();
//
//        }

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
                meal_recyclerView.setVisibility(View.VISIBLE);
                mealLoadingProgress.setVisibility(View.GONE);
            }
        } else {
            if(meals.get(0) instanceof Meal) {
                viewModel.updateRecommendList(meals);
                recommend_recyclerView.setVisibility(View.VISIBLE);
                recLoadingProgress.setVisibility(View.GONE);
            }
            else{
                Log.i("TAG", "showData: hahda"+meals);
            }
        }
    }

    @Override
    public void showError(String error) {
        //here the visibility
        Toast.makeText(getContext(), "Check your connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddClick(Meal meal){
        presenter.addFav(meal);
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
    @Override
    public void onStop() {
        super.onStop();
        if (networkChangeReceiver != null) {
            getActivity().unregisterReceiver(networkChangeReceiver);
        }
    }
    public void refreshPage() {
        if (networkChangeReceiver.isNetworkAvailable(getContext())) {
            presenter.getProducts("letter", "a");  // Adjust as needed to re-fetch the data
            presenter.getRecommend();
        } else {
            Toast.makeText(getContext(), "No network connection", Toast.LENGTH_SHORT).show();
        }
    }


}
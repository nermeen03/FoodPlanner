package com.example.foodplanner.app.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.app.adapters.CardAdapter;
import com.example.foodplanner.app.register.FirebaseHelper;
import com.example.foodplanner.app.views.viewhelpers.AllMealsView;
import com.example.foodplanner.data.local.MealsLocalDataSource;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.MealRemoteDataSource;
import com.example.foodplanner.data.repo.MealPlanRepository;
import com.example.foodplanner.data.repo.MealRepository;
import com.example.foodplanner.data.repo.RemoteMealRepository;
import com.example.foodplanner.presenter.FavPresenter;
import com.example.foodplanner.app.adapters.Listener;
import com.example.foodplanner.presenter.MealPresenter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment implements AllMealsView<Meal>, Listener {
    private RecyclerView recyclerView;
    CardAdapter cardAdapter;
    FavPresenter presenter;
    private LiveData<List<Meal>> mealsList;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.fav_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        presenter = new FavPresenter(this, MealRepository.getInstance(MealsLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()));
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        String user = firebaseHelper.fetchUserDetails();
        mealsList = presenter.getProducts(user);
        if(mealsList!=null){
            mealsList = firebaseHelper.getFavoriteMeals(user);
        }
        MealPlanRepository repository = new MealPlanRepository(getActivity().getApplication());
        MealFragment mealFragment = new MealFragment();
        MealPresenter mealPresenter = new MealPresenter(
                mealFragment,
                RemoteMealRepository.getInstance(MealRemoteDataSource.getInstance())
        );

        //MealPresenter mealPresenter = new MealPresenter(this, RemoteMealRepository.getInstance(MealRemoteDataSource.getInstance()));
        cardAdapter = new CardAdapter(mealsList,getContext(), this,repository,mealPresenter,view);
        mealsList.observe(getViewLifecycleOwner(), meals -> {
            if (meals != null) {
                cardAdapter.setMeals(meals);
            }
        });
        recyclerView.setAdapter(cardAdapter);
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
    public void onAddClick(Meal meal){
        presenter.addFav(meal);
    }
    @Override
    public void onRemoveClick(Meal meal){
        presenter.removeFav(meal);
    }

}
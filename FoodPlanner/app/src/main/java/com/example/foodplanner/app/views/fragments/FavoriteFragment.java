package com.example.foodplanner.app.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.app.adapters.CardAdapter;
import com.example.foodplanner.app.adapters.Listener;
import com.example.foodplanner.app.register.FirebaseHelper;
import com.example.foodplanner.app.views.viewhelpers.AllMealsView;
import com.example.foodplanner.data.local.MealsLocalDataSource;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.remote.network.MealRemoteDataSource;
import com.example.foodplanner.data.repo.MealPlanRepository;
import com.example.foodplanner.data.repo.MealRepository;
import com.example.foodplanner.data.repo.RemoteMealRepository;
import com.example.foodplanner.presenter.FavPresenter;
import com.example.foodplanner.presenter.MealPresenter;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment implements AllMealsView<Meal>, Listener {
    CardAdapter cardAdapter;
    FavPresenter presenter;
    private RecyclerView recyclerView;
    private Observable<List<Meal>> mealsList;

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
        Disposable disposable = mealsList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> {
                    cardAdapter.setMeals(meals);
                }, throwable -> {
                    Log.e("TAG", "Error fetching meals", throwable);
                });
        if (mealsList == null) {
            Disposable disposable2 = firebaseHelper.getFavoriteMeals(user).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(meals -> {
                        cardAdapter.setMeals(meals);
                    }, throwable -> {
                        Log.e("TAG", "Error fetching mealRemote", throwable);
                    });
        }
        MealPlanRepository repository = new MealPlanRepository(getActivity().getApplication());
        MealFragment mealFragment = new MealFragment();
        MealPresenter mealPresenter = new MealPresenter(
                mealFragment,
                RemoteMealRepository.getInstance(MealRemoteDataSource.getInstance())
        );

        cardAdapter = new CardAdapter(mealsList, getContext(), this, repository, mealPresenter, view);
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
    public void onAddClick(Meal meal) {
        presenter.addFav(meal);
    }

    @Override
    public void onRemoveClick(Meal meal) {
        presenter.removeFav(meal);
    }

}
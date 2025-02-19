package com.example.foodplanner.app.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import io.reactivex.rxjava3.core.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.app.adapters.CardAdapter;
import com.example.foodplanner.app.adapters.Listener;
import com.example.foodplanner.app.register.FirebaseHelper;
import com.example.foodplanner.app.views.viewhelpers.AllMealsView;
import com.example.foodplanner.data.local.MealsLocalDataSource;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.meals.MealInfo;
import com.example.foodplanner.data.meals.MealResponse;
import com.example.foodplanner.data.pojos.Data;
import com.example.foodplanner.data.remote.network.MealRemoteDataSource;
import com.example.foodplanner.data.repo.MealPlanRepository;
import com.example.foodplanner.data.repo.MealRepository;
import com.example.foodplanner.data.repo.RemoteMealRepository;
import com.example.foodplanner.presenter.FavPresenter;
import com.example.foodplanner.presenter.MealPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment implements AllMealsView, Listener{

    private RecyclerView filter_recycler;
    private CardAdapter cardAdapter;
    private List<Meal> dataList;
    private FavPresenter favPresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        FilterFragmentArgs args = FilterFragmentArgs.fromBundle(getArguments());
        MealResponse dataList = args.getFilter();
        List<Meal> mealList = dataList.getProducts();
        Log.i("TAG", "onCreateView: yyy"+dataList);

        MealPlanRepository repository = new MealPlanRepository(getActivity().getApplication());
        MealFragment mealFragment = new MealFragment();
        MealPresenter mealPresenter = new MealPresenter(
                mealFragment,
                RemoteMealRepository.getInstance(MealRemoteDataSource.getInstance())
        );

        filter_recycler = view.findViewById(R.id.filter_recycler);
        filter_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        String user = firebaseHelper.fetchUserDetails();

        favPresenter = new FavPresenter(this, MealRepository.getInstance(MealsLocalDataSource.getInstance(getContext()), MealRemoteDataSource.getInstance()));
        cardAdapter = new CardAdapter(mealList,getContext(),this,repository,mealPresenter,view,favPresenter.getProducts(user));
        filter_recycler.setAdapter(cardAdapter);

        return view;
    }

    @Override
    public void showData(List data) {

    }

    @Override
    public void showError(String error) {

    }
}
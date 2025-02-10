package com.example.foodplanner.app.views;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.app.adapters.SearchAdapter;
import com.example.foodplanner.app.navigation.NavigationButton;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.pojos.CategoriesResponse;
import com.example.foodplanner.data.pojos.Category;
import com.example.foodplanner.data.pojos.Countries;
import com.example.foodplanner.data.pojos.CountriesResponse;
import com.example.foodplanner.data.pojos.Data;
import com.example.foodplanner.data.pojos.Ingredient;
import com.example.foodplanner.data.pojos.IngredientResponse;
import com.example.foodplanner.data.remote.network.SearchRemoteDataSource;
import com.example.foodplanner.data.repo.SearchRepository;
import com.example.foodplanner.presenter.Listener;
import com.example.foodplanner.presenter.SearchPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements AllDataView, Listener {
    RecyclerView country_recycler;
    RecyclerView ingredient_recycler;
    RecyclerView category_recycler;
    SearchAdapter countryAdapter;
    SearchAdapter ingredientAdapter;
    SearchAdapter categoryAdapter;
    List<Data> categoriesList = new ArrayList<>();
    List<Data> ingredientsList = new ArrayList<>();
    List<Data> countriesList = new ArrayList<>();
    SearchPresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        country_recycler = view.findViewById(R.id.country_recycler);
        country_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        setupRecyclerView(country_recycler);
        countryAdapter = new SearchAdapter(countriesList,getContext(),this);
        country_recycler.setAdapter(countryAdapter);

        ingredient_recycler = view.findViewById(R.id.ingredient_recycler);
        ingredient_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        setupRecyclerView(ingredient_recycler);
        ingredientAdapter = new SearchAdapter(ingredientsList,getContext(),this);
        ingredient_recycler.setAdapter(ingredientAdapter);

        category_recycler = view.findViewById(R.id.category_recycler);
        category_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        setupRecyclerView(category_recycler);
        categoryAdapter = new SearchAdapter(categoriesList,getContext(),this);
        category_recycler.setAdapter(categoryAdapter);

        presenter = new SearchPresenter(this, SearchRepository.getInstance(SearchRemoteDataSource.getInstance()));
        presenter.getCategories("categories",null);
        presenter.getIngredient("ingredients",null);
        presenter.getCountries("countries",null);
        return view;
    }

    @Override
    public void showData(List<Data> dataList) {
        if(dataList.get(0) instanceof Ingredient) {
            ingredientsList.addAll(dataList);
            ingredientAdapter.notifyDataSetChanged();
        }else if(dataList.get(0) instanceof Category) {
            categoriesList.addAll(dataList);
            categoryAdapter.notifyDataSetChanged();
        }else if(dataList.get(0) instanceof Countries) {
            countriesList.addAll(dataList);
            countryAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showError(String error) {

    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        int horizontalSpacing = (int) (10 * getResources().getDisplayMetrics().density); // Horizontal spacing (dp to px)
        int verticalSpacing = (int) (5 * getResources().getDisplayMetrics().density); // Vertical spacing (dp to px)
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(horizontalSpacing, verticalSpacing));
    }
    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int horizontalSpacing;
        private final int verticalSpacing;

        public GridSpacingItemDecoration(int horizontalSpacing, int verticalSpacing) {
            this.horizontalSpacing = horizontalSpacing-10;
            this.verticalSpacing = verticalSpacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            // Apply horizontal spacing (left and right)
            outRect.left = horizontalSpacing;
            outRect.right = horizontalSpacing;

            // Apply vertical spacing (top and bottom)
            outRect.top = verticalSpacing;
            outRect.bottom = verticalSpacing;
        }
    }

}
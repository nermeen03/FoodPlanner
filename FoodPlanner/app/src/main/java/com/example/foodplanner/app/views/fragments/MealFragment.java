package com.example.foodplanner.app.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.app.adapters.IngredientAdapter;
import com.example.foodplanner.app.views.viewhelpers.AllMealsView;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.meals.MealInfo;
import com.example.foodplanner.data.pojos.Data;
import com.example.foodplanner.data.pojos.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MealFragment extends Fragment implements AllMealsView<Data> {
    View view ;
    private RecyclerView recyclerView;
    private IngredientAdapter ingredientAdapter;
    private List<Ingredient> ingredientList;
    private ImageView imgFlag, imgMeal;
    private TextView mealName, txtInstructions;

    public MealFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_meal, container, false);
        return view;
    }

    @Override
    public void showData(List<Data> data) {
        MealInfo mealInfo = (MealInfo)data.get(0);
        Log.i("TAG", "Meal data received: " + mealInfo.getIdMeal());
        recyclerView = view.findViewById(R.id.recyclerIngredient);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        imgFlag = view.findViewById(R.id.imgFlag);
        imgMeal = view.findViewById(R.id.imgMeal);
        mealName = view.findViewById(R.id.mealName);
        txtInstructions = view.findViewById(R.id.txtInstructions);
        ingredientList = new ArrayList<>();
        ingredientList.add(new Ingredient("Ingredient 1", R.drawable.ingredient));
        ingredientList.add(new Ingredient("Ingredient 2", R.drawable.ingredient));
        ingredientList.add(new Ingredient("Ingredient 3", R.drawable.ingredient));

        ingredientAdapter = new IngredientAdapter(getContext(), ingredientList);
        recyclerView.setAdapter(ingredientAdapter);
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void onClick(Meal meal) {

    }
}
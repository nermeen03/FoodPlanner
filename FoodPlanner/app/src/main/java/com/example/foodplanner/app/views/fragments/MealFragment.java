package com.example.foodplanner.app.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.app.adapters.HorizontalSpaceItemDecoration;
import com.example.foodplanner.app.adapters.NamesAdapter;
import com.example.foodplanner.app.views.viewhelpers.AllMealsView;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.meals.MealInfo;
import com.example.foodplanner.data.pojos.Data;
import com.example.foodplanner.data.remote.network.MealRemoteDataSource;
import com.example.foodplanner.data.repo.RemoteMealRepository;
import com.example.foodplanner.presenter.MealPresenter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MealFragment extends Fragment implements AllMealsView<Data> {
    View view ;
    private RecyclerView ingredientRecyclerView;
    private NamesAdapter ingredientAdapter;
    private List<String> ingredients,measures;
    private ImageView imgMeal,favImg,calenderImg;
    private TextView txtFlag,mealName, txtInstructions,txtCategory;

    public MealFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_meal, container, false);
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        scrollView.post(() -> scrollView.scrollTo(0, 0));

        MealFragmentArgs args = MealFragmentArgs.fromBundle(getArguments());
        MealInfo dataList = args.getMeal();

        ingredientRecyclerView = view.findViewById(R.id.recyclerIngredient);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ingredientRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(1));
        imgMeal = view.findViewById(R.id.imgMeal);
        favImg = view.findViewById(R.id.favoriteImg);
        calenderImg = view.findViewById(R.id.calendarImg);
        txtFlag = view.findViewById(R.id.txtFlag);
        mealName = view.findViewById(R.id.mealName);
        txtInstructions = view.findViewById(R.id.txtInstructions);
        txtCategory = view.findViewById(R.id.mealCat);

        if (dataList.getStrMealThumb() != null) {
            Glide.with(getContext())
                    .load(dataList.getStrMealThumb())
                    .placeholder(R.drawable.food)
                    .error(R.drawable.food)
                    .into(imgMeal);
        }
        favImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        calenderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txtFlag.setText(dataList.getStrArea());
        mealName.setText(dataList.getStrMeal());
        txtCategory.setText(dataList.getStrCategory());
        ingredients = new ArrayList<>();
        measures = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            try {
                String methodName = "getStrIngredient" + i;
                Method method = dataList.getClass().getMethod(methodName);
                String ingredient = (String) method.invoke(dataList);
                if (ingredient != null && !ingredient.isEmpty()) {
                    ingredients.add(ingredient);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i <= 20; i++) {
            try {
                String methodName = "getStrMeasure" + i;
                Method method = dataList.getClass().getMethod(methodName);
                String ingredient = (String) method.invoke(dataList);
                if (ingredient != null && !ingredient.isEmpty()) {
                    measures.add(ingredient);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        MealPresenter mealPresenter = new MealPresenter(
                this,
                RemoteMealRepository.getInstance(MealRemoteDataSource.getInstance())
        );
        ingredientAdapter = new NamesAdapter(ingredients,mealPresenter);
        ingredientRecyclerView.setAdapter(ingredientAdapter);
        txtInstructions.setText(dataList.getStrInstructions());
        return view;
    }

    @Override
    public void showData(List<Data> data) {
    }

    @Override
    public void showError(String error) {

    }
}
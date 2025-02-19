package com.example.foodplanner.app.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.app.adapters.Listener;
import com.example.foodplanner.app.adapters.MealPlanAdapter;
import com.example.foodplanner.app.views.viewhelpers.CalendarViewModel;
import com.example.foodplanner.data.local.plans.MealPlan;
import com.example.foodplanner.data.pojos.Event;
import com.example.foodplanner.data.repo.MealPlanRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CalenderFragment extends Fragment implements Listener {

    private CalendarViewModel calendarViewModel;
    private RecyclerView recyclerViewMealPlans;
    private MealPlanAdapter mealPlanAdapter;
    private MealPlanRepository mealPlanRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        mealPlanRepository = new MealPlanRepository(requireActivity().getApplication());

        recyclerViewMealPlans = view.findViewById(R.id.recyclerViewMealPlans);
        recyclerViewMealPlans.setLayoutManager(new LinearLayoutManager(getContext()));
        mealPlanAdapter = new MealPlanAdapter(this);
        recyclerViewMealPlans.setAdapter(mealPlanAdapter);

        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        Disposable disposable = Observable.zip(
                        calendarViewModel.getMealPlansForDay(),
                        calendarViewModel.getSelectedDayFormatted(),
                        (mealPlans, selectedDate) -> {
                            return new Pair<>(mealPlans, selectedDate);
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pair -> {
                            mealPlanAdapter.setMealPlans(pair.first, pair.second);
                        },
                        throwable -> {
                            Log.e("CalendarViewModel", "Error", throwable);
                        }
                );




        CalendarView calendarView = view.findViewById(R.id.calendarView);
        if (calendarView != null) {
            calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                Calendar selectedCal = Calendar.getInstance();
                selectedCal.set(year, month, dayOfMonth, 0, 0, 0);
                selectedCal.set(Calendar.MILLISECOND, 0);
                calendarViewModel.setSelectedDay(selectedCal.getTimeInMillis());
            });
        }

        Calendar today = Calendar.getInstance();
        calendarViewModel.setSelectedDay(today.getTimeInMillis());
        return view;
    }


    @Override
    public void onRemoveMealPlan(MealPlan mealPlan) {
        mealPlanRepository.deleteMealPlan(mealPlan);
        mealPlanAdapter.removeMealPlans(mealPlan);
        mealPlanAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Meal plan removed", Toast.LENGTH_SHORT).show();
    }

}

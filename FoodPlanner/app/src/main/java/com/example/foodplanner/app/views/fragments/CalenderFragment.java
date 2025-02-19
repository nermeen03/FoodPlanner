package com.example.foodplanner.app.views.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
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

    private List<Event> calendarEvents = new ArrayList<>();
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

        // Initialize RecyclerView and adapter for meal plans
        recyclerViewMealPlans = view.findViewById(R.id.recyclerViewMealPlans);
        recyclerViewMealPlans.setLayoutManager(new LinearLayoutManager(getContext()));
        mealPlanAdapter = new MealPlanAdapter(this);
        recyclerViewMealPlans.setAdapter(mealPlanAdapter);

        // Initialize your ViewModel
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        // Observe meal plans for the selected day
        Disposable disposable = Observable.zip(
                        calendarViewModel.getMealPlansForDay(),
                        calendarViewModel.getSelectedDayFormatted(),
                        (mealPlans, selectedDate) -> {
                            return new Pair<>(mealPlans, selectedDate);
                        })
                .subscribeOn(Schedulers.io()) // Perform background work
                .observeOn(AndroidSchedulers.mainThread()) // Switch to main thread for UI updates
                .subscribe(
                        pair -> {
                            // Update the adapter with both meal plans and selected date
                            mealPlanAdapter.setMealPlans(pair.first, pair.second);
                        },
                        throwable -> {
                            // Handle any error
                            Log.e("CalendarViewModel", "Error", throwable);
                        }
                );




        // Find the CalendarView (if available in your layout)
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        if (calendarView != null) {
            calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                // Create a Calendar instance for the selected date.
                Calendar selectedCal = Calendar.getInstance();
                selectedCal.set(year, month, dayOfMonth, 0, 0, 0);
                selectedCal.set(Calendar.MILLISECOND, 0);
                // Update the ViewModel with the selected day
                calendarViewModel.setSelectedDay(selectedCal.getTimeInMillis());
            });
        }

        // Optionally, you can set a default day (for example, today)
        Calendar today = Calendar.getInstance();
        calendarViewModel.setSelectedDay(today.getTimeInMillis());

        return view;
    }


    private void loadNativeCalendarEvents() {
        // Calculate the week range
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        long weekStart = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_WEEK, 7);
        long weekEnd = calendar.getTimeInMillis();

//        Disposable disposable = io.reactivex.Single.fromCallable(() -> {
//                    List<Event> events = new ArrayList<>();
//                    Uri eventsUri = CalendarContract.Events.CONTENT_URI;
//                    String[] projection = {
//                            CalendarContract.Events._ID,
//                            CalendarContract.Events.TITLE,
//                            CalendarContract.Events.DTSTART,
//                            CalendarContract.Events.DTEND
//                    };
//
//                    String selection = CalendarContract.Events.DTSTART + " >= ? AND " +
//                            CalendarContract.Events.DTSTART + " <= ?";
//                    String[] selectionArgs = new String[] { String.valueOf(weekStart), String.valueOf(weekEnd) };
//
//                    Cursor cursor = getActivity().getContentResolver().query(
//                            eventsUri,
//                            projection,
//                            selection,
//                            selectionArgs,
//                            CalendarContract.Events.DTSTART + " ASC"
//                    );
//
//                    if (cursor == null) {
//                        return events;
//                    }
//
//                    // Log available column names for debugging
//                    String[] columnNames = cursor.getColumnNames();
//                    for (String col : columnNames) {
//                        Log.d("CalendarFragment", "Returned Column: " + col);
//                    }
//
//                    int idIndex = cursor.getColumnIndex(CalendarContract.Events._ID);
//                    int titleIndex = cursor.getColumnIndex(CalendarContract.Events.TITLE);
//                    int dtStartIndex = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
//
//                    if (idIndex == -1 || titleIndex == -1 || dtStartIndex == -1) {
//                        Log.e("CalendarFragment", "One or more required columns were not found in the cursor.");
//                        cursor.close();
//                        return events;
//                    }
//
//                    while (cursor.moveToNext()) {
//                        long id = cursor.getLong(idIndex);
//                        String title = cursor.getString(titleIndex);
//                        long dtStart = cursor.getLong(dtStartIndex);
//                        events.add(new Event(id, title, dtStart));
//                    }
//                    cursor.close();
//                    return events;
//                })
//                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
//                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
//                .subscribe(events -> {
//                    calendarEvents = events;
//                    // Update your calendar UI if needed.
//                }, throwable -> {
//                    throwable.printStackTrace();
//                });
    }

    @Override
    public void onRemoveMealPlan(MealPlan mealPlan) {
        // Now the repository is initialized, so this should work.
        mealPlanRepository.deleteMealPlan(mealPlan);
        Toast.makeText(getContext(), "Meal plan removed", Toast.LENGTH_SHORT).show();
    }

}

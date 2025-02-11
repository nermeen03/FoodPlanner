package com.example.foodplanner.app.views.viewhelpers;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.foodplanner.data.local.plans.MealPlan;
import com.example.foodplanner.data.repo.MealPlanRepository;

import java.util.Calendar;
import java.util.List;

public class CalendarViewModel extends AndroidViewModel {
    private MealPlanRepository repository;
    private final MutableLiveData<Long> selectedDay = new MutableLiveData<>();
    // LiveData for meal plans for the selected day
    private final LiveData<List<MealPlan>> mealPlansForDay;

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        repository = new MealPlanRepository(application);

        // Calculate the start and end timestamps for the current week
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
//        long weekStart = calendar.getTimeInMillis();
//        calendar.add(Calendar.DAY_OF_WEEK, 7);
//        long weekEnd = calendar.getTimeInMillis();
//
//        mealPlansLiveData = repository.getMealPlansForWeek(weekStart, weekEnd);
        repository = new MealPlanRepository(application);
        mealPlansForDay = Transformations.switchMap(selectedDay, day -> {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(day);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            long startTime = cal.getTimeInMillis();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            long endTime = cal.getTimeInMillis();
            return repository.getMealPlansForDay(startTime, endTime);
        });
    }
    public void setSelectedDay(long dayInMillis) {
        selectedDay.setValue(dayInMillis);
    }

    public LiveData<List<MealPlan>> getMealPlansForDay() {
        return mealPlansForDay;
    }

}

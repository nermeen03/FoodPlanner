package com.example.foodplanner.app.views.viewhelpers;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import com.example.foodplanner.data.local.plans.MealPlan;
import com.example.foodplanner.data.repo.MealPlanRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarViewModel extends AndroidViewModel {
    private MealPlanRepository repository;
    private final BehaviorSubject<Long> selectedDaySubject = BehaviorSubject.create(); // Used instead of MutableLiveData
    private final Observable<List<MealPlan>> mealPlansForDay;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        repository = new MealPlanRepository(application);

        // Observable for meal plans for the selected day
        mealPlansForDay = selectedDaySubject
                .flatMap(day -> {
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
        selectedDaySubject.onNext(dayInMillis); // Update the selected day
    }

    public Observable<Long> getSelectedDay() {
        return selectedDaySubject; // Emit the selected day as an observable
    }

    public Observable<List<MealPlan>> getMealPlansForDay() {
        return mealPlansForDay; // Observable for meal plans for the selected day
    }

    public Observable<String> getSelectedDayFormatted() {
        return selectedDaySubject
                .map(day -> dateFormat.format(day)); // Format the selected day as a string
    }
}

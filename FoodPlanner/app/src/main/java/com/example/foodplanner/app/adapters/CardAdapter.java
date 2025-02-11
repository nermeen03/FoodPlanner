package com.example.foodplanner.app.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.data.local.plans.MealPlan;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.data.repo.MealPlanRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{
    private List<Meal> meals = new ArrayList<>();
    private Context context;
    private Listener listener;
    private final List<String> favoriteMeals = new ArrayList<>();
    private MealPlanRepository mealPlanRepository;
    public CardAdapter(List<Meal> meals, Context context, Listener listener, MealPlanRepository mealPlanRepository) {
        this.meals = meals;
        this.context = context;
        this.listener = listener;
        this.mealPlanRepository = mealPlanRepository; // Initialize the repository field
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.txtName.setText(meal.getStrMeal());
        holder.txtDes.setText(String.format(meal.getIdMeal()));
        if (meal.getStrMealThumb() != null) {
            Glide.with(context)
                    .load(meal.getStrMealThumb())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.img);
        }
        if (favoriteMeals.contains(meal.getIdMeal())) {
            holder.imgFav.setImageResource(R.drawable.checked_fav);
        } else {
            holder.imgFav.setImageResource(R.drawable.favorite);
        }

        holder.imgFav.setOnClickListener(v -> {
            if (!favoriteMeals.contains(meal.getIdMeal())) {
                listener.onAddClick(meal);
                favoriteMeals.add(meal.getIdMeal());
            } else {
                listener.onRemoveClick(meal);
                favoriteMeals.remove(meal.getIdMeal());
            }
            notifyDataSetChanged();
        });
        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeekDialog(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView txtName, txtDes;
        public ImageView imgFav,imgAdd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.meal_img);
            txtName = itemView.findViewById(R.id.meal_name);
            txtDes = itemView.findViewById(R.id.meal_desc);
            imgFav = itemView.findViewById(R.id.imgFav);
            imgAdd = itemView.findViewById(R.id.imgAdd);

        }
    }
    public void updateData(List<Meal> newMeals) {
        meals.clear();
        meals.addAll(newMeals);
        notifyDataSetChanged();
    }
    private void showWeekDialog(Meal meal) {
        // Start with today's date
        Calendar calendar = Calendar.getInstance();

        // Prepare arrays to hold the Calendar objects and labels for each day.
        final Calendar[] weekDays = new Calendar[7];
        String[] dayLabels = new String[7];

        // Create a date formatter (e.g., "Mon, Jan 2")
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("EEE, MMM d", java.util.Locale.getDefault());

        // Populate the arrays with each day starting from today.
        for (int i = 0; i < 7; i++) {
            Calendar day = (Calendar) calendar.clone();
            weekDays[i] = day;
            dayLabels[i] = dateFormat.format(day.getTime());
            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        new androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Select a Day for Your Meal")
                .setItems(dayLabels, new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        // 'which' is the index of the selected day.
                        Calendar selectedDay = weekDays[which];

                        // Set the time for the meal, e.g., 12:00 PM.
                        selectedDay.set(Calendar.HOUR_OF_DAY, 12);
                        selectedDay.set(Calendar.MINUTE, 0);
                        selectedDay.set(Calendar.SECOND, 0);

                        long eventStartTime = selectedDay.getTimeInMillis();
                        // For demonstration, set the event duration to one hour.
                        long eventEndTime = eventStartTime + 60 * 60 * 1000;
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity)context,
                                    new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR},
                                    1001);
                        } else {
                            addMealToCalendar(context, meal.getStrMeal(), eventStartTime, eventEndTime);
                        }
                        MealPlan mealPlan = new MealPlan(meal.getStrMeal(), eventStartTime,-1);

                        // Insert the meal plan into your local database.
                        mealPlanRepository.insertMealPlanIfNotExists(mealPlan);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    public void addMealToCalendar(Context context, String mealName, long startTimeMillis, long endTimeMillis) {
        long calendarId = getDefaultCalendarId(context);
        if (calendarId == -1) {
            Toast.makeText(context, "No calendar available", Toast.LENGTH_SHORT).show();
            return;
        }

        android.content.ContentResolver cr = context.getContentResolver();
        android.content.ContentValues values = new android.content.ContentValues();

        values.put(android.provider.CalendarContract.Events.DTSTART, startTimeMillis);
        values.put(android.provider.CalendarContract.Events.DTEND, endTimeMillis);
        values.put(android.provider.CalendarContract.Events.TITLE, "Meal: " + mealName);
        values.put(android.provider.CalendarContract.Events.DESCRIPTION, "Meal added from Food Planner App");
        values.put(android.provider.CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(android.provider.CalendarContract.Events.EVENT_TIMEZONE, java.util.TimeZone.getDefault().getID());

        android.net.Uri uri = cr.insert(android.provider.CalendarContract.Events.CONTENT_URI, values);
        if (uri != null) {
            long eventId = Long.parseLong(uri.getLastPathSegment());
            // Now, when you create the MealPlan, include the eventId.
            MealPlan mealPlan = new MealPlan(mealName, startTimeMillis, eventId);
            mealPlanRepository.insertMealPlanIfNotExists(mealPlan);
        }
    }
    public long getDefaultCalendarId(Context context) {
        long calendarId = -1;
        android.database.Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(
                    android.provider.CalendarContract.Calendars.CONTENT_URI,
                    new String[]{android.provider.CalendarContract.Calendars._ID},
                    android.provider.CalendarContract.Calendars.VISIBLE + " = 1",
                    null,
                    android.provider.CalendarContract.Calendars._ID + " ASC"
            );
            if (cursor != null && cursor.moveToFirst()) {
                calendarId = cursor.getLong(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return calendarId;
    }



}

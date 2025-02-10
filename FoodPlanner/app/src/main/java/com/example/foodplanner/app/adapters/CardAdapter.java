package com.example.foodplanner.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.data.meals.Meal;
import com.example.foodplanner.presenter.Listener;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{
    private List<Meal> meals;
    private Context context;
    private Listener listener;
    private final List<String> favoriteMeals = new ArrayList<>();

    public CardAdapter(List<Meal> meals, Context context,Listener listener) {
        this.meals = meals;
        this.context = context;
        this.listener = listener;
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
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView txtName, txtDes;
        public ImageView imgFav;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.meal_img);
            txtName = itemView.findViewById(R.id.meal_name);
            txtDes = itemView.findViewById(R.id.meal_desc);
            imgFav = itemView.findViewById(R.id.imgFav);
        }
    }
}

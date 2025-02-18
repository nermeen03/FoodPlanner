package com.example.foodplanner.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodplanner.R;
import com.example.foodplanner.app.register.FirebaseHelper;
import com.example.foodplanner.data.local.plans.MealPlan;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.MealPlanViewHolder> {

    private List<MealPlan> mealPlans = new ArrayList<>();
    private Listener listener;
    FirebaseHelper firebaseHelper;
    public MealPlanAdapter(Listener listener){
        this.listener = listener;
        firebaseHelper = new FirebaseHelper();
    }

    @NonNull
    @Override
    public MealPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_plan, parent, false);
        return new MealPlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanViewHolder holder, int position) {
        MealPlan mealPlan = mealPlans.get(position);
        holder.bind(mealPlan);
        holder.imgRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveMealPlan(mealPlan);
                firebaseHelper.deleteMealFromPlan(firebaseHelper.fetchUserDetails(),mealPlan.getMealId(),mealPlan.getMealName());

            }
        });
    }

    @Override
    public int getItemCount() {
        return mealPlans.size();
    }

    public void setMealPlans(List<MealPlan> mealPlans,String date) {
        this.mealPlans = mealPlans;
        if(mealPlans.isEmpty()){
            firebaseHelper.getMealPlan(firebaseHelper.fetchUserDetails(),date);
        }
        notifyDataSetChanged();
    }

    static class MealPlanViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMealName;
        private ImageView imgRemove;

        public MealPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMealName = itemView.findViewById(R.id.txtMealName);
            imgRemove = itemView.findViewById(R.id.imgRemove);
        }

        public void bind(MealPlan mealPlan) {
            textViewMealName.setText(mealPlan.getMealName());
        }
    }
}

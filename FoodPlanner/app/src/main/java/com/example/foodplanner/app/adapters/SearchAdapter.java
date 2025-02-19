package com.example.foodplanner.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.data.pojos.Data;
import com.example.foodplanner.presenter.MealPresenter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    RecyclerView recyclerView;
    private List<Data> dataList;
    private Context context;
    private Listener listener;
    private MealPresenter mealPresenter;

    public SearchAdapter(List<Data> dataList, Context context, Listener listener, MealPresenter mealPresenter) {
        this.dataList = dataList;
        this.context = context;
        this.listener = listener;
        this.mealPresenter = mealPresenter;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_view, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        Data data = dataList.get(position);
        holder.btn.setText(data.getIngredient());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateData(List<Data> newNames) {
        dataList.clear();
        dataList.addAll(newNames);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MaterialButton btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn_country);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerView != null) {
                        int recyclerViewId = recyclerView.getId();
                        String recyclerViewName = recyclerView.getResources().getResourceEntryName(recyclerViewId);
                        if (recyclerViewName.equals("category_recycler")) {
                            mealPresenter.getCategories("categories", btn.getText().toString(), itemView);
                        } else if (recyclerViewName.equals("ingredient_recycler")) {
                            mealPresenter.getIngredients("ingredients", btn.getText().toString(), itemView);
                        } else if (recyclerViewName.equals("country_recycler")) {
                            mealPresenter.getCountries("countries", btn.getText().toString(), itemView);
                        }
                    }
                }
            });
        }
    }
}

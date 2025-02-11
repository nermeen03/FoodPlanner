package com.example.foodplanner.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NamesAdapter extends RecyclerView.Adapter<NamesAdapter.NameViewHolder> {

    private List<String> namesList;

    public NamesAdapter(List<String> namesList) {
        this.namesList = namesList;
    }

    @NonNull
    @Override
    public NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new NameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
        String name = namesList.get(position);
        holder.nameTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        return namesList.size();
    }

    public static class NameViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(android.R.id.text1);
        }
    }
    public void updateData(List<String> newNames) {
        namesList.clear();
        namesList.addAll(newNames);
        notifyDataSetChanged();
    }
}


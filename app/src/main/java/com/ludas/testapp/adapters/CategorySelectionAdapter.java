package com.ludas.testapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ludas.testapp.R;
import com.ludas.testapp.database.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategorySelectionAdapter extends RecyclerView.Adapter<CategorySelectionAdapter.ViewHolder> {

    private List<Category> categories;
    private final Set<Long> selectedIds;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final TextView tvName;
        protected final View viewColor;
        protected final CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.category_selection_item_name);
            viewColor = view.findViewById(R.id.category_selection_item_color);
            checkBox = view.findViewById(R.id.category_selection_item_checkbox);
        }
    }

    public CategorySelectionAdapter(List<Category> categories, List<Long> initiallySelectedIds) {
        this.categories = categories;
        this.selectedIds = new HashSet<>(initiallySelectedIds);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public List<Long> getSelectedIds() {
        return new ArrayList<>(selectedIds);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_category_selection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvName.setText(category.getName());
        
        try {
            holder.viewColor.setBackgroundColor(Color.parseColor(category.getColor()));
        } catch (Exception e) {
            holder.viewColor.setBackgroundColor(Color.BLACK);
        }

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedIds.contains(category.getId_category()));

        View.OnClickListener listener = v -> {
            boolean isChecked = !holder.checkBox.isChecked();
            holder.checkBox.setChecked(isChecked);
            if (isChecked) {
                selectedIds.add(category.getId_category());
            } else {
                selectedIds.remove(category.getId_category());
            }
        };

        holder.itemView.setOnClickListener(listener);
        holder.checkBox.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}

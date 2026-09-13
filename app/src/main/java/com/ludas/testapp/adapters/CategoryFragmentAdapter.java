package com.ludas.testapp.adapters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Category;
import com.ludas.testapp.database.CategoryDao;

import java.util.List;

public class CategoryFragmentAdapter extends RecyclerView.Adapter<CategoryFragmentAdapter.ViewHolder> {

    private List<Category> categories;
    private AppDatabase database;
    private CategoryDao categoryDao;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final TextView recId;
        protected final TextView recName;
        protected final ImageButton recDelete;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            recId = (TextView) view.findViewById(R.id.category_recview_id);
            recName = (TextView) view.findViewById(R.id.category_recview_name);
            recDelete = (ImageButton) view.findViewById(R.id.category_recview_delete);
        }
    }

    public interface OnListClicked {
        void onCategorySelected(long categoryId);
    }

    public CategoryFragmentAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_category, viewGroup, false);
        database = AppDatabase.getInstance(view.getContext());
        categoryDao = database.categoryDao();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Category category = categories.get(position);
        viewHolder.recId.setText(String.valueOf(category.getId_category()));
        viewHolder.recName.setText(categories.get(position).getName());
        viewHolder.recDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle(category.getName())
                        .setMessage(R.string.category_info_delete_confirmation)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                categoryDao.delete(category);
                                int pos = viewHolder.getAbsoluteAdapterPosition();
                                categories.remove(pos);
                                notifyItemRemoved(pos);
                            }})
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    OnListClicked listener = (OnListClicked) view.getContext();
                    listener.onCategorySelected(category.getId_category());
                }
                catch (ClassCastException e) {
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}

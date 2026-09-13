package com.ludas.testapp.adapters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import com.ludas.testapp.database.CategoryDao;
import com.ludas.testapp.database.Product;
import com.ludas.testapp.database.ProductDao;

import java.util.List;

public class ProductFragmentAdapter extends RecyclerView.Adapter<ProductFragmentAdapter.ViewHolder> {

    private List<Product> products;
    private AppDatabase database;
    private ProductDao productDao;
    private CategoryDao categoryDao;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final TextView recName, recDescription, recPrice, recCategory;
        protected final ImageButton recDelete;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            recName = (TextView) view.findViewById(R.id.product_recview_name);
            recDescription = (TextView) view.findViewById(R.id.product_recview_description);
            recPrice = (TextView) view.findViewById(R.id.product_recview_price);
            recCategory = (TextView) view.findViewById(R.id.product_recview_category);
            recDelete = (ImageButton) view.findViewById(R.id.product_recview_delete);
        }
    }

    public interface OnListClicked {
        void onProductSelected(long productId);
    }

    public ProductFragmentAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_product, viewGroup, false);
        database = AppDatabase.getInstance(view.getContext());
        productDao = database.productDao();
        categoryDao = database.categoryDao();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Product product = products.get(position);
        viewHolder.recName.setText(product.getName());
        viewHolder.recDescription.setText(product.getDescription());
        viewHolder.recPrice.setText(String.valueOf(product.getPrice()));
        viewHolder.recCategory.setText(categoryDao.findById(product.getId_category()).getName());


        viewHolder.recDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle(product.getName())
                        .setMessage(R.string.product_info_delete_confirmation)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                productDao.delete(product);
                                int pos = viewHolder.getAbsoluteAdapterPosition();
                                products.remove(pos);
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
                    listener.onProductSelected(product.getId_product());
                }
                catch (ClassCastException e) {
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}

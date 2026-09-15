package com.ludas.testapp.adapters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Category;
import com.ludas.testapp.database.CategoryDao;
import com.ludas.testapp.database.Product;
import com.ludas.testapp.database.ProductCategoryDao;
import com.ludas.testapp.database.ProductDao;
import com.ludas.testapp.database.StorageDao;

import java.util.List;

public class ProductFragmentAdapter extends RecyclerView.Adapter<ProductFragmentAdapter.ViewHolder> {

    private List<Product> products;
    private AppDatabase database;
    private ProductDao productDao;
    private ProductCategoryDao productCategoryDao;
    private StorageDao storageDao;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final TextView recName, recDescription, recPrice;
        protected final ImageButton recDelete;
        protected final ChipGroup chipGroup;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            recName = (TextView) view.findViewById(R.id.product_recview_name);
            recDescription = (TextView) view.findViewById(R.id.product_recview_description);
            recPrice = (TextView) view.findViewById(R.id.product_recview_price);
            recDelete = (ImageButton) view.findViewById(R.id.product_recview_delete);
            chipGroup = view.findViewById(R.id.product_recview_chipgroup);
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
        productCategoryDao = database.productCategoryDao();
        storageDao = database.storageDao();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Product product = products.get(position);
        viewHolder.recName.setText(product.getName());
        viewHolder.recDescription.setText(product.getDescription());
        viewHolder.recPrice.setText(String.format(java.util.Locale.getDefault(), "R$ %.2f", product.getPrice()));
        
        List<Category> categories = productCategoryDao.getCategoriesForProduct(product.getId_product());
        viewHolder.chipGroup.removeAllViews();
        
        LayoutInflater inflater = LayoutInflater.from(viewHolder.itemView.getContext());
        for (Category cat : categories) {
            Chip chip = (Chip) inflater.inflate(R.layout.chip_category, viewHolder.chipGroup, false);
            chip.setText(cat.getName());
            try {
                int color = Color.parseColor(cat.getColor());
                // Create a semi-transparent version for the background (alpha 40/255)
                int alphaColor = Color.argb(40, Color.red(color), Color.green(color), Color.blue(color));
                
                chip.setChipBackgroundColor(ColorStateList.valueOf(alphaColor));
                chip.setChipStrokeColor(ColorStateList.valueOf(color));
                chip.setChipIconTint(ColorStateList.valueOf(color));
                chip.setTextColor(color);
            } catch (Exception e) {
                int defaultColor = Color.GRAY;
                chip.setChipIconTint(ColorStateList.valueOf(defaultColor));
            }
            viewHolder.chipGroup.addView(chip);
        }


        viewHolder.recDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int storageCount = storageDao.countByProductId(product.getId_product());
                if (storageCount > 0) {
                    new MaterialAlertDialogBuilder(view.getContext())
                            .setTitle(product.getName())
                            .setMessage(R.string.error_delete_product_has_storage)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                    return;
                }

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

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
import com.ludas.testapp.database.Storage;
import com.ludas.testapp.database.StorageDao;

import java.util.List;

public class StorageFragmentAdapter extends RecyclerView.Adapter<StorageFragmentAdapter.ViewHolder> {

    private List<Storage> storages;
    private AppDatabase database;
    private ProductDao productDao;
    private StorageDao storageDao;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final TextView recProductName, recQuantity, recLocation;
        protected final ImageButton recDelete;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            recProductName = (TextView) view.findViewById(R.id.storage_recview_product_name);
            recQuantity = (TextView) view.findViewById(R.id.storage_recview_quantity);
            recLocation = (TextView) view.findViewById(R.id.storage_recview_location);
            recDelete = (ImageButton) view.findViewById(R.id.storage_recview_delete);
        }
    }

    public interface OnListClicked {
        void onStorageSelected(long storageId);
    }

    public StorageFragmentAdapter(List<Storage> storages) {
        this.storages = storages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_storage, viewGroup, false);
        database = AppDatabase.getInstance(view.getContext());
        productDao = database.productDao();
        storageDao = database.storageDao();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Storage storage = storages.get(position);
        viewHolder.recQuantity.setText(String.valueOf(storage.getQuantity()));
        viewHolder.recLocation.setText(storage.getLocation());
        viewHolder.recProductName.setText(productDao.findById(storage.getId_product()).getName());


        viewHolder.recDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle(storage.getLocation() + " - " +
                                productDao.findById(storage.getId_product()).getName() + ": " +
                                storage.getQuantity())
                        .setMessage(R.string.storage_info_delete_confirmation)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                storageDao.delete(storage);
                                int pos = viewHolder.getAbsoluteAdapterPosition();
                                storages.remove(pos);
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
                    listener.onStorageSelected(storage.getId_storage());
                }
                catch (ClassCastException e) {
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return storages.size();
    }
}

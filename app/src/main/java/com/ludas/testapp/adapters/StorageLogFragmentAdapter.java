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
import com.ludas.testapp.database.Product;
import com.ludas.testapp.database.ProductDao;
import com.ludas.testapp.database.Storage;
import com.ludas.testapp.database.StorageDao;
import com.ludas.testapp.database.StorageLog;
import com.ludas.testapp.database.StorageLogDao;

import java.util.List;

public class StorageLogFragmentAdapter extends RecyclerView.Adapter<StorageLogFragmentAdapter.ViewHolder> {
    private List<StorageLog> storageLogs;
    private AppDatabase database;
    private StorageLogDao storageLogDao;
    private StorageDao storageDao;
    private ProductDao productDao;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final TextView recStorage, recQuantity, recType, recDate;
        protected final ImageButton recDelete;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            recStorage = (TextView) view.findViewById(R.id.storage_log_recview_storage);
            recQuantity = (TextView) view.findViewById(R.id.storage_log_recview_quantity);
            recType = (TextView) view.findViewById(R.id.storage_log_recview_type);
            recDate = (TextView) view.findViewById(R.id.storage_log_recview_date);
            recDelete = (ImageButton) view.findViewById(R.id.storage_log_recview_delete);
        }
    }

    public interface OnListClicked {
        void onStorageLogSelected(long storageLogId);
    }

    public StorageLogFragmentAdapter(List<StorageLog> storageLogs) {
        this.storageLogs = storageLogs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_storage_log, viewGroup, false);
        database = AppDatabase.getInstance(view.getContext());
        storageLogDao = database.storageLogDao();
        storageDao = database.storageDao();
        productDao = database.productDao();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        StorageLog storageLog = storageLogs.get(position);
        Storage storage = storageDao.findById(storageLog.getId_storage());
        Product product = productDao.findById(storage.getId_product());

        viewHolder.recQuantity.setText(String.valueOf(storageLog.getQuantity()));
        viewHolder.recDate.setText(storageLog.getDate());
        viewHolder.recStorage.setText(storage.getLocation() + " - " + product.getName());
        switch (storageLog.getType()) {
            case 0: {
                viewHolder.recType.setText(R.string.text_storage_log_type_0);
                break;
            }
            case 1: {
                viewHolder.recType.setText(R.string.text_storage_log_type_1);
                break;
            }
            default: {
                viewHolder.recType.setText(R.string.text_storage_log_type_unknown);
                break;
            }
        }

        viewHolder.recDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle(storage.getLocation() + " - " +
                                product.getName() + ": " +
                                storageLog.getQuantity())
                        .setMessage(R.string.storage_log_info_delete_confirmation)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                storageLogDao.delete(storageLog);
                                int pos = viewHolder.getAbsoluteAdapterPosition();
                                storageLogs.remove(pos);
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
                    listener.onStorageLogSelected(storageLog.getId_storage_log());
                }
                catch (ClassCastException e) {
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return storageLogs.size();
    }
}

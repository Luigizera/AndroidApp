package com.ludas.testapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Category;
import com.ludas.testapp.database.Product;
import com.ludas.testapp.database.ProductDao;
import com.ludas.testapp.database.Storage;
import com.ludas.testapp.database.StorageDao;
import com.ludas.testapp.database.StorageLog;
import com.ludas.testapp.database.StorageLogDao;

import java.util.List;

public class StorageInfoFragment extends Fragment {

    public static final String TAG = "StorageInfoFragment";
    private static final String ARG_STORAGEID = "storageId";
    private long storageId;
    private EditText editTextQuantity, editTextLocation;
    private TextView textViewError;
    private ImageButton imageButtonSubmit;
    private ImageButton imageButtonDelete;
    private Storage storage;
    private StorageDao storageDao;
    private StorageLogDao storageLogDao;
    private Product storageProduct;
    private List<Product> list;
    private Spinner spinnerProduct;
    private ArrayAdapter<Product> spinnerAdapter;

    public StorageInfoFragment() {
        // Required empty public constructor
    }

    public static StorageInfoFragment newInstance(long storageId) {
        StorageInfoFragment fragment = new StorageInfoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_STORAGEID, storageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.storageId = getArguments().getLong(ARG_STORAGEID);
            if(storageId >= 0) {
                AppDatabase database = AppDatabase.getInstance(getActivity());
                storageDao = database.storageDao();
                storageLogDao = database.storageLogDao();
                storage = storageDao.findById(storageId);
                if(storage != null) {
                    storageProduct = database
                            .productDao().findById(storage.getId_product());
                    list = database.productDao().getAll();
                    spinnerAdapter = new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            list
                    );
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage_info, container, false);
        if(storage != null) {
            //TODO
            editTextLocation = view.findViewById(R.id.fragment_storage_info_edittext_location);
            editTextLocation.setText(storage.getLocation());
            editTextQuantity = view.findViewById(R.id.fragment_storage_info_edittext_quantity);
            int oldQuantity = storage.getQuantity();
            editTextQuantity.setText(String.valueOf(oldQuantity));
            textViewError = view.findViewById(R.id.fragment_storage_info_textview_error);
            imageButtonSubmit = view.findViewById(R.id.fragment_storage_info_submitbutton);
            imageButtonDelete = view.findViewById(R.id.fragment_storage_info_deletebutton);
            spinnerProduct = view.findViewById(R.id.fragment_storage_info_id_product);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            int pos = spinnerAdapter.getPosition(storageProduct);
            spinnerProduct.setAdapter(spinnerAdapter);
            spinnerProduct.setSelection(pos);


            imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(spinnerProduct.getSelectedItem() == null) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(R.string.error_empty_product_spinner);
                        return;
                    }
                    if(editTextLocation.getText().toString().isEmpty()) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(R.string.error_empty_location);
                        return;
                    }
                    if(editTextQuantity.getText().toString().isEmpty()) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(R.string.error_empty_quantity);
                        return;
                    }
                    int quantity;
                    try {
                        quantity = Integer.parseInt(editTextQuantity.getText().toString());
                    } catch (Exception e) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(R.string.error_convert_quantity);
                        return;
                    }
                    storage.setLocation(editTextLocation.getText().toString());
                    storage.setQuantity(quantity);
                    Product product = (Product) spinnerProduct.getSelectedItem();
                    storage.setId_product(product.getId_product());

                    textViewError.setVisibility(View.INVISIBLE);
                    storageDao.updateStorages(storage);
                    int type = 0;
                    int diff = quantity - oldQuantity;
                    if(diff < 0) {
                        type = 1;
                    }
                    storageLogDao.insertAll(new StorageLog(diff, type, AppDatabase.getCurrentDate(), storageId));

                    Bundle result = new Bundle();
                    result.putBoolean("refresh_key", true);
                    getParentFragmentManager().setFragmentResult("request_key", result);
                    getParentFragmentManager().popBackStack();
                }
            });

            imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.storage_info_delete_confirmation)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    storageDao.delete(storage);
                                    Bundle result = new Bundle();
                                    result.putBoolean("refresh_key", true);

                                    getParentFragmentManager().setFragmentResult("request_key", result);
                                    getParentFragmentManager().popBackStack();
                                }})
                            .setNegativeButton(android.R.string.cancel, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    //TODO: DESCOBRIR COMO FAZER UM TEMA DECENTE PARA DELETAR ESSE CODIGO ABAIXO
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(R.style.Theme_TestApp);
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(R.style.Theme_TestApp);
                }
            });
        }
        return view;
    }
}
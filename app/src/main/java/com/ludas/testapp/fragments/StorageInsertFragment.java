package com.ludas.testapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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

public class StorageInsertFragment extends Fragment {

    public static final String TAG = "StorageInsertFragment";

    private EditText editTextQuantity, editTextLocation;
    private TextView textViewError;
    private ImageButton imageButtonSubmit;
    private StorageDao storageDao;
    private List<Product> list;
    private ArrayAdapter<Product> spinnerAdapter;
    private Spinner spinnerProduct;

    public StorageInsertFragment() {
        // Required empty public constructor
    }

    public static StorageInsertFragment newInstance() {
        StorageInsertFragment fragment = new StorageInsertFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase database = AppDatabase.getInstance(getActivity());
        list = database.productDao().getAll();
        storageDao = database.storageDao();
        spinnerAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                list
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage_insert, container, false);
        editTextQuantity = view.findViewById(R.id.fragment_storage_insert_edittext_quantity);
        editTextLocation = view.findViewById(R.id.fragment_storage_insert_edittext_location);
        spinnerProduct = view.findViewById(R.id.fragment_storage_insert_id_product);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduct.setAdapter(spinnerAdapter);

        textViewError = view.findViewById(R.id.fragment_storage_insert_textview_error);
        imageButtonSubmit = view.findViewById(R.id.fragment_storage_insert_submitbutton);

        imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextQuantity.getText().toString().isEmpty()) {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(R.string.error_empty_quantity);
                    return;
                }
                if(editTextLocation.getText().toString().isEmpty()) {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(R.string.error_empty_location);
                    return;
                }
                if(spinnerProduct.getSelectedItem() == null) {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(R.string.error_empty_product_spinner);
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
                Product selectedValue = (Product) spinnerProduct.getSelectedItem();
                textViewError.setVisibility(View.INVISIBLE);
                storageDao.insertAll(new Storage(quantity,
                        editTextLocation.getText().toString(),
                        selectedValue.getId_product()));
                editTextQuantity.setText("");
                editTextLocation.setText("");
            }
        });
        return view;
    }
}
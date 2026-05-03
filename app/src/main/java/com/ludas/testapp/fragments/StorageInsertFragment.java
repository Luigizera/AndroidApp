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

import java.util.List;

public class StorageInsertFragment extends Fragment {

    public static final String TAG = "StorageInsertFragment";

    private EditText quantity, location;
    private TextView textviewError;
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
        quantity = view.findViewById(R.id.fragment_storage_insert_quantity);
        location = view.findViewById(R.id.fragment_storage_insert_location);
        spinnerProduct = view.findViewById(R.id.fragment_storage_insert_id_product);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduct.setAdapter(spinnerAdapter);

        textviewError = view.findViewById(R.id.fragment_storage_insert_textview_error);
        imageButtonSubmit = view.findViewById(R.id.fragment_storage_insert_submitbutton);

        imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: verificar quantity se é inteiro
                if(quantity.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.error_empty_quantity);
                    return;
                }
                if(location.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.error_empty_location);
                    return;
                }
                if(spinnerProduct.getSelectedItem() == null) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.error_empty_product_spinner);
                    return;
                }
                Product selectedValue = (Product) spinnerProduct.getSelectedItem();
                textviewError.setVisibility(View.INVISIBLE);
                storageDao.insertAll(new Storage(Integer.parseInt(quantity.getText().toString()),
                        location.getText().toString(),
                        selectedValue.getId_product()));
                quantity.setText("");
                location.setText("");
            }
        });
        return view;
    }
}
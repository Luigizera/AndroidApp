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
    private android.widget.Button buttonBack;
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
        buttonBack = view.findViewById(R.id.fragment_storage_insert_backbutton);

        buttonBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearErrors();
                if(editTextQuantity.getText().toString().isEmpty()) {
                    showError(R.string.error_empty_quantity, editTextQuantity);
                    return;
                }
                if(editTextLocation.getText().toString().isEmpty()) {
                    showError(R.string.error_empty_location, editTextLocation);
                    return;
                }
                if(spinnerProduct.getSelectedItem() == null) {
                    showError(R.string.error_empty_product_spinner, spinnerProduct);
                    return;
                }
                int quantity;
                try {
                    quantity = Integer.parseInt(editTextQuantity.getText().toString());
                } catch (Exception e) {
                    showError(R.string.error_convert_quantity, editTextQuantity);
                    return;
                }
                Product selectedValue = (Product) spinnerProduct.getSelectedItem();
                textViewError.setVisibility(View.INVISIBLE);
                storageDao.insertAll(new Storage(quantity,
                        editTextLocation.getText().toString(),
                        selectedValue.getId_product()));
                clearFields();
            }
        });
        return view;
    }

    private void showError(int resId, View view) {
        textViewError.setVisibility(View.VISIBLE);
        textViewError.setText(resId);
        if (view != null) {
            int colorError = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.red);
            androidx.core.view.ViewCompat.setBackgroundTintList(view, android.content.res.ColorStateList.valueOf(colorError));
        }
    }

    private void clearErrors() {
        textViewError.setVisibility(View.INVISIBLE);
        int colorDefault = android.graphics.Color.BLACK;
        android.util.TypedValue typedValue = new android.util.TypedValue();
        if (requireContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true)) {
            colorDefault = typedValue.data;
        }
        android.content.res.ColorStateList tintList = android.content.res.ColorStateList.valueOf(colorDefault);
        androidx.core.view.ViewCompat.setBackgroundTintList(editTextQuantity, tintList);
        androidx.core.view.ViewCompat.setBackgroundTintList(editTextLocation, tintList);
        androidx.core.view.ViewCompat.setBackgroundTintList(spinnerProduct, tintList);
    }

    private void clearFields() {
        clearErrors();
        editTextQuantity.setText("");
        editTextLocation.setText("");
    }
}

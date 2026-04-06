package com.ludas.testapp.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Category;
import com.ludas.testapp.database.CategoryDao;
import com.ludas.testapp.database.Product;
import com.ludas.testapp.database.ProductDao;

import java.util.List;

public class ProductInsertFragment extends Fragment {

    public static final String TAG = "ProductInsertFragment";

    private EditText name;
    private EditText description;
    private EditText price;
    private Spinner spinnerCategory;
    private TextView textviewError;
    private ImageButton submitButton;
    private AppDatabase database;
    private CategoryDao categoryDao;
    private ArrayAdapter<Category> spinnerAdapter;
    private Cursor cursor;

    public ProductInsertFragment() {
        // Required empty public constructor
    }

    public static ProductInsertFragment newInstance() {
        ProductInsertFragment fragment = new ProductInsertFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getInstance(getActivity());
        categoryDao = database.categoryDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_insert, container, false);
        name = view.findViewById(R.id.fragment_product_insert_name);
        description = view.findViewById(R.id.fragment_product_insert_description);
        price = view.findViewById(R.id.fragment_product_insert_price);
        //TODO: FIX
        new Thread(() -> {
            List<Category> list = categoryDao.getAll();
            setupSpinner(view, list);
        }).start();

        textviewError = view.findViewById(R.id.fragment_product_insert_textview_error);
        submitButton = view.findViewById(R.id.fragment_product_insert_submitbutton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDao productDao = database.productDao();
                //TODO: create Strings
                if(name.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.category_error_empty_name);
                    return;
                }
                if(description.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.category_error_empty_name);
                    return;
                }
                if(price.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.category_error_empty_name);
                    return;
                }
                if(spinnerCategory.getSelectedItem() == null) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.category_error_empty_name);
                    return;
                }
                Category selectedValue = (Category) spinnerCategory.getSelectedItem();
                textviewError.setVisibility(View.INVISIBLE);
                productDao.insertAll(new Product(name.getText().toString(), description.getText().toString(), Double.parseDouble(price.getText().toString()), selectedValue.getId_category()));
                name.setText("");
                description.setText("");
                price.setText("");
            }
        });
        return view;
    }

    private void setupSpinner(View view, List<Category> list) {

        spinnerCategory = view.findViewById(R.id.fragment_product_insert_id_category);

        spinnerAdapter = new ArrayAdapter<>(
                view.getContext(),
                android.R.layout.simple_spinner_item,
                list
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(spinnerAdapter);
    }
}
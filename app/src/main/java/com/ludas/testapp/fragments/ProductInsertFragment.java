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

    private EditText name, description, price;
    private TextView textviewError;
    private ImageButton imageButtonSubmit;
    private List<Category> list;
    private ProductDao productDao;
    private Spinner spinnerCategory;
    private ArrayAdapter<Category> spinnerAdapter;

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
        AppDatabase database = AppDatabase.getInstance(getActivity());
        list = database.categoryDao().getAll();
        productDao = database.productDao();
        spinnerAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                list
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_insert, container, false);
        name = view.findViewById(R.id.fragment_product_insert_name);
        description = view.findViewById(R.id.fragment_product_insert_description);
        price = view.findViewById(R.id.fragment_product_insert_price);
        spinnerCategory = view.findViewById(R.id.fragment_product_insert_id_category);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        textviewError = view.findViewById(R.id.fragment_product_insert_textview_error);
        imageButtonSubmit = view.findViewById(R.id.fragment_product_insert_submitbutton);

        imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.error_empty_name);
                    return;
                }
                if(description.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.error_empty_description);
                    return;
                }
                if(price.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.error_empty_price);
                    return;
                }
                if(spinnerCategory.getSelectedItem() == null) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.error_empty_category_spinner);
                    return;
                }
                Category selectedValue = (Category) spinnerCategory.getSelectedItem();
                textviewError.setVisibility(View.INVISIBLE);
                productDao.insertAll(new Product(name.getText().toString(),
                        description.getText().toString(),
                        Double.parseDouble(price.getText().toString()),
                        selectedValue.getId_category()));
                name.setText("");
                description.setText("");
                price.setText("");
            }
        });
        return view;
    }
}
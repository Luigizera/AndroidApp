package com.ludas.testapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Category;
import com.ludas.testapp.database.Product;
import com.ludas.testapp.database.ProductCategoryCrossRef;
import com.ludas.testapp.database.ProductCategoryDao;
import com.ludas.testapp.database.ProductDao;

import java.util.ArrayList;
import java.util.List;

public class ProductInsertFragment extends Fragment {

    public static final String TAG = "ProductInsertFragment";

    private EditText editTextName, editTextDescription, editTextPrice, editTextPurchasePrice, editTextMinStock;
    private TextView textViewError, textViewSelectedCategories;
    private ImageButton imageButtonSubmit;
    private Button buttonSelectCategories;
    
    private List<Category> allCategories;
    private boolean[] checkedCategories;
    private List<Category> selectedCategories = new ArrayList<>();
    
    private ProductDao productDao;
    private ProductCategoryDao productCategoryDao;

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
        allCategories = database.categoryDao().getAll();
        productDao = database.productDao();
        productCategoryDao = database.productCategoryDao();
        
        checkedCategories = new boolean[allCategories.size()];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_insert, container, false);
        editTextName = view.findViewById(R.id.fragment_product_insert_edittext_name);
        editTextDescription = view.findViewById(R.id.fragment_product_insert_edittext_description);
        editTextPrice = view.findViewById(R.id.fragment_product_insert_edittext_price);
        editTextPurchasePrice = view.findViewById(R.id.fragment_product_insert_edittext_purchase_price);
        editTextMinStock = view.findViewById(R.id.fragment_product_insert_edittext_min_stock);
        buttonSelectCategories = view.findViewById(R.id.fragment_product_insert_button_select_categories);
        textViewSelectedCategories = view.findViewById(R.id.fragment_product_insert_textview_selected_categories);
        
        textViewError = view.findViewById(R.id.fragment_product_insert_textview_error);
        imageButtonSubmit = view.findViewById(R.id.fragment_product_insert_submitbutton);

        buttonSelectCategories.setOnClickListener(v -> showCategorySelectionDialog());

        imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextName.getText().toString().isEmpty()) {
                    showError(R.string.error_empty_name);
                    return;
                }
                if(editTextDescription.getText().toString().isEmpty()) {
                    showError(R.string.error_empty_description);
                    return;
                }
                if(editTextPrice.getText().toString().isEmpty()) {
                    showError(R.string.error_empty_price);
                    return;
                }
                if(selectedCategories.isEmpty()) {
                    showError(R.string.error_empty_category_spinner);
                    return;
                }
                
                textViewError.setVisibility(View.INVISIBLE);
                
                try {
                    double price = Double.parseDouble(editTextPrice.getText().toString());
                    double purchasePrice = Double.parseDouble(editTextPurchasePrice.getText().toString().isEmpty() ? "0" : editTextPurchasePrice.getText().toString());
                    int minStock = Integer.parseInt(editTextMinStock.getText().toString().isEmpty() ? "0" : editTextMinStock.getText().toString());

                    Product newProduct = new Product(
                            editTextName.getText().toString(),
                            editTextDescription.getText().toString(),
                            price,
                            purchasePrice,
                            minStock
                    );
                    
                    long productId = productDao.insert(newProduct);
                    
                    List<ProductCategoryCrossRef> crossRefs = new ArrayList<>();
                    for (Category cat : selectedCategories) {
                        crossRefs.add(new ProductCategoryCrossRef(productId, cat.getId_category()));
                    }
                    productCategoryDao.insertAll(crossRefs);
                    
                    clearFields();
                } catch (NumberFormatException e) {
                    showError(R.string.error_convert_price);
                }
            }
        });
        return view;
    }
    
    private void showCategorySelectionDialog() {
        String[] categoryNames = new String[allCategories.size()];
        for (int i = 0; i < allCategories.size(); i++) {
            categoryNames[i] = allCategories.get(i).getName();
        }

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.select_categories)
                .setMultiChoiceItems(categoryNames, checkedCategories, (dialog, which, isChecked) -> checkedCategories[which] = isChecked)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    selectedCategories.clear();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < checkedCategories.length; i++) {
                        if (checkedCategories[i]) {
                            Category cat = allCategories.get(i);
                            selectedCategories.add(cat);
                            if (sb.length() > 0) sb.append(", ");
                            sb.append(cat.getName());
                        }
                    }
                    if (sb.length() > 0) {
                        textViewSelectedCategories.setText(sb.toString());
                    } else {
                        textViewSelectedCategories.setText(R.string.no_categories_selected);
                    }
                })
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }
    
    private void showError(int resId) {
        textViewError.setVisibility(View.VISIBLE);
        textViewError.setText(resId);
    }
    
    private void clearFields() {
        editTextName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextPurchasePrice.setText("");
        editTextMinStock.setText("");
        selectedCategories.clear();
        for (int i = 0; i < checkedCategories.length; i++) {
            checkedCategories[i] = false;
        }
        textViewSelectedCategories.setText(R.string.no_categories_selected);
        textViewError.setVisibility(View.INVISIBLE);
    }
}

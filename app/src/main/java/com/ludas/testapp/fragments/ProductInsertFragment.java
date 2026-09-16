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
    private Button buttonSelectCategories, buttonBack;
    
    private List<Category> allCategories;
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
        buttonBack = view.findViewById(R.id.fragment_product_insert_backbutton);

        buttonBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        buttonSelectCategories.setOnClickListener(v -> {
            ArrayList<Long> ids = new ArrayList<>();
            for (Category cat : selectedCategories) ids.add(cat.getId_category());
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_framelayout, CategorySelectionFragment.newInstance(ids))
                    .addToBackStack(null)
                    .commit();
        });

        getParentFragmentManager().setFragmentResultListener(CategorySelectionFragment.REQUEST_KEY, getViewLifecycleOwner(), (requestKey, bundle) -> {
            List<Long> ids = (List<Long>) bundle.getSerializable(CategorySelectionFragment.EXTRA_SELECTED_IDS);
            if (ids != null) {
                selectedCategories.clear();
                for (Long id : ids) {
                    Category cat = findCategoryById(id);
                    if (cat != null) selectedCategories.add(cat);
                }
                updateSelectedCategoriesText();
            }
        });

        imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearErrors();
                if(editTextName.getText().toString().isEmpty()) {
                    showError(R.string.error_empty_name, editTextName);
                    return;
                }
                if(editTextDescription.getText().toString().isEmpty()) {
                    showError(R.string.error_empty_description, editTextDescription);
                    return;
                }
                if(editTextPrice.getText().toString().isEmpty()) {
                    showError(R.string.error_empty_price, editTextPrice);
                    return;
                }
                if(selectedCategories.isEmpty()) {
                    showError(R.string.error_empty_category_spinner, buttonSelectCategories);
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
                    showError(R.string.error_convert_price, editTextPrice);
                }
            }
        });
        return view;
    }
    
    private void updateSelectedCategoriesText() {
        StringBuilder sb = new StringBuilder();
        for (Category cat : selectedCategories) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(cat.getName());
        }
        textViewSelectedCategories.setText(sb.length() > 0 ? sb.toString() : getString(R.string.no_categories_selected));
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
        androidx.core.view.ViewCompat.setBackgroundTintList(editTextName, tintList);
        androidx.core.view.ViewCompat.setBackgroundTintList(editTextDescription, tintList);
        androidx.core.view.ViewCompat.setBackgroundTintList(editTextPrice, tintList);
        androidx.core.view.ViewCompat.setBackgroundTintList(editTextPurchasePrice, tintList);
        androidx.core.view.ViewCompat.setBackgroundTintList(editTextMinStock, tintList);
        androidx.core.view.ViewCompat.setBackgroundTintList(buttonSelectCategories, tintList);
    }
    
    private void clearFields() {
        clearErrors();
        editTextName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextPurchasePrice.setText("");
        editTextMinStock.setText("");
        selectedCategories.clear();
        textViewSelectedCategories.setText(R.string.no_categories_selected);
    }

    private Category findCategoryById(long id) {
        for (Category cat : allCategories) {
            if (cat.getId_category() == id) return cat;
        }
        return null;
    }
}

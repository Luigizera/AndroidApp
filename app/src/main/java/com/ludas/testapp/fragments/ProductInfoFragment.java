package com.ludas.testapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.ludas.testapp.database.StorageDao;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoFragment extends Fragment {

    public static final String TAG = "ProductInfoFragment";
    private static final String ARG_PRODUCTID = "productId";
    private long productId;
    private EditText editTextName, editTextDescription, editTextPrice, editTextPurchasePrice, editTextMinStock;
    private TextView textViewError, textViewSelectedCategories;
    private ImageButton imageButtonSubmit;
    private ImageButton imageButtonDelete;
    private Button buttonSelectCategories;

    private Product product;
    private ProductDao productDao;
    private ProductCategoryDao productCategoryDao;
    private StorageDao storageDao;

    private List<Category> allCategories;
    private List<Category> selectedCategories = new ArrayList<>();

    public ProductInfoFragment() {
        // Required empty public constructor
    }

    public static ProductInfoFragment newInstance(long productId) {
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PRODUCTID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.productId = getArguments().getLong(ARG_PRODUCTID);
            if(productId >= 0) {
                AppDatabase database = AppDatabase.getInstance(getActivity());
                productDao = database.productDao();
                productCategoryDao = database.productCategoryDao();
                storageDao = database.storageDao();
                product = productDao.findById(productId);
                allCategories = database.categoryDao().getAll();
                
                if(product != null) {
                    selectedCategories = productCategoryDao.getCategoriesForProduct(productId);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);
        if(product != null) {
            editTextName = view.findViewById(R.id.fragment_product_info_edittext_name);
            editTextName.setText(product.getName());
            editTextDescription = view.findViewById(R.id.fragment_product_info_edittext_description);
            editTextDescription.setText(product.getDescription());
            editTextPrice = view.findViewById(R.id.fragment_product_info_edittext_price);
            editTextPrice.setText(String.valueOf(product.getPrice()));
            editTextPurchasePrice = view.findViewById(R.id.fragment_product_info_edittext_purchase_price);
            editTextPurchasePrice.setText(String.valueOf(product.getPurchasePrice()));
            editTextMinStock = view.findViewById(R.id.fragment_product_info_edittext_min_stock);
            editTextMinStock.setText(String.valueOf(product.getMinStockLevel()));
            
            textViewError = view.findViewById(R.id.fragment_product_info_textview_error);
            textViewSelectedCategories = view.findViewById(R.id.fragment_product_info_textview_selected_categories);
            imageButtonSubmit = view.findViewById(R.id.fragment_product_info_submitbutton);
            imageButtonDelete = view.findViewById(R.id.fragment_product_info_deletebutton);
            buttonSelectCategories = view.findViewById(R.id.fragment_product_info_button_select_categories);

            updateSelectedCategoriesText();

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
                    
                    try {
                        double price = Double.parseDouble(editTextPrice.getText().toString());
                        double purchasePrice = Double.parseDouble(editTextPurchasePrice.getText().toString().isEmpty() ? "0" : editTextPurchasePrice.getText().toString());
                        int minStock = Integer.parseInt(editTextMinStock.getText().toString().isEmpty() ? "0" : editTextMinStock.getText().toString());

                        product.setName(editTextName.getText().toString());
                        product.setDescription(editTextDescription.getText().toString());
                        product.setPrice(price);
                        product.setPurchasePrice(purchasePrice);
                        product.setMinStockLevel(minStock);

                        textViewError.setVisibility(View.INVISIBLE);
                        productDao.updateProducts(product);
                        
                        // Update links
                        productCategoryDao.deleteByProductId(productId);
                        List<ProductCategoryCrossRef> crossRefs = new ArrayList<>();
                        for (Category cat : selectedCategories) {
                            crossRefs.add(new ProductCategoryCrossRef(productId, cat.getId_category()));
                        }
                        productCategoryDao.insertAll(crossRefs);

                        Bundle result = new Bundle();
                        result.putBoolean("refresh_key", true);
                        getParentFragmentManager().setFragmentResult("request_key", result);
                        getParentFragmentManager().popBackStack();
                    } catch (NumberFormatException e) {
                        showError(R.string.error_convert_price);
                    }
                }
            });

            imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int storageCount = storageDao.countByProductId(productId);
                    if (storageCount > 0) {
                        new MaterialAlertDialogBuilder(getActivity())
                                .setTitle(product.getName())
                                .setMessage(R.string.error_delete_product_has_storage)
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                        return;
                    }

                    new MaterialAlertDialogBuilder(getActivity())
                            .setTitle(product.getName())
                            .setMessage(R.string.product_info_delete_confirmation)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    productDao.delete(product);
                                    Bundle result = new Bundle();
                                    result.putBoolean("refresh_key", true);

                                    getParentFragmentManager().setFragmentResult("request_key", result);
                                    getParentFragmentManager().popBackStack();
                                }})
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                }
            });
        }

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

    private void showError(int resId) {
        textViewError.setVisibility(View.VISIBLE);
        textViewError.setText(resId);
    }

    private Category findCategoryById(long id) {
        for (Category cat : allCategories) {
            if (cat.getId_category() == id) return cat;
        }
        return null;
    }
}

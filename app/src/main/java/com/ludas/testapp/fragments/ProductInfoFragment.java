package com.ludas.testapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Category;
import com.ludas.testapp.database.CategoryDao;
import com.ludas.testapp.database.Product;
import com.ludas.testapp.database.ProductDao;

import java.util.List;

public class ProductInfoFragment extends Fragment {

    public static final String TAG = "ProductInfoFragment";
    private static final String ARG_PRODUCTID = "productId";
    private long productId;
    private EditText editTextName, editTextDescription, editTextPrice;
    private TextView textViewError;
    private ImageButton imageButtonSubmit;
    private ImageButton imageButtonDelete;
    private Product product;
    private ProductDao productDao;
    private Category productCategory;
    private List<Category> list;
    private Spinner spinnerCategory;
    private ArrayAdapter<Category> spinnerAdapter;

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
                product = productDao.findById(productId);
                if(product != null) {
                    productCategory = database
                            .categoryDao().findById(product.getId_category());
                    list = database.categoryDao().getAll();
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
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);
        if(product != null) {
            editTextName = view.findViewById(R.id.fragment_product_info_name);
            editTextName.setText(product.getName());
            editTextDescription = view.findViewById(R.id.fragment_product_info_description);
            editTextDescription.setText(product.getDescription());
            editTextPrice = view.findViewById(R.id.fragment_product_info_price);
            editTextPrice.setText(String.valueOf(product.getPrice()));
            textViewError = view.findViewById(R.id.fragment_product_info_textview_error);
            imageButtonSubmit = view.findViewById(R.id.fragment_product_info_submitbutton);
            imageButtonDelete = view.findViewById(R.id.fragment_product_info_deletebutton);
            spinnerCategory = view.findViewById(R.id.fragment_product_info_id_category);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            int pos = spinnerAdapter.getPosition(productCategory);
            spinnerCategory.setAdapter(spinnerAdapter);
            spinnerCategory.setSelection(pos);


            imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(spinnerCategory.getSelectedItem() == null) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(R.string.error_empty_category_spinner);
                        return;
                    }
                    if(editTextName.getText().toString().isEmpty()) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(R.string.error_empty_name);
                        return;
                    }
                    if(editTextDescription.getText().toString().isEmpty()) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(R.string.error_empty_description);
                        return;
                    }
                    if(editTextPrice.getText().toString().isEmpty()) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(R.string.error_empty_price);
                        return;
                    }
                    double price;
                    try {
                        price = Double.parseDouble(editTextPrice.getText().toString());
                    } catch (Exception e) {
                        textViewError.setVisibility(View.VISIBLE);
                        textViewError.setText(R.string.error_convert_price);
                        return;
                    }
                    product.setName(editTextName.getText().toString());
                    product.setDescription(editTextDescription.getText().toString());
                    product.setPrice(price);
                    Category category = (Category) spinnerCategory.getSelectedItem();
                    product.setId_category(category.getId_category());

                    textViewError.setVisibility(View.INVISIBLE);
                    productDao.updateProducts(product);

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
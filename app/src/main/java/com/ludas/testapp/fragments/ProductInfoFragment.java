package com.ludas.testapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ludas.testapp.R;

public class ProductInfoFragment extends Fragment {

    public static final String TAG = "ProductInfoFragment";
    private static final String ARG_PRODUCTID = "productId";
    private long productId;
    TextView textView;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);
        if(productId != -1) {
            textView = view.findViewById(R.id.product_info_textview);
            textView.setText(String.valueOf(productId));
        }
        return view;
    }
}
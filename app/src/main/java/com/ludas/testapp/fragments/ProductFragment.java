package com.ludas.testapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ludas.testapp.R;
import com.ludas.testapp.adapters.ProductFragmentAdapter;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Product;
import com.ludas.testapp.database.ProductDao;

import java.util.List;

public class ProductFragment extends Fragment {

    public static final String TAG = "ProductFragment";
    private RecyclerView recyclerView;
    private AppDatabase database;
    private ProductDao productDao;
    private List<Product> products;
    private ProductFragmentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public ProductFragment() {
        // Required empty public constructor
    }
    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getInstance(getActivity());
        productDao = database.productDao();
        products = productDao.getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        recyclerView = view.findViewById(R.id.fragment_product_recview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductFragmentAdapter(products);
        recyclerView.setAdapter(adapter);
        getParentFragmentManager().setFragmentResultListener("request_key",
                getViewLifecycleOwner(),
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        boolean result = bundle.getBoolean("refresh_key");
                        if(result) {
                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.activity_main_framelayout, ProductFragment.newInstance())
                                    .commit();
                        }
                    }
                }
        );
        return view;
    }
}
package com.ludas.testapp.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ludas.testapp.R;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        CardView cardCategory = view.findViewById(R.id.card_category);
        CardView cardProduct = view.findViewById(R.id.card_product);
        CardView cardStorage = view.findViewById(R.id.card_storage);
        CardView cardStorageLog = view.findViewById(R.id.card_storage_log);

        cardCategory.setOnClickListener(v -> navigateTo(CategoryFragment.newInstance(1), CategoryFragment.TAG));
        cardProduct.setOnClickListener(v -> navigateTo(ProductFragment.newInstance(), ProductFragment.TAG));
        cardStorage.setOnClickListener(v -> navigateTo(StorageFragment.newInstance(), StorageFragment.TAG));
        cardStorageLog.setOnClickListener(v -> navigateTo(StorageLogFragment.newInstance(), StorageLogFragment.TAG));

        return view;
    }

    private void navigateTo(Fragment fragment, String tag) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_main_framelayout, fragment, tag)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
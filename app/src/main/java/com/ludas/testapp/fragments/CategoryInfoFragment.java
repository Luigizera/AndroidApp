package com.ludas.testapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ludas.testapp.R;


public class CategoryInfoFragment extends Fragment {

    public static final String TAG = "CategoryInfoFragment";
    private static final String ARG_CATEGORYID = "categoryId";
    private long categoryId;
    TextView textView;

    public CategoryInfoFragment() {
        // Required empty public constructor
    }

    public static CategoryInfoFragment newInstance(long categoryId) {
        CategoryInfoFragment fragment = new CategoryInfoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORYID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.categoryId = getArguments().getLong(ARG_CATEGORYID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_info, container, false);
        if(categoryId != -1) {
            textView = view.findViewById(R.id.category_info_textview);
            textView.setText(String.valueOf(categoryId));
        }
        return view;
    }
}
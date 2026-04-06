package com.ludas.testapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ludas.testapp.adapters.CategoryFragmentAdapter;
import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Category;
import com.ludas.testapp.database.CategoryDao;

import java.util.List;

public class CategoryFragment extends Fragment {

    public static final String TAG = "CategoryFragment";
    private RecyclerView recyclerView;
    private AppDatabase database;
    private CategoryDao categoryDao;
    private List<Category> categories;
    private CategoryFragmentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public CategoryFragment() {
        // Required empty public constructor
    }
    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getInstance(getActivity());
        categoryDao = database.categoryDao();
        categories = categoryDao.getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.fragment_category_recview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CategoryFragmentAdapter(categories);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
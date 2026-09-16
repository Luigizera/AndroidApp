package com.ludas.testapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ludas.testapp.R;
import com.ludas.testapp.adapters.CategorySelectionAdapter;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Category;
import com.ludas.testapp.database.CategoryDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategorySelectionFragment extends Fragment {

    public static final String TAG = "CategorySelectionFragment";
    public static final String REQUEST_KEY = "category_selection_request";
    public static final String EXTRA_SELECTED_IDS = "selected_ids";
    
    private static final String ARG_INITIAL_IDS = "initial_ids";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private RecyclerView recyclerView;
    private CategorySelectionAdapter adapter;
    private SearchView searchView;
    private Button btnConfirm, btnBack;
    private CategoryDao categoryDao;
    
    private List<Category> allCategories = new ArrayList<>();
    private List<Long> initialSelectedIds;

    public static CategorySelectionFragment newInstance(ArrayList<Long> initialSelectedIds) {
        CategorySelectionFragment fragment = new CategorySelectionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_INITIAL_IDS, initialSelectedIds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryDao = AppDatabase.getInstance(requireContext()).categoryDao();
        if (getArguments() != null) {
            initialSelectedIds = (List<Long>) getArguments().getSerializable(ARG_INITIAL_IDS);
        }
        if (initialSelectedIds == null) initialSelectedIds = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_selection, container, false);

        recyclerView = view.findViewById(R.id.category_selection_recview);
        searchView = view.findViewById(R.id.category_selection_search);
        btnConfirm = view.findViewById(R.id.category_selection_confirm);
        btnBack = view.findViewById(R.id.category_selection_back);

        adapter = new CategorySelectionAdapter(allCategories, initialSelectedIds);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        loadCategories("");

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        btnConfirm.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putSerializable(EXTRA_SELECTED_IDS, (ArrayList<Long>) adapter.getSelectedIds());
            getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
            getParentFragmentManager().popBackStack();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadCategories(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchRunnable != null) searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = () -> loadCategories(newText);
                searchHandler.postDelayed(searchRunnable, 400);
                return true;
            }
        });

        return view;
    }

    private void loadCategories(String query) {
        executor.execute(() -> {
            List<Category> results;
            if (query == null || query.isEmpty()) {
                results = categoryDao.getAll();
            } else {
                results = categoryDao.search(query);
            }

            if (!isAdded()) return;

            requireActivity().runOnUiThread(() -> {
                allCategories.clear();
                allCategories.addAll(results);
                adapter.setCategories(allCategories);
            });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}

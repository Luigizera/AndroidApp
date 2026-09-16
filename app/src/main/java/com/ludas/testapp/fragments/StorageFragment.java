package com.ludas.testapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ludas.testapp.R;
import com.ludas.testapp.adapters.StorageFragmentAdapter;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Storage;
import com.ludas.testapp.database.StorageDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StorageFragment extends Fragment {

    public static final String TAG = "StorageFragment";
    private static final String ARG_CURRENTPAGE = "current_page";
    private static final String ARG_SEARCHTEXT = "search_text";
    private static final String ARG_FILTERPOS = "filter_pos";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private RecyclerView recyclerView;
    private ImageButton imageButtonAdd;
    private SearchView searchView;
    private Spinner filterSpinner;
    private LinearLayout paginationContainer;
    private AppDatabase database;
    private StorageDao storageDao;
    private List<Storage> storages;
    private StorageFragmentAdapter adapter;
    private LinearLayoutManager layoutManager;

    private final int limit = 10;
    private int currentPage = 1;
    private int totalPages = 1;
    private boolean isLoading = false;
    private String currentSearchText = "";
    private int currentFilterPosition = 0; // 0 for Location, 1 for ID

    public StorageFragment() {
        // Required empty public constructor
    }
    public static StorageFragment newInstance() {
        return newInstance(1, "", 0);
    }
    public static StorageFragment newInstance(int currentPage, String searchText, int filterPos) {
        StorageFragment fragment = new StorageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENTPAGE, currentPage);
        args.putString(ARG_SEARCHTEXT, searchText);
        args.putInt(ARG_FILTERPOS, filterPos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getInstance(requireContext());
        storageDao = database.storageDao();

        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(ARG_CURRENTPAGE, 1);
            currentSearchText = savedInstanceState.getString(ARG_SEARCHTEXT, "");
            currentFilterPosition = savedInstanceState.getInt(ARG_FILTERPOS, 0);
        } else if (getArguments() != null) {
            currentPage = getArguments().getInt(ARG_CURRENTPAGE, 1);
            currentSearchText = getArguments().getString(ARG_SEARCHTEXT, "");
            currentFilterPosition = getArguments().getInt(ARG_FILTERPOS, 0);
        }

        storages = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_CURRENTPAGE, currentPage);
        outState.putString(ARG_SEARCHTEXT, currentSearchText);
        outState.putInt(ARG_FILTERPOS, currentFilterPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage, container, false);
        recyclerView = view.findViewById(R.id.fragment_storage_recview);
        imageButtonAdd = view.findViewById(R.id.fragment_storage_buttonadd);
        searchView = view.findViewById(R.id.fragment_storage_search);
        filterSpinner = view.findViewById(R.id.fragment_storage_filter_spinner);
        paginationContainer = view.findViewById(R.id.fragment_storage_pagination_container);

        setupFilterSpinner();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StorageFragmentAdapter(storages);
        recyclerView.setAdapter(adapter);

        carregarStorage();

        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity fragmentActivity = getActivity();
                if(fragmentActivity == null) {
                    return;
                }
                fragmentActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main_framelayout, StorageInsertFragment.newInstance(), StorageInsertFragment.TAG)
                        .addToBackStack(null)
                        .commit();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pesquisarStorage(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(currentSearchText)) {
                    return true;
                }
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> pesquisarStorage(newText);
                searchHandler.postDelayed(searchRunnable, 400);
                return true;
            }
        });

        getParentFragmentManager().setFragmentResultListener("request_key",
                getViewLifecycleOwner(),
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        boolean result = bundle.getBoolean("refresh_key");
                        if(result) {
                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.activity_main_framelayout, StorageFragment.newInstance(currentPage, currentSearchText, currentFilterPosition))
                                    .commit();
                        }
                    }
                }
        );
        return view;
    }

    private void setupFilterSpinner() {
        String[] filters = {getString(R.string.text_storage_location), getString(R.string.filter_quantity), getString(R.string.filter_id)};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, filters);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);
        filterSpinner.setSelection(currentFilterPosition);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentFilterPosition != position) {
                    currentFilterPosition = position;
                    pesquisarStorage(searchView.getQuery().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void pesquisarStorage(String texto) {
        currentSearchText = texto;
        currentPage = 1;
        carregarStorage();
    }

    private void carregarStorage() {
        if (isLoading) return;
        isLoading = true;

        executor.execute(() -> {
            int totalItems;
            List<Storage> resultado;
            int offset = (currentPage - 1) * limit;

            if (currentSearchText == null || currentSearchText.trim().isEmpty()) {
                totalItems = storageDao.count();
                resultado = storageDao.getPaged(limit, offset);
            } else {
                String searchStr = currentSearchText.trim();
                if (currentFilterPosition == 2) { // ID
                    totalItems = storageDao.countSearchById(searchStr);
                    resultado = storageDao.searchByIdPaged(searchStr, limit, offset);
                } else if (currentFilterPosition == 1) { // Quantity
                    totalItems = storageDao.countSearchByQuantity(searchStr);
                    resultado = storageDao.searchByQuantityPaged(searchStr, limit, offset);
                } else { // Location
                    totalItems = storageDao.countSearchByLocation(searchStr);
                    resultado = storageDao.searchByLocationPaged(searchStr, limit, offset);
                }
            }

            int calculatedTotalPages = (int) Math.ceil((double) totalItems / limit);
            final int finalTotalPages = Math.max(1, calculatedTotalPages);
            final List<Storage> finalResultado = resultado;

            if (!isAdded()) {
                isLoading = false;
                return;
            }

            requireActivity().runOnUiThread(() -> {
                totalPages = finalTotalPages;
                storages.clear();
                storages.addAll(finalResultado);
                adapter.notifyDataSetChanged();
                atualizarPaginacao();
                isLoading = false;
            });
        });
    }

    private void atualizarPaginacao() {
        android.content.Context context = getContext();
        if (context == null || paginationContainer == null) return;
        paginationContainer.removeAllViews();

        TypedValue typedValue = new TypedValue();
        int colorPrimaryAttr = context.getResources().getIdentifier("colorPrimary", "attr", context.getPackageName());
        int colorSecondaryAttr = context.getResources().getIdentifier("colorSecondary", "attr", context.getPackageName());

        context.getTheme().resolveAttribute(colorPrimaryAttr, typedValue, true);
        int colorPrimary = typedValue.data;
        context.getTheme().resolveAttribute(colorSecondaryAttr, typedValue, true);
        int colorSecondary = typedValue.data;

        int marginPx = (int) (4 * getResources().getDisplayMetrics().density);

        Button btnPrev = new Button(getContext(), null, android.R.attr.buttonStyleSmall);
        btnPrev.setText("<");
        btnPrev.setEnabled(currentPage > 1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(marginPx, marginPx, marginPx, marginPx);
        btnPrev.setLayoutParams(params);
        btnPrev.setBackgroundColor(colorSecondary);
        btnPrev.setTextColor(colorPrimary);
        btnPrev.setAlpha(btnPrev.isEnabled() ? 1.0f : 0.5f);
        if (btnPrev.isEnabled() && btnPrev.getBackground() != null) btnPrev.getBackground().setAlpha(150);
        btnPrev.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                carregarStorage();
            }
        });
        paginationContainer.addView(btnPrev);

        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);
        for (int i = startPage; i <= endPage; i++) {
            final int pageNum = i;
            Button btnPage = new Button(getContext(), null, android.R.attr.buttonStyleSmall);
            btnPage.setText(String.valueOf(i));
            btnPage.setLayoutParams(params);
            if (i == currentPage) {
                btnPage.setBackgroundColor(colorPrimary);
                btnPage.setTextColor(colorSecondary);
            } else {
                btnPage.setBackgroundColor(colorSecondary);
                btnPage.setTextColor(colorPrimary);
                if (btnPage.getBackground() != null) btnPage.getBackground().setAlpha(150);
            }
            btnPage.setOnClickListener(v -> {
                if (currentPage != pageNum) {
                    currentPage = pageNum;
                    carregarStorage();
                }
            });
            paginationContainer.addView(btnPage);
        }

        Button btnNext = new Button(getContext(), null, android.R.attr.buttonStyleSmall);
        btnNext.setText(">");
        btnNext.setEnabled(currentPage < totalPages);
        btnNext.setLayoutParams(params);
        btnNext.setBackgroundColor(colorSecondary);
        btnNext.setTextColor(colorPrimary);
        btnNext.setAlpha(btnNext.isEnabled() ? 1.0f : 0.5f);
        if (btnNext.isEnabled() && btnNext.getBackground() != null) btnNext.getBackground().setAlpha(150);
        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                carregarStorage();
            }
        });
        paginationContainer.addView(btnNext);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchRunnable != null) searchHandler.removeCallbacks(searchRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
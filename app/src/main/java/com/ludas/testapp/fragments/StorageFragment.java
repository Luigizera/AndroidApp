package com.ludas.testapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ludas.testapp.R;
import com.ludas.testapp.adapters.ProductFragmentAdapter;
import com.ludas.testapp.adapters.StorageFragmentAdapter;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Product;
import com.ludas.testapp.database.ProductDao;
import com.ludas.testapp.database.Storage;
import com.ludas.testapp.database.StorageDao;

import java.util.List;

public class StorageFragment extends Fragment {

    public static final String TAG = "StorageFragment";
    private RecyclerView recyclerView;
    private ImageButton imageButtonAdd;
    private AppDatabase database;
    private StorageDao storageDao;
    private List<Storage> storages;
    private StorageFragmentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public StorageFragment() {
        // Required empty public constructor
    }
    public static StorageFragment newInstance() {
        StorageFragment fragment = new StorageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getInstance(getActivity());
        storageDao = database.storageDao();
        storages = storageDao.getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage, container, false);
        recyclerView = view.findViewById(R.id.fragment_storage_recview);
        imageButtonAdd = view.findViewById(R.id.fragment_storage_buttonadd);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StorageFragmentAdapter(storages);
        recyclerView.setAdapter(adapter);

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
                        .commit();
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
                                    .replace(R.id.activity_main_framelayout, StorageFragment.newInstance())
                                    .commit();
                        }
                    }
                }
        );
        return view;
    }
}
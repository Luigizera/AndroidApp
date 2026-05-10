package com.ludas.testapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ludas.testapp.R;
import com.ludas.testapp.adapters.StorageLogFragmentAdapter;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.StorageLog;
import com.ludas.testapp.database.StorageLogDao;

import java.util.List;

public class StorageLogFragment extends Fragment {

    public static final String TAG = "StorageLogFragment";
    private RecyclerView recyclerView;
    private AppDatabase database;
    private StorageLogDao storageLogDao;
    private List<StorageLog> storageLogs;
    private StorageLogFragmentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public StorageLogFragment() {
        // Required empty public constructor
    }
    public static StorageLogFragment newInstance() {
        StorageLogFragment fragment = new StorageLogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getInstance(getActivity());
        storageLogDao = database.storageLogDao();
        storageLogs = storageLogDao.getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage_log, container, false);
        recyclerView = view.findViewById(R.id.fragment_storage_log_recview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StorageLogFragmentAdapter(storageLogs);
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
                                    .replace(R.id.activity_main_framelayout, StorageLogFragment.newInstance())
                                    .commit();
                        }
                    }
                }
        );
        return view;
    }
}
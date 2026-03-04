package com.ludas.testapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.User;
import com.ludas.testapp.database.UserDao;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private EditText firstName, lastName;
    private Button submitButton;

    private AppDatabase database;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param databaseName Database name.
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance(String databaseName) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        //args.putString(KEY_DATABASENAME, databaseName);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //databaseName = getArguments().getString(KEY_DATABASENAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = AppDatabase.getInstance(view.getContext());
        firstName = view.findViewById(R.id.t5);
        lastName = view.findViewById(R.id.t4);
        submitButton = view.findViewById(R.id.submit_button);


        if(firstName == null || lastName == null || submitButton == null) return;

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDao userDao = database.userDao();
                if(firstName.getText().toString().isEmpty()) {
                    return;
                }
                if(lastName.getText().toString().isEmpty()) {
                    return;
                }
                userDao.insertAll(new User(firstName.getText().toString(), lastName.getText().toString()));
                firstName.setText("");
                lastName.setText("");
            }
        });
    }
}
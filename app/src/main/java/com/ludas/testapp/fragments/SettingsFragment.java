package com.ludas.testapp.fragments;

import android.icu.text.SimpleDateFormat;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";
    private EditText firstName, lastName;
    private Button submitButton;

    private AppDatabase database;
    private SimpleDateFormat sdf;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance() {
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        database = AppDatabase.getInstance(view.getContext());
        sdf = AppDatabase.getDateFormat();
        firstName = view.findViewById(R.id.fragment_settings_firstname);
        lastName = view.findViewById(R.id.fragment_settings_secondname);
        submitButton = view.findViewById(R.id.fragment_settings_submitbutton);

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
                Date d = Calendar.getInstance().getTime();
                String date = sdf.format(d);
                userDao.insertAll(new User(date, firstName.getText().toString(), lastName.getText().toString()));
                firstName.setText("");
                lastName.setText("");
            }
        });
        return view;
    }
}
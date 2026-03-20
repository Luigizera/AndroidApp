package com.ludas.testapp.fragments;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.User;
import com.ludas.testapp.database.UserDao;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertFragment extends Fragment {

    public static final String TAG = "SettingsFragment";
    private EditText firstName, lastName;
    private TextView textviewError;
    private ImageButton submitButton;

    private AppDatabase database;
    private SimpleDateFormat sdf;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    public static InsertFragment newInstance() {
        InsertFragment fragment = new InsertFragment();
        Bundle args = new Bundle();
        //args.putString(KEY_DATABASENAME, databaseName);
        fragment.setArguments(args);
        return fragment;
    }

    public InsertFragment() {
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
        View view = inflater.inflate(R.layout.fragment_insert, container, false);
        database = AppDatabase.getInstance(view.getContext());
        sdf = AppDatabase.getDateFormat();
        firstName = view.findViewById(R.id.fragment_settings_firstname);
        textviewError = view.findViewById(R.id.fragment_settings_textview_error);
        lastName = view.findViewById(R.id.fragment_settings_lastname);
        submitButton = view.findViewById(R.id.fragment_settings_submitbutton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDao userDao = database.userDao();

                if(firstName.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.firstname_empty_error);
                    return;
                }
                if(lastName.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.lastname_empty_error);
                    return;
                }
                textviewError.setVisibility(View.INVISIBLE);
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
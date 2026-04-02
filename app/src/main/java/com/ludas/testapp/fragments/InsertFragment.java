package com.ludas.testapp.fragments;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertFragment extends Fragment {

    public static final String TAG = "InsertFragment";
    private EditText firstName, lastName;
    private TextView textviewError;
    private ImageButton submitButton;

    private AppDatabase database;

    public static InsertFragment newInstance() {
        InsertFragment fragment = new InsertFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public InsertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert, container, false);
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
                    textviewError.setText(R.string.error_empty_firstname);
                    return;
                }
                if(lastName.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.error_empty_lastname);
                    return;
                }
                textviewError.setVisibility(View.INVISIBLE);
                userDao.insertAll(new User(firstName.getText().toString(), lastName.getText().toString()));
                firstName.setText("");
                lastName.setText("");
            }
        });
        return view;
    }
}
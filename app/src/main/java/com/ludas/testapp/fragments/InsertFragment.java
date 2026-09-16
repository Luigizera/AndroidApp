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

public class InsertFragment extends Fragment {

    public static final String TAG = "InsertFragment";
    private EditText editTextFirstName, editTextLastName;
    private TextView textviewError;
    private ImageButton submitButton;
    private android.widget.Button buttonBack;

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
        editTextFirstName = view.findViewById(R.id.fragment_settings_firstname);
        editTextLastName = view.findViewById(R.id.fragment_settings_lastname);
        textviewError = view.findViewById(R.id.fragment_settings_textview_error);
        submitButton = view.findViewById(R.id.fragment_settings_submitbutton);
        buttonBack = view.findViewById(R.id.fragment_settings_backbutton);

        buttonBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearErrors();
                UserDao userDao = database.userDao();

                if(editTextFirstName.getText().toString().isEmpty()) {
                    showError(R.string.error_empty_firstname, editTextFirstName);
                    return;
                }
                if(editTextLastName.getText().toString().isEmpty()) {
                    showError(R.string.error_empty_lastname, editTextLastName);
                    return;
                }
                textviewError.setVisibility(View.INVISIBLE);
                userDao.insertAll(new User(editTextFirstName.getText().toString(), editTextLastName.getText().toString()));
                clearFields();
            }
        });
        return view;
    }

    private void showError(int resId, View view) {
        textviewError.setVisibility(View.VISIBLE);
        textviewError.setText(resId);
        if (view != null) {
            int colorError = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.red);
            androidx.core.view.ViewCompat.setBackgroundTintList(view, android.content.res.ColorStateList.valueOf(colorError));
        }
    }

    private void clearErrors() {
        textviewError.setVisibility(View.INVISIBLE);
        int colorDefault = android.graphics.Color.BLACK;
        android.util.TypedValue typedValue = new android.util.TypedValue();
        if (requireContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true)) {
            colorDefault = typedValue.data;
        }
        android.content.res.ColorStateList tintList = android.content.res.ColorStateList.valueOf(colorDefault);
        androidx.core.view.ViewCompat.setBackgroundTintList(editTextFirstName, tintList);
        androidx.core.view.ViewCompat.setBackgroundTintList(editTextLastName, tintList);
    }

    private void clearFields() {
        clearErrors();
        editTextFirstName.setText("");
        editTextLastName.setText("");
    }
}

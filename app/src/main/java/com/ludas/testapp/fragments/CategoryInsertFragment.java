package com.ludas.testapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.Category;
import com.ludas.testapp.database.CategoryDao;
import com.ludas.testapp.database.User;

public class CategoryInsertFragment extends Fragment {
    public static final String TAG = "CategoryInsertFragment";

    private EditText editTextName;
    private TextView textViewError;
    private ImageButton imageButtonSubmit;
    private CategoryDao categoryDao;

    public CategoryInsertFragment() {
        // Required empty public constructor
    }

    public static CategoryInsertFragment newInstance() {
        CategoryInsertFragment fragment = new CategoryInsertFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryDao = AppDatabase.getInstance(getActivity()).categoryDao();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_insert, container, false);
        editTextName = view.findViewById(R.id.fragment_category_insert_edittext_name);
        textViewError = view.findViewById(R.id.fragment_category_insert_textview_error);
        imageButtonSubmit = view.findViewById(R.id.fragment_category_insert_submitbutton);

        Button buttonBack = view.findViewById(R.id.fragment_category_insert_backbutton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextName.getText().toString().isEmpty()) {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(R.string.error_empty_name);
                    return;
                }
                if(categoryDao.findByNameEquals(editTextName.getText().toString()) != null) {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(R.string.category_error_already_exists);
                    return;
                }
                textViewError.setVisibility(View.INVISIBLE);
                categoryDao.insertAll(new Category(editTextName.getText().toString()));
                editTextName.setText("");
            }
        });
        return view;
    }
}
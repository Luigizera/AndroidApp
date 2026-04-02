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
import com.ludas.testapp.database.Category;
import com.ludas.testapp.database.CategoryDao;
import com.ludas.testapp.database.User;

public class CategoryInsertFragment extends Fragment {
    public static final String TAG = "CategoryInsertFragment";

    private EditText name;
    private TextView textviewError;
    private ImageButton submitButton;
    private AppDatabase database;

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
        database = AppDatabase.getInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_insert, container, false);
        name = view.findViewById(R.id.fragment_category_insert_name);
        textviewError = view.findViewById(R.id.fragment_category_insert_textview_error);
        submitButton = view.findViewById(R.id.fragment_category_insert_submitbutton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryDao categoryDao = database.categoryDao();

                if(name.getText().toString().isEmpty()) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.category_error_empty_name);
                    return;
                }
                if(categoryDao.findByNameEquals(name.getText().toString()) != null) {
                    textviewError.setVisibility(View.VISIBLE);
                    textviewError.setText(R.string.category_error_already_exists);
                    return;
                }
                textviewError.setVisibility(View.INVISIBLE);
                categoryDao.insertAll(new Category(name.getText().toString()));
                name.setText("");
            }
        });
        return view;
    }
}
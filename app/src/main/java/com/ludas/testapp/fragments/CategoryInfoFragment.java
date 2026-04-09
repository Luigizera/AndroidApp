package com.ludas.testapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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


public class CategoryInfoFragment extends Fragment {

    public static final String TAG = "CategoryInfoFragment";
    private static final String ARG_CATEGORYID = "categoryId";
    private long categoryId;
    private TextView textViewError;
    private EditText editTextName;
    private ImageButton imageButtonSubmit;
    private CategoryDao categoryDao;
    private Category category;


    public CategoryInfoFragment() {
        // Required empty public constructor
    }

    public static CategoryInfoFragment newInstance(long categoryId) {
        CategoryInfoFragment fragment = new CategoryInfoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORYID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.categoryId = getArguments().getLong(ARG_CATEGORYID);
            if(categoryId >= 0) {
                categoryDao = AppDatabase.getInstance(getActivity()).categoryDao();
                category = categoryDao.findById(categoryId);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_info, container, false);
        if(category != null) {
            editTextName = view.findViewById(R.id.fragment_category_info_name);
            editTextName.setText(category.getName());
            textViewError = view.findViewById(R.id.fragment_category_info_textview_error);
            imageButtonSubmit = view.findViewById(R.id.fragment_category_info_submitbutton);

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
                    category.setName(editTextName.getText().toString());
                    textViewError.setVisibility(View.INVISIBLE);
                    categoryDao.updateCategories(category);
                    Bundle result = new Bundle();
                    result.putBoolean("refresh_key", true);

                    getParentFragmentManager().setFragmentResult("request_key", result);
                    getParentFragmentManager().popBackStack();
                }
            });
        }
        return view;
    }
}
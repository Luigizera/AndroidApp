package com.ludas.testapp.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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


public class CategoryInfoFragment extends Fragment {

    public static final String TAG = "CategoryInfoFragment";
    private static final String ARG_CATEGORYID = "categoryId";
    private long categoryId;
    private TextView textViewError;
    private EditText editTextName;
    private ImageButton imageButtonSubmit;
    private ImageButton imageButtonDelete;
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
            editTextName = view.findViewById(R.id.fragment_category_info_edittext_name);
            editTextName.setText(category.getName());
            textViewError = view.findViewById(R.id.fragment_category_info_textview_error);
            imageButtonSubmit = view.findViewById(R.id.fragment_category_info_submitbutton);
            imageButtonDelete = view.findViewById(R.id.fragment_category_info_deletebutton);

            Button buttonBack = view.findViewById(R.id.fragment_category_info_backbutton);
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
                    category.setName(editTextName.getText().toString());
                    textViewError.setVisibility(View.INVISIBLE);
                    categoryDao.updateCategories(category);
                    Bundle result = new Bundle();
                    result.putBoolean("refresh_key", true);

                    getParentFragmentManager().setFragmentResult("request_key", result);
                    getParentFragmentManager().popBackStack();
                }
            });

            imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialAlertDialogBuilder(getActivity())
                            .setTitle(category.getName())
                            .setMessage(R.string.category_info_delete_confirmation)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    categoryDao.delete(category);
                                    Bundle result = new Bundle();
                                    result.putBoolean("refresh_key", true);

                                    getParentFragmentManager().setFragmentResult("request_key", result);
                                    getParentFragmentManager().popBackStack();
                                }})
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                }
            });
        }
        return view;
    }
}
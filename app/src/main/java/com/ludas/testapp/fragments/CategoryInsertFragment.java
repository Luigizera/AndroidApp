package com.ludas.testapp.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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

public class CategoryInsertFragment extends Fragment {
    public static final String TAG = "CategoryInsertFragment";

    private EditText editTextName, editTextColor;
    private View viewColorPreview;
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
        editTextColor = view.findViewById(R.id.fragment_category_insert_edittext_color);
        viewColorPreview = view.findViewById(R.id.fragment_category_insert_color_preview);
        textViewError = view.findViewById(R.id.fragment_category_insert_textview_error);
        imageButtonSubmit = view.findViewById(R.id.fragment_category_insert_submitbutton);

        editTextColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String colorStr = s.toString();
                    if (!colorStr.startsWith("#")) colorStr = "#" + colorStr;
                    int color = Color.parseColor(colorStr);
                    viewColorPreview.setBackgroundColor(color);
                } catch (Exception e) {
                    viewColorPreview.setBackgroundColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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
                String colorHex = editTextColor.getText().toString();
                if (colorHex.isEmpty()) colorHex = "#000000";
                if (!colorHex.startsWith("#")) colorHex = "#" + colorHex;
                
                try {
                    Color.parseColor(colorHex);
                } catch (Exception e) {
                    colorHex = "#000000";
                }

                categoryDao.insertAll(new Category(editTextName.getText().toString(), colorHex));
                editTextName.setText("");
                editTextColor.setText("");
                viewColorPreview.setBackgroundColor(Color.BLACK);
            }
        });
        return view;
    }
}
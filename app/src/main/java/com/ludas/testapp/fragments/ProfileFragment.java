package com.ludas.testapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ludas.testapp.R;
import com.ludas.testapp.database.User;

public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";
    private static final String ARG_USERDATE = "userDate";
    private String userDate;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String userDate) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERDATE, userDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.userDate = getArguments().getString(ARG_USERDATE);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        if(userDate != null) {
            TextView textView = view.findViewById(R.id.fragment_profile_textview);
            textView.setText(userDate);
        }

        return view;
    }
}
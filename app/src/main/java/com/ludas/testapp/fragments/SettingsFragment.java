package com.ludas.testapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ludas.testapp.R;

public class SettingsFragment extends Fragment {
    public static final String TAG = "Settings";
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME = "selected_theme";

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        RadioGroup themeRadioGroup = view.findViewById(R.id.theme_radio_group);
        RadioButton radioLight = view.findViewById(R.id.radio_light);
        RadioButton radioDark = view.findViewById(R.id.radio_dark);
        RadioButton radioSystem = view.findViewById(R.id.radio_system);

        RadioGroup languageRadioGroup = view.findViewById(R.id.language_radio_group);
        RadioButton radioLangEn = view.findViewById(R.id.radio_lang_en);
        RadioButton radioLangPt = view.findViewById(R.id.radio_lang_pt);

        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        
        // Theme selection logic
        int savedTheme = prefs.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (savedTheme == AppCompatDelegate.MODE_NIGHT_NO) {
            radioLight.setChecked(true);
        } else if (savedTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            radioDark.setChecked(true);
        } else {
            radioSystem.setChecked(true);
        }

        themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int mode;
            if (checkedId == R.id.radio_light) {
                mode = AppCompatDelegate.MODE_NIGHT_NO;
            } else if (checkedId == R.id.radio_dark) {
                mode = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }

            AppCompatDelegate.setDefaultNightMode(mode);
            prefs.edit().putInt(KEY_THEME, mode).apply();
        });

        // Language selection logic
        String currentLang = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        if (currentLang.startsWith("pt")) {
            radioLangPt.setChecked(true);
        } else {
            radioLangEn.setChecked(true);
        }

        languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String localeTag;
            if (checkedId == R.id.radio_lang_pt) {
                localeTag = "pt-BR";
            } else {
                localeTag = "en";
            }

            LocaleListCompat appLocales = LocaleListCompat.forLanguageTags(localeTag);
            AppCompatDelegate.setApplicationLocales(appLocales);
        });

        return view;
    }
}
package com.ludas.testapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.ludas.testapp.databinding.ActivityMainBinding;
import com.ludas.testapp.fragments.HomeFragment;
import com.ludas.testapp.fragments.ProfileFragment;
import com.ludas.testapp.fragments.InsertFragment;

public class MainActivity extends AppCompatActivity implements HomeFragmentAdapter.OnListClicked {
    public static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        if(savedInstanceState == null) {
            addFragment(HomeFragment.newInstance(), HomeFragment.TAG);
            toolbar.setTitle(R.string.title_fragment_home);
        }
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.bottom_nav_menu_home) {
                replaceFragment(HomeFragment.newInstance(), HomeFragment.TAG);
                toolbar.setTitle(R.string.title_fragment_home);
            }
            if(itemId == R.id.bottom_nav_menu_profile) {
                replaceFragment(ProfileFragment.newInstance(null), ProfileFragment.TAG);
                toolbar.setTitle(R.string.title_fragment_profile);
            }
            if(itemId == R.id.bottom_nav_menu_settings) {
                replaceFragment(InsertFragment.newInstance(), InsertFragment.TAG);
                toolbar.setTitle(R.string.title_fragment_insert);
            }

            return true;
        });
    }

    private void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_framelayout, fragment, tag)
                .commit();
    }

    private void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_framelayout, fragment, tag)
                .commit();
    }
    private void replaceWithBackStack(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_framelayout, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onSelected(String userDate) {
        replaceWithBackStack(ProfileFragment.newInstance(userDate), ProfileFragment.TAG);
        toolbar.setTitle(R.string.title_fragment_profile);
    }
}
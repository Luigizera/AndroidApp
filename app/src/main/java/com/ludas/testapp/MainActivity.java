package com.ludas.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.User;
import com.ludas.testapp.database.UserDao;
import com.ludas.testapp.databinding.ActivityMainBinding;
import com.ludas.testapp.fragments.HomeFragment;
import com.ludas.testapp.fragments.ProfileFragment;
import com.ludas.testapp.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements HomeFragmentAdapter.OnListClicked {
    public static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    String databaseName;

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
        if(savedInstanceState == null) {
            addFragment(HomeFragment.newInstance(), HomeFragment.TAG);
        }
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.bottom_nav_menu_home) {
                replaceFragment(HomeFragment.newInstance(), HomeFragment.TAG);
            }
            if(itemId == R.id.bottom_nav_menu_profile) {
                replaceFragment(ProfileFragment.newInstance(User.NULL_ID), ProfileFragment.TAG);
            }
            if(itemId == R.id.bottom_nav_menu_settings) {
                replaceFragment(SettingsFragment.newInstance(), SettingsFragment.TAG);
            }

            return true;
        });
    }

    private void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout, fragment, tag)
                .commit();
    }

    private void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment, tag)
                .commit();
    }
    private void replaceWithBackStack(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onSelected(int userId) {
        replaceWithBackStack(ProfileFragment.newInstance(userId), ProfileFragment.TAG);
    }
}
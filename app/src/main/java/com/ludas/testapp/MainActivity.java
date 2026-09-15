package com.ludas.testapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.ludas.testapp.adapters.CategoryFragmentAdapter;
import com.ludas.testapp.adapters.HomeFragmentAdapter;
import com.ludas.testapp.adapters.ProductFragmentAdapter;
import com.ludas.testapp.adapters.StorageFragmentAdapter;
import com.ludas.testapp.adapters.StorageLogFragmentAdapter;
import com.ludas.testapp.databinding.ActivityMainBinding;
import com.ludas.testapp.fragments.CategoryFragment;
import com.ludas.testapp.fragments.CategoryInfoFragment;
import com.ludas.testapp.fragments.CategoryInsertFragment;
import com.ludas.testapp.fragments.GraphFragment;
import com.ludas.testapp.fragments.HomeFragment;
import com.ludas.testapp.fragments.ProductFragment;
import com.ludas.testapp.fragments.ProductInfoFragment;
import com.ludas.testapp.fragments.ProductInsertFragment;
import com.ludas.testapp.fragments.ProfileFragment;
import com.ludas.testapp.fragments.InsertFragment;
import com.ludas.testapp.fragments.SettingsFragment;
import com.ludas.testapp.fragments.StorageFragment;
import com.ludas.testapp.fragments.StorageInfoFragment;
import com.ludas.testapp.fragments.StorageInsertFragment;
import com.ludas.testapp.fragments.StorageLogFragment;


public class MainActivity extends AppCompatActivity implements
        HomeFragmentAdapter.OnListClicked,
        CategoryFragmentAdapter.OnListClicked,
        ProductFragmentAdapter.OnListClicked,
        StorageFragmentAdapter.OnListClicked,
        StorageLogFragmentAdapter.OnListClicked {
    public static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    FragmentManager fragmentManager;
    DrawerLayout drawerLayout;
    NavigationView leftNavigationView;
    //ActionBarDrawerToggle toggle;
    MaterialToolbar toolbar;


    /*TODO:
       - ANTES DE DELETAR DO DATABASE FAZER VERIFICAÇÃO DE TABELAS
       - https://github.com/codepath/android_guides/wiki/Developing-Custom-Themes
       - https://www.geeksforgeeks.org/android/how-to-implement-custom-searchable-spinner-in-android/
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        int savedTheme = prefs.getInt("selected_theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedTheme);

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

        fragmentManager = getSupportFragmentManager();
        toolbar = findViewById(R.id.activity_main_appbarlayout_toolbar);
        drawerLayout = findViewById(R.id.main);
        leftNavigationView = findViewById(R.id.activity_main_leftnavview);
        leftNavigationView.bringToFront();

        if(savedInstanceState == null) {
            addFragment(HomeFragment.newInstance(), HomeFragment.TAG);
        }
        binding.activityMainAppbarlayoutToolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.top_nav_menu_settings) {
                replaceFragment(SettingsFragment.newInstance(), SettingsFragment.TAG);
            }
            return true;
        });
        leftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.left_nav_menu_home) {
                    replaceFragment(HomeFragment.newInstance(), HomeFragment.TAG);
                }
                else if (itemId == R.id.left_nav_menu_graph) {
                    replaceFragment(GraphFragment.newInstance(), GraphFragment.TAG);
                }
                else if (itemId == R.id.left_nav_menu_category) {
                    replaceFragment(CategoryFragment.newInstance(1), CategoryFragment.TAG);
                }
                else if(itemId == R.id.left_nav_menu_product) {
                    replaceFragment(ProductFragment.newInstance(), ProductFragment.TAG);
                }
                else if(itemId == R.id.left_nav_menu_storage) {
                    replaceFragment(StorageFragment.newInstance(), StorageFragment.TAG);
                }
                else if(itemId == R.id.left_nav_menu_storage_log) {
                    replaceFragment(StorageLogFragment.newInstance(), StorageLogFragment.TAG);
                }

                if(drawerLayout.isDrawerOpen(leftNavigationView)) {
                    drawerLayout.closeDrawer(leftNavigationView);
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!drawerLayout.isDrawerOpen(leftNavigationView)) {
                    drawerLayout.openDrawer(leftNavigationView);
                }
            }
        });
    }

    private void addFragment(Fragment fragment, String tag) {
        fragmentManager
                .beginTransaction()
                .add(R.id.activity_main_framelayout, fragment, tag)
                .commit();
    }

    private void replaceFragment(Fragment fragment, String tag) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_framelayout, fragment, tag)
                .commit();
    }
    private void replaceWithBackStack(Fragment fragment, String tag) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_framelayout, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSelected(String userDate) {
        replaceWithBackStack(ProfileFragment.newInstance(userDate), ProfileFragment.TAG);
    }

    @Override
    public void onCategorySelected(long categoryId) {
        replaceWithBackStack(CategoryInfoFragment.newInstance(categoryId), CategoryInfoFragment.TAG);
    }

    @Override
    public void onProductSelected(long productId) {
        replaceWithBackStack(ProductInfoFragment.newInstance(productId), ProductInfoFragment.TAG);
    }

    @Override
    public void onStorageSelected(long storageId) {
        replaceWithBackStack(StorageInfoFragment.newInstance(storageId), StorageInfoFragment.TAG);
    }

    @Override
    public void onStorageLogSelected(long storageLogId) {

    }


    /*binding.activityMainBottomnavview.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.bottom_nav_menu_home) {
                replaceFragment(HomeFragment.newInstance(), HomeFragment.TAG);
            }
            if(itemId == R.id.bottom_nav_menu_profile) {
                replaceFragment(ProfileFragment.newInstance(null), ProfileFragment.TAG);
            }
            if(itemId == R.id.bottom_nav_menu_add) {
                replaceFragment(InsertFragment.newInstance(), InsertFragment.TAG);
            }

            return true;
        });

        >> USADO PARA DEBUG
        leftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.left_nav_menu_home) {
                    replaceFragment(HomeFragment.newInstance(), HomeFragment.TAG);
                }
                else if(itemId == R.id.left_nav_menu_profile) {
                    replaceFragment(ProfileFragment.newInstance(null), ProfileFragment.TAG);
                }
                else if(itemId == R.id.left_nav_menu_add) {
                    replaceFragment(InsertFragment.newInstance(), InsertFragment.TAG);
                }

                else if (itemId == R.id.left_nav_menu_category_home) {
                    replaceFragment(CategoryFragment.newInstance(), CategoryFragment.TAG);
                }
                else if(itemId == R.id.left_nav_menu_category_info) {
                    replaceFragment(CategoryInfoFragment.newInstance(-1), CategoryInfoFragment.TAG);
                }
                else if(itemId == R.id.left_nav_menu_category_add) {
                    replaceFragment(CategoryInsertFragment.newInstance(), CategoryInsertFragment.TAG);
                }

                else if(itemId == R.id.left_nav_menu_product_home) {
                    replaceFragment(ProductFragment.newInstance(), ProductFragment.TAG);
                }
                else if(itemId == R.id.left_nav_menu_product_info) {
                    replaceFragment(ProductInfoFragment.newInstance(-1), ProductInfoFragment.TAG);
                }
                else if(itemId == R.id.left_nav_menu_product_add) {
                    replaceFragment(ProductInsertFragment.newInstance(), ProductInsertFragment.TAG);
                }

                else if(itemId == R.id.left_nav_menu_storage_home) {
                    replaceFragment(StorageFragment.newInstance(), StorageFragment.TAG);
                }
                else if(itemId == R.id.left_nav_menu_storage_info) {
                    replaceFragment(StorageInfoFragment.newInstance(-1), StorageInfoFragment.TAG);
                }
                else if(itemId == R.id.left_nav_menu_storage_add) {
                    replaceFragment(StorageInsertFragment.newInstance(), StorageInsertFragment.TAG);
                }

                if(drawerLayout.isDrawerOpen(leftNavigationView)) {
                    drawerLayout.closeDrawer(leftNavigationView);
                }
                return true;
            }
        });*/
}
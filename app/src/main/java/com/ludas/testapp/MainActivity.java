package com.ludas.testapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.ludas.testapp.adapters.CategoryFragmentAdapter;
import com.ludas.testapp.adapters.HomeFragmentAdapter;
import com.ludas.testapp.adapters.ProductFragmentAdapter;
import com.ludas.testapp.databinding.ActivityMainBinding;
import com.ludas.testapp.fragments.CategoryFragment;
import com.ludas.testapp.fragments.CategoryInfoFragment;
import com.ludas.testapp.fragments.CategoryInsertFragment;
import com.ludas.testapp.fragments.HomeFragment;
import com.ludas.testapp.fragments.ProductFragment;
import com.ludas.testapp.fragments.ProductInfoFragment;
import com.ludas.testapp.fragments.ProductInsertFragment;
import com.ludas.testapp.fragments.ProfileFragment;
import com.ludas.testapp.fragments.InsertFragment;
import com.ludas.testapp.fragments.SettingsFragment;



public class MainActivity extends AppCompatActivity implements
        HomeFragmentAdapter.OnListClicked, CategoryFragmentAdapter.OnListClicked, ProductFragmentAdapter.OnListClicked {
    public static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    DrawerLayout drawerLayout;
    NavigationView leftNavigationView;
    //ActionBarDrawerToggle toggle;
    MaterialToolbar toolbar;



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

        toolbar = findViewById(R.id.activity_main_appbarlayout_toolbar);
        drawerLayout = findViewById(R.id.main);
        leftNavigationView = findViewById(R.id.activity_main_leftnavview);


        if(savedInstanceState == null) {
            addFragment(HomeFragment.newInstance(), HomeFragment.TAG);
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
        });*/
        binding.activityMainAppbarlayoutToolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.top_nav_menu_settings) {
                replaceFragment(SettingsFragment.newInstance(), SettingsFragment.TAG);
            }
            return true;
        });
        leftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.left_nav_menu_home) {
                    replaceFragment(HomeFragment.newInstance(), HomeFragment.TAG);
                }
                if(itemId == R.id.left_nav_menu_profile) {
                    replaceFragment(ProfileFragment.newInstance(null), ProfileFragment.TAG);
                }
                if(itemId == R.id.left_nav_menu_add) {
                    replaceFragment(InsertFragment.newInstance(), InsertFragment.TAG);
                }

                if (itemId == R.id.left_nav_menu_category_home) {
                    replaceFragment(CategoryFragment.newInstance(), CategoryFragment.TAG);
                }
                if(itemId == R.id.left_nav_menu_category_info) {
                    replaceFragment(CategoryInfoFragment.newInstance(-1), CategoryInfoFragment.TAG);
                }
                if(itemId == R.id.left_nav_menu_category_add) {
                    replaceFragment(CategoryInsertFragment.newInstance(), CategoryInsertFragment.TAG);
                }

                if (itemId == R.id.left_nav_menu_product_home) {
                    replaceFragment(ProductFragment.newInstance(), ProductFragment.TAG);
                }
                if(itemId == R.id.left_nav_menu_product_info) {
                    replaceFragment(ProductInfoFragment.newInstance(-1), ProductInfoFragment.TAG);
                }
                if(itemId == R.id.left_nav_menu_product_add) {
                    replaceFragment(ProductInsertFragment.newInstance(), ProductInsertFragment.TAG);
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
    }

    @Override
    public void onCategorySelected(long categoryId) {
        replaceWithBackStack(CategoryInfoFragment.newInstance(categoryId), CategoryInfoFragment.TAG);
    }

    @Override
    public void onProductSelected(long productId) {
        replaceWithBackStack(ProductInfoFragment.newInstance(productId), ProductInfoFragment.TAG);
    }
}
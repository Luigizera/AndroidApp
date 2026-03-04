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

public class MainActivity extends AppCompatActivity {
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

        String databaseName = "database-name";

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            switch (itemId) {
                case R.id.bottom_nav_menu_home: {

                    break;
                }
            }
            if (itemId == R.id.bottom_nav_menu_home) {
                replaceFragment(new HomeFragment());
            }
            if(itemId == R.id.bottom_nav_menu_profile) {
                replaceFragment(new ProfileFragment());
            }
            if(itemId == R.id.bottom_nav_menu_settings) {
                replaceFragment(SettingsFragment.newInstance(databaseName));
            }

            return true;
        });

        /*t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        submit_button = findViewById(R.id.submit_button);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new bgThread().start();
            }
        });
        */
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    /*class bgThread extends Thread {
        public void run() {
            super.run();
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name").allowMainThreadQueries().build();

            UserDao userDao = db.userDao();
            if(t1.getText().toString().isEmpty()) {
                return;
            }
            if(t2.getText().toString().isEmpty()) {
                return;
            }
            userDao.insertAll(new User(t1.getText().toString(), t2.getText().toString()));
            t1.setText("");
            t2.setText("");
        }
    }*/
}
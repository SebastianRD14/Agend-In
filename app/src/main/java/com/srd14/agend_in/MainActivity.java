package com.srd14.agend_in;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.srd14.agend_in.add_task_fragment;
import com.srd14.agend_in.calendar_fragment;
import com.srd14.agend_in.edit_task_fragment;
import com.srd14.agend_in.settings_fragment;
import com.srd14.agend_in.task_detail_fragment;
import com.srd14.agend_in.task_fragment;
import com.srd14.agend_in.tasklist_fragment;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new tasklist_fragment()).commit();
        }
    }

    private boolean NavigationBarView;
    NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    int itemId = item.getItemId();


                    if (itemId == R.id.item_1) {
                        selectedFragment = new tasklist_fragment();

                    } else if (itemId == R.id.item_2) {
                        selectedFragment = new calendar_fragment();

                    } else if (itemId == R.id.item_3) {
                        selectedFragment = new settings_fragment();

                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment).commit();
                    }

                    return true;
                }
            };

}
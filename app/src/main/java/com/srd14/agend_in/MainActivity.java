package com.srd14.agend_in;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // --- Leer configuración guardada del modo oscuro ---
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new tasklist_fragment()).commit();
        }
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
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

package com.srd14.agend_in;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private LightSensorHelper sensorHelper;
    private Boolean currentMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- Iniciar el sensor con callback ---
        sensorHelper = new LightSensorHelper(this, new LightSensorHelper.LightCallBack() {

            // No se usa para este sensor de luz pero se necesita agregar
            @Override
            public void onLightChanged(float lux) {
            }
            @Override
            public void onModeChanged(boolean isDark) {
                if (isDark) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


        // --- Tema oscuro ---
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        currentMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);


        // --- Navegación ---

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new tasklist_fragment()).commit();
        }

        // Mandar al usuario a habilitar las notificaciones
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        // Permiso para alarmas exactas
        permisoAlarmaExacta(this);

    }
    // Control para que el sensor solo funcione si está encendida la app
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorHelper != null) sensorHelper.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorHelper != null) sensorHelper.stop();
    }
    // Clase para el permiso de las alarmas exactas
    public static void permisoAlarmaExacta(Context context) {
        // Verificamos si el permiso ya está otorgado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Creamos un alarm manager
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // Si no está otorgado, lo pedimos
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(context, "⚠️ Se necesita permiso para alarmas exactas.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
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

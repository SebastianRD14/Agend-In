package com.srd14.agend_in;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

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

import java.util.Calendar;


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
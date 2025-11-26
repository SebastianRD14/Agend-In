package com.srd14.agend_in;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import com.srd14.agend_in.R;

public class settings_fragment extends Fragment {

    private SwitchCompat switchManualMode, switchNotifications, switchAuto;
    private SharedPreferences prefs;

    private RadioGroup radioGroup;
    private RadioButton radioLight, radioDark;

    // Para evitar que los listeners se disparen solos
    private boolean ignoreAuto = false;
    private boolean ignoreManual = false;
    private boolean ignoreRadios = false;
    private boolean ignoreNotifs = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.settings_view, container, false);

        // Variables
        switchManualMode = view.findViewById(R.id.switchManualMode);
        switchNotifications = view.findViewById(R.id.switchNotifications);
        switchAuto = view.findViewById(R.id.switchAutoMode);

        radioGroup = view.findViewById(R.id.radioGroupTheme);
        radioLight = view.findViewById(R.id.radioLight);
        radioDark = view.findViewById(R.id.radioDark);

        prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

        // ----------------- PRIMERA VEZ QUE CORRE LA APP  -----------------
        // ----------------- SIN DISPARAR LISTENER -----------------
        ignoreNotifs = true;

        // Obtener si Android permite notificaciones
        boolean enabled = prefs.getBoolean("notificaciones_activadas", true);

        // Mostrar en el switch
        switchNotifications.setChecked(enabled);

        // Guardar la primera vez que se activa
        boolean firstLaunch = prefs.getBoolean("firstLaunch_notifs", true);
        if (firstLaunch) {
            prefs.edit()
                    .putBoolean("notificaciones_activadas", enabled) // sincroniza con Android
                    .putBoolean("firstLaunch_notifs", false)
                    .apply();
        }

        ignoreNotifs = false;

        // ----------------- Restauración inicial (SIN DISPARAR listeners) -----------------
        ignoreAuto = true;
        ignoreManual = true;
        ignoreRadios = true;
        ignoreNotifs = true;

        boolean auto = prefs.getBoolean("autoMode", false);
        boolean manual = prefs.getBoolean("manualMode", false);
        String theme = prefs.getString("manualSelection", "light");

        switchAuto.setChecked(auto);
        switchManualMode.setChecked(manual);

        if (manual) {
            radioGroup.setVisibility(View.VISIBLE);
            if (theme.equals("dark")) {
                radioDark.setChecked(true);
            } else {
                radioLight.setChecked(true);
            }
        } else {
            radioGroup.setVisibility(View.GONE);
            radioGroup.clearCheck();
        }

        ignoreAuto = false;
        ignoreManual = false;
        ignoreRadios = false;
        ignoreNotifs = false;

        // ----------------- SWITCH NOTIFICACIONES -----------------
        switchNotifications.setOnCheckedChangeListener((button, isOn) -> {
            if (ignoreNotifs) return;

            prefs.edit().putBoolean("notificaciones_activadas", isOn).apply();
        });

        // ----------------- MODO MANUAL -----------------
        switchManualMode.setOnCheckedChangeListener((btn, isOn) -> {
            if (ignoreManual) return;

            prefs.edit().putBoolean("manualMode", isOn).apply();

            if (isOn) {
                // Si activa manual, apaga auto
                ignoreAuto = true;
                switchAuto.setChecked(false);
                ignoreAuto = false;

                radioGroup.setVisibility(View.VISIBLE);

                // Activar el radio guardado
                String saved = prefs.getString("manualSelection", "light");
                ignoreRadios = true;
                if (saved.equals("dark")) radioDark.setChecked(true);
                else radioLight.setChecked(true);
                ignoreRadios = false;

                // Aplicar inmediatamente
                applyManualTheme();
            } else {
                radioGroup.setVisibility(View.GONE);
            }
        });

        // ----------------- MODO AUTOMÁTICO -----------------
        switchAuto.setOnCheckedChangeListener((btn, isOn) -> {
            if (ignoreAuto) return;

            prefs.edit().putBoolean("autoMode", isOn).apply();

            if (isOn) {
                // si activa automático → apaga manual
                ignoreManual = true;
                switchManualMode.setChecked(false);
                ignoreManual = false;

                radioGroup.setVisibility(View.GONE);
                radioGroup.clearCheck();

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                requireActivity().recreate();
            }
        });

        // ----------------- RADIO LIGHT -----------------
        radioLight.setOnCheckedChangeListener((btn, checked) -> {
            if (ignoreRadios || !checked) return;

            prefs.edit().putString("manualSelection", "light").apply();
            applyManualTheme();
        });

        // ----------------- RADIO DARK -----------------
        radioDark.setOnCheckedChangeListener((btn, checked) -> {
            if (ignoreRadios || !checked) return;

            prefs.edit().putString("manualSelection", "dark").apply();
            applyManualTheme();
        });

        return view;
    }

    // ----------------- APLICAR TEMA MANUAL -----------------
    private void applyManualTheme() {
        boolean dark = prefs.getString("manualSelection", "light").equals("dark");

        AppCompatDelegate.setDefaultNightMode(
                dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        // recrear la actividad para que se aplique el tema
        requireActivity().recreate();
    }



}
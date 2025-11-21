package com.srd14.agend_in;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SwitchCompat;
import com.srd14.agend_in.R;

public class settings_fragment extends Fragment {

    private SwitchCompat switchDarkMode, switchNotifications, switchReminders;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_view, container, false);

        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        switchNotifications = view.findViewById(R.id.switchNotifications);
        switchReminders = view.findViewById(R.id.switchReminders);

        prefs = requireActivity().getSharedPreferences("settings", getContext().MODE_PRIVATE);

        // Restaurar estado guardado
        switchDarkMode.setChecked(prefs.getBoolean("darkMode", false));
        switchNotifications.setChecked(prefs.getBoolean("notifications", true));
        switchReminders.setChecked(prefs.getBoolean("autoReminders", true));

        // --- Tema oscuro ---
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("darkMode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        // --- Notificaciones ---
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notifications", isChecked).apply();
        });

        // --- Recordatorios automáticos ---
        switchReminders.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("autoReminders", isChecked).apply();
        });

        return view;
    }
}
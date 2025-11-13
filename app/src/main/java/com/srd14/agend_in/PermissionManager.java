
package com.srd14.agend_in;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {

    public static final int REQUEST_CODE_POST_NOTIFICATIONS = 101;
    public static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 102;

    public static boolean hasPostNotificationsPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestPostNotificationsPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
        }
    }

    public static boolean canScheduleExactAlarms(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            return alarmManager != null && alarmManager.canScheduleExactAlarms();
        }
        return true; // Not needed for older versions
    }

    public static void requestScheduleExactAlarmPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Toast.makeText(context, "Se necesita permiso para programar alarmas.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            context.startActivity(intent);
        }
    }
}


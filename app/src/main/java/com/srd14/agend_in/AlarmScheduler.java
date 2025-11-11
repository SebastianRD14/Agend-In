package com.srd14.agend_in;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmScheduler {


    public static void scheduleTaskAlarm(Context context, Task task) {
        try {
            // Convertimos la fecha y hora (String) de la tarea en un objeto Calendar
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(task.getDate() + " " + task.getTime()));

            long triggerTime = calendar.getTimeInMillis();
            if (triggerTime < System.currentTimeMillis()) {
                Toast.makeText(context, "❌ La hora ya pasó, no se programó la alarma.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Intent que activará al ReminderReceiver
            Intent intent = new Intent(context, ReminderReceiver.class);
            intent.putExtra("taskName", task.getName());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    task.getId(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // Programamos la alarma
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            // Permiso alarmas exactas
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                } else {
                    Toast.makeText(context, "⚠️ No tienes permiso para alarmas exactas.", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                // Versiones anteriores
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
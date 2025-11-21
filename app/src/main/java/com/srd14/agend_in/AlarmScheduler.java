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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Calendar taskCalendar = Calendar.getInstance();
            taskCalendar.setTime(sdf.parse(task.getDate() + " " + task.getTime()));

            long taskTime = taskCalendar.getTimeInMillis();
            if (taskTime < System.currentTimeMillis()) {
                Toast.makeText(context, "❌ La hora ya pasó, no se programó la alarma.", Toast.LENGTH_SHORT).show();
                return;
            }

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


            // Días de anticipación según prioridad
            int[] diasAntes;
            switch (task.getPriority()) {
                case "Alta":
                    diasAntes = new int[]{5, 3, 1, 0};
                    break;
                case "Media":
                    diasAntes = new int[]{5, 2, 1, 0};
                    break;
                case "Baja":
                    diasAntes = new int[]{7, 1, 0};
                    break;
                default:
                    diasAntes = new int[]{1}; // Por si no tiene prioridad asignada
                    break;
            }

            for (int dias : diasAntes) {
                Calendar reminderCalendar = (Calendar) taskCalendar.clone();
                reminderCalendar.add(Calendar.DAY_OF_MONTH, -dias);

                long triggerTime = reminderCalendar.getTimeInMillis();
                if (triggerTime < System.currentTimeMillis()) continue; // Saltar si ya pasó

                Intent intent = new Intent(context, ReminderReceiver.class);
                intent.putExtra("taskName", task.getName());
                intent.putExtra("taskPriority", task.getPriority());
                intent.putExtra("taskId", task.getId());

                // ID único para cada recordatorio
                int requestCode = task.getId() * 10 + dias;

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                    }
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                }

                Log.d("AlarmScheduler", "⏰ Programada notificación para " + dias + " días antes de " + task.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
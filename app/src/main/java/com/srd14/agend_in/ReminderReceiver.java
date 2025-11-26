package com.srd14.agend_in;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class ReminderReceiver extends BroadcastReceiver {
    @Override

    // Este método se ejecuta cuando se programa una alarma
    public void onReceive(Context context, Intent intent) {
        // Verificar preferencia antes de mostrar
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean notifsEnabled = prefs.getBoolean("notificaciones_activadas", true);

        if (!notifsEnabled) {
            return; // ❌ Notificaciones desactivadas → NO mostrar
        }
        String taskName = intent.getStringExtra("taskName");
        String taskPriority = intent.getStringExtra("taskPriority");
        int taskId = intent.getIntExtra("taskId", -1);
        NotificationHelper.showNotification(context, "Recordatorio de tarea", taskName, taskPriority, taskId);
    }
}

package com.srd14.agend_in;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {
    @Override

    // Este método se ejecuta cuando se programa una alarma
    public void onReceive(Context context, Intent intent) {
        String taskName = intent.getStringExtra("taskName");
        NotificationHelper.showNotification(context, "Recordatorio de tarea", taskName);
    }
}

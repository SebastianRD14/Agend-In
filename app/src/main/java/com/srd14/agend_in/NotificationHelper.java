package com.srd14.agend_in;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class NotificationHelper {
    // Variable para el canal de notificaciones
    private static final String CHANNEL_ID = "task_channel";

    // Método para mostrar la notificación
    public static void showNotification(Context context, String title, String message, String priority, int taskId) {

        // Obtener el servicio de notificaciones
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        int importance;
        int priorityCompat;

        switch (priority) {
            case "Alta":
                importance = NotificationManager.IMPORTANCE_HIGH;
                priorityCompat = NotificationCompat.PRIORITY_HIGH;
                break;
            case "Media":
                importance = NotificationManager.IMPORTANCE_DEFAULT;
                priorityCompat = NotificationCompat.PRIORITY_DEFAULT;
                break;
            case "Baja":
                importance = NotificationManager.IMPORTANCE_LOW;
                priorityCompat = NotificationCompat.PRIORITY_LOW;
                break;
            default:
                importance = NotificationManager.IMPORTANCE_DEFAULT;
                priorityCompat = NotificationCompat.PRIORITY_DEFAULT;
                break;
        }

        // Crear canal
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Recordatorios de tareas",
                    importance
            );
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction("CANCEL_ALARM");
        intent.putExtra("taskId", taskId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                taskId,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        // Crear notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(priorityCompat)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if ("Alta".equals(priority)) {
            builder.setColor(ContextCompat.getColor(context, R.color.azul_marino));
        }

        // Mostrar la notificación
        manager.notify(taskId, builder.build());
    }

    // Metodo de cancelacion de notificaciones
    public static void cancelNotification(Context context, int taskId) {
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(taskId);
    }
}

package com.srd14.agend_in;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

public class CalendarUtils {

    /**
     * Abre la aplicación de calendario del sistema para que el usuario confirme y guarde un nuevo evento.
     * @param context El contexto de la aplicación.
     * @param title El título del evento.
     * @param description La descripción del evento.
     * @param startTimeMillis La hora de inicio del evento en milisegundos.
     */
    public static void addEventToCalendar(Context context, String title, String description, long startTimeMillis) {
        if (context == null) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeMillis)
                // Por defecto, el evento durará 1 hora. Se puede ajustar si es necesario (mas que nada por que dependiendo la configuracion del evento google pide una hora de finalizacion.
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTimeMillis + 60 * 60 * 1000)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, description)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        // Se necesita este flag si el contexto no es una Activity (por ejemplo, desde un servicio o un fragmento sin acceso directo a la actividad).
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
}

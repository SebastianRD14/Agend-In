package com.srd14.agend_in;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class add_task_fragment extends Fragment {

    private EditText editTextName, editTextDate, editTextTime, editTextDescription;
    private Spinner spinnerPriority;
    private Calendar selectedDate = Calendar.getInstance();

    public add_task_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_task_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextName = view.findViewById(R.id.editTextText);
        editTextDate = view.findViewById(R.id.editTextDate3);
        editTextTime = view.findViewById(R.id.editTextTime2);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        spinnerPriority = view.findViewById(R.id.spinner);

        // --- Mejoramos la selección de fecha ---
        editTextDate.setOnClickListener(v -> showDatePickerDialog());
        editTextDate.setFocusable(false); // Para que no se pueda escribir manualmente

        // --- Mejoramos la selección de hora ---
        editTextTime.setOnClickListener(v -> showTimePickerDialog());
        editTextTime.setFocusable(false);

        Button buttonCancel = view.findViewById(R.id.button5);
        Button buttonAdd = view.findViewById(R.id.button6);

        buttonCancel.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        buttonAdd.setOnClickListener(v -> {
            saveTask();
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (dp, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    // Formateamos la fecha para mostrarla en el EditText
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    editTextDate.setText(dateFormat.format(selectedDate.getTime()));
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        new android.app.TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute1) -> {
                    String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                    editTextTime.setText(timeFormatted);
                },
                hour,
                minute,
                true // true = formato de 24 horas (por ejemplo 22:11)
        ).show();
    }

    private void saveTask() {
        String name = editTextName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();

        // Validamos que haya por lo menos nombre y fecha
        if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            return; // si falta algo, no hace nada
        }

        // Creamos el objeto Task con esos datos
        Task task = new Task(name, date, time, description, priority);

        // Programamos la alarma
        AlarmScheduler.scheduleTaskAlarm(getContext(), task);

        // Guardamos la tarea en segundo plano para no congelar la app
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase.getDatabase(getContext()).taskDao().insert(task);

            // Convertimos la fecha y hora a milisegundos para la alarma
            try {
                String[] dateParts = date.split("-");
                String[] timeParts = time.split(":");

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(dateParts[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(dateParts[1]) - 1);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[2]));
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
                calendar.set(Calendar.SECOND, 0);

                long triggerTime = calendar.getTimeInMillis();

                // Llamamos al ReminderScheduler para programar la notificación
                AlarmScheduler.scheduleTaskAlarm(getContext(), task);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Volvemos a la pantalla anterior después de guardar
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    getParentFragmentManager().popBackStack();
                });
            }
        });
    }
}

package com.srd14.agend_in;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class edit_task_fragment extends Fragment {

    private EditText editTextName, editTextDate, editTextTime, editTextDescription;
    private Spinner spinnerPriority;
    private int taskId = -1;
    private Task taskToSave; // Tarea que se guardará o actualizará

    public edit_task_fragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getInt("TASK_ID", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_task_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextName = view.findViewById(R.id.editTextText);
        editTextDate = view.findViewById(R.id.editTextDate3);
        editTextTime = view.findViewById(R.id.editTextTime2);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        spinnerPriority = view.findViewById(R.id.spinner);

        Button buttonCancel = view.findViewById(R.id.button5);
        Button buttonAdd = view.findViewById(R.id.button6);

        // Configurar el spinner de prioridad
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        if (taskId != -1) {
            loadTaskData();
        }
        // Si es una nueva tarea, taskToSave permanecerá nulo hasta que se guarde.

        buttonCancel.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        buttonAdd.setOnClickListener(v -> saveTask());
    }

    private void loadTaskData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            taskToSave = AppDatabase.getDatabase(getContext()).taskDao().getTaskById(taskId);
            if (taskToSave != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    editTextName.setText(taskToSave.getName());
                    editTextDate.setText(taskToSave.getDate());
                    editTextTime.setText(taskToSave.getTime());
                    editTextDescription.setText(taskToSave.getDescription());

                    if (taskToSave.getPriority() != null) {
                        int spinnerPosition = ((ArrayAdapter<String>) spinnerPriority.getAdapter()).getPosition(taskToSave.getPriority());
                        spinnerPriority.setSelection(spinnerPosition);
                    }
                });
            }
        });
    }

    private void saveTask() {
        String name = editTextName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(getContext(), "Nombre, fecha y hora son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (taskToSave == null) { // Es una tarea nueva
            taskToSave = new Task(name, date, time, description, priority);
        } else { // Es una tarea existente
            taskToSave.setName(name);
            taskToSave.setDate(date);
            taskToSave.setTime(time);
            taskToSave.setDescription(description);
            taskToSave.setPriority(priority);
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if (taskId == -1) {
                long newId = AppDatabase.getDatabase(getContext()).taskDao().insert(taskToSave);
                taskToSave.setId((int) newId);
            } else {
                AppDatabase.getDatabase(getContext()).taskDao().update(taskToSave);
            }

            // Una vez guardada, verificar permisos y programar alarma
            if (getActivity() != null) {
                getActivity().runOnUiThread(this::checkPermissionsAndScheduleAlarm);
            }
        });
    }

    private void checkPermissionsAndScheduleAlarm() {
        if (!PermissionManager.hasPostNotificationsPermission(getContext())) {
            PermissionManager.requestPostNotificationsPermission(getActivity());
        } else if (!PermissionManager.canScheduleExactAlarms(getContext())) {
            PermissionManager.requestScheduleExactAlarmPermission(getContext());
        } else {
            scheduleAlarmAndFinish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionManager.REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de notificaciones concedido, ahora verificar el de alarmas
                checkPermissionsAndScheduleAlarm();
            } else {
                Toast.makeText(getContext(), "Permiso de notificaciones denegado.", Toast.LENGTH_SHORT).show();
                scheduleAlarmAndFinish(); // Continuar sin notificación
            }
        }
    }

    private void scheduleAlarmAndFinish() {
        if (PermissionManager.hasPostNotificationsPermission(getContext()) && PermissionManager.canScheduleExactAlarms(getContext())) {
            AlarmScheduler.scheduleTaskAlarm(getContext(), taskToSave);
            Toast.makeText(getContext(), "Alarma programada.", Toast.LENGTH_SHORT).show();
        }
        getParentFragmentManager().popBackStack(); // Regresar
    }
}

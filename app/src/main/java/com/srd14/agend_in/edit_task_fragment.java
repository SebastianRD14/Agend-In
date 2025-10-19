package com.srd14.agend_in;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class edit_task_fragment extends Fragment {

    private EditText editTextName, editTextDate, editTextTime, editTextDescription;
    private int taskId = -1;
    private Task currentTask; // Variable para mantener la tarea actual

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

        Button buttonCancel = view.findViewById(R.id.button5);
        Button buttonAdd = view.findViewById(R.id.button6);

        if (taskId != -1) {
            loadTaskData();
        }

        buttonCancel.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        // El botón "Agregar" guarda los cambios
        buttonAdd.setOnClickListener(v -> {
            saveChanges();
        });
    }

    private void loadTaskData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            currentTask = AppDatabase.getDatabase(getContext()).taskDao().getTaskById(taskId);
            if (currentTask != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    editTextName.setText(currentTask.getName());
                    editTextDate.setText(currentTask.getDate());
                    editTextTime.setText(currentTask.getTime());
                    editTextDescription.setText(currentTask.getDescription());
                });
            }
        });
    }

    private void saveChanges() {
        String newName = editTextName.getText().toString().trim();
        String newDate = editTextDate.getText().toString().trim();
        String newTime = editTextTime.getText().toString().trim();
        String newDescription = editTextDescription.getText().toString().trim();

        // Validar que la tarea exista y que los campos no estén vacíos
        if (currentTask == null || newName.isEmpty() || newDate.isEmpty()) {
            // Mostrar error al usuario
            return;
        }

        // Actualizar el objeto de la tarea con los nuevos datos
        currentTask.setName(newName);
        currentTask.setDate(newDate);
        currentTask.setTime(newTime);
        currentTask.setDescription(newDescription);

        // Guardar el evento actualizado en la base de datos en un hilo secundario
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase.getDatabase(getContext()).taskDao().update(currentTask);

            // Regresar a la pantalla anterior en el hilo principal
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    getParentFragmentManager().popBackStack();
                });
            }
        });
    }
}

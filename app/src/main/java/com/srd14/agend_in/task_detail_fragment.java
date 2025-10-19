package com.srd14.agend_in;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class task_detail_fragment extends Fragment {

    private int taskId = -1;
    private Task currentTask; // Variable para guardar la tarea actual
    private TextView textName, textDate, textTime, textDescription;

    public task_detail_fragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.task_detail_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textName = view.findViewById(R.id.editTextText);
        textDate = view.findViewById(R.id.editTextDate3);
        textTime = view.findViewById(R.id.editTextTime2);
        textDescription = view.findViewById(R.id.textView);

        if (taskId != -1) {
            loadTaskDetails();
        }

        Button buttonEdit = view.findViewById(R.id.button5);
        Button buttonDelete = view.findViewById(R.id.button6);

        buttonEdit.setOnClickListener(v -> {
            edit_task_fragment editFragment = new edit_task_fragment();
            Bundle args = new Bundle();
            args.putInt("TASK_ID", taskId);
            editFragment.setArguments(args);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, editFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // 1. Añadir la lógica al botón de eliminar
        buttonDelete.setOnClickListener(v -> {
            deleteCurrentTask();
        });
    }

    private void loadTaskDetails() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Guardamos la tarea actual en la variable de la clase
            currentTask = AppDatabase.getDatabase(getContext()).taskDao().getTaskById(taskId);

            if (currentTask != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    textName.setText(currentTask.getName());
                    textDate.setText(currentTask.getDate());
                    textTime.setText(currentTask.getTime());
                    textDescription.setText(currentTask.getDescription());
                });
            }
        });
    }

    // 2. Nuevo método para eliminar la tarea
    private void deleteCurrentTask() {
        if (currentTask == null) {
            // No se puede eliminar si no se ha cargado la tarea
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Eliminar la tarea de la BD en un hilo secundario
            AppDatabase.getDatabase(getContext()).taskDao().delete(currentTask);

            // Regresar a la pantalla anterior en el hilo principal
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    getParentFragmentManager().popBackStack();
                });
            }
        });
    }
}

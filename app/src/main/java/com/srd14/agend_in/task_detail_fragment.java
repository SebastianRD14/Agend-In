package com.srd14.agend_in;

import android.content.Intent;
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
    private TextView textName, textDate, textTime, textPriority, textDescription;

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

        // Corregimos los IDs para que coincidan con el XML
        textName = view.findViewById(R.id.taskDetailName);
        textDate = view.findViewById(R.id.taskDetailDate);
        textTime = view.findViewById(R.id.taskDetailTime);
        textPriority = view.findViewById(R.id.taskDetailPriority);
        textDescription = view.findViewById(R.id.taskDetailDescription);

        if (taskId != -1) {
            loadTaskDetails();
        }

        Button buttonEdit = view.findViewById(R.id.buttonEdit);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);
        Button buttonShare = view.findViewById(R.id.buttonShare); // Obtenemos el nuevo botón

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

        buttonDelete.setOnClickListener(v -> {
            deleteCurrentTask();
        });

        // --- Lógica para el botón de compartir ---
        buttonShare.setOnClickListener(v -> {
            if (currentTask != null) {
                // 1. Crear el texto que se va a compartir
                String shareText = "¡Mira esta tarea!\n\n" +
                        "Nombre: " + currentTask.getName() + "\n" +
                        "Fecha: " + currentTask.getDate() + "\n" +
                        "Hora: " + currentTask.getTime() + "\n\n" +
                        "Descripción: " + currentTask.getDescription();

                // 2. Crear un Intent para compartir
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tarea: " + currentTask.getName());
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

                // 3. Mostrar el menú de compartir de Android
                startActivity(Intent.createChooser(shareIntent, "Compartir tarea vía"));
            }
        });
    }

    private void loadTaskDetails() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            currentTask = AppDatabase.getDatabase(getContext()).taskDao().getTaskById(taskId);

            if (currentTask != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    textName.setText(currentTask.getName());
                    textDate.setText(currentTask.getDate());
                    textTime.setText(currentTask.getTime());
                    textPriority.setText(currentTask.getPriority());
                    textDescription.setText(currentTask.getDescription());
                });
            }
        });
    }

    private void deleteCurrentTask() {
        if (currentTask == null) {
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase.getDatabase(getContext()).taskDao().delete(currentTask);

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    getParentFragmentManager().popBackStack();
                });
            }
        });
    }
}

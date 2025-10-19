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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class add_task_fragment extends Fragment {

    private EditText editTextName, editTextDate, editTextTime, editTextDescription;
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

        // --- Mejoramos la selección de fecha ---
        editTextDate.setOnClickListener(v -> showDatePickerDialog());
        editTextDate.setFocusable(false); // Para que no se pueda escribir manualmente

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

    private void saveTask() {
        // Ahora nos aseguramos de que la fecha se guarda en el formato correcto
        String name = editTextName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim(); // formato YYYY-MM-DD
        String time = editTextTime.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty()) {
            return;
        }

        Task task = new Task(name, date, time, description);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase.getDatabase(getContext()).taskDao().insert(task);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    getParentFragmentManager().popBackStack();
                });
            }
        });
    }
}

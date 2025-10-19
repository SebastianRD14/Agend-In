package com.srd14.agend_in;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class calendar_fragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private CalendarTaskAdapter adapter;

    public calendar_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView5);
        recyclerView = view.findViewById(R.id.recyclerProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar el adaptador inicial con una lista vacía
        adapter = new CalendarTaskAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Listener para cuando el usuario selecciona una fecha
        calendarView.setOnDateChangeListener((cv, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
            loadTasksForDate(selectedDate);
        });

        // Cargar las tareas para el día de hoy al abrir el fragmento
        loadTasksForDate(getTodayDateString());
    }

    private void loadTasksForDate(String date) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Task> tasks = AppDatabase.getDatabase(getContext()).taskDao().getTasksByDate(date);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.updateTasks(tasks);
                });
            }
        });
    }

    private String getTodayDateString() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}

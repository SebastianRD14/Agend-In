package com.srd14.agend_in;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class calendar_fragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private CalendarTaskAdapter adapter;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    public calendar_fragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                    boolean allGranted = true;
                    for (Boolean isGranted : permissions.values()) {
                        if (!isGranted) {
                            allGranted = false;
                            break;
                        }
                    }

                    if (allGranted) {
                        loadDataForSelectedDate();
                    } else {
                        Log.d("CalendarFragment", "Permissions denied by user.");
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CalendarTaskAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        calendarView.setOnDateChangeListener((cv, year, month, dayOfMonth) -> {
            loadDataForSelectedDate(year, month, dayOfMonth);
        });

        if (arePermissionsGranted()) {
            loadDataForSelectedDate();
        } else {
            requestCalendarPermissions();
        }
    }

    private void loadDataForSelectedDate() {
        Calendar today = Calendar.getInstance();
        loadDataForSelectedDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    }

    private void loadDataForSelectedDate(int year, int month, int dayOfMonth) {
        String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Task> appTasks = AppDatabase.getDatabase(getContext()).taskDao().getTasksByDate(selectedDate);
            
            List<CalendarEvent> calendarEvents = obtenerEventosDelCalendario(year, month, dayOfMonth);

            List<Object> combinedList = new ArrayList<>();
            combinedList.addAll(appTasks);
            combinedList.addAll(calendarEvents);
            
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.updateItems(combinedList);
                });
            }
        });
    }

    private boolean arePermissionsGranted() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCalendarPermissions() {
        requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR});
    }

    public List<CalendarEvent> obtenerEventosDelCalendario(int year, int month, int dayOfMonth) {
        List<CalendarEvent> events = new ArrayList<>();
        if (!arePermissionsGranted()) {
            Log.d("CalendarFragment", "Cannot get events, permissions not granted.");
            return events;
        }

        ContentResolver contentResolver = requireContext().getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        String[] projection = new String[]{
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART
        };

        Calendar startTime = Calendar.getInstance();
        startTime.set(year, month, dayOfMonth, 0, 0, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, dayOfMonth, 23, 59, 59);

        String selection = CalendarContract.Events.DTSTART + " >= ? AND " + CalendarContract.Events.DTSTART + " <= ?";
        String[] selectionArgs = new String[]{
                String.valueOf(startTime.getTimeInMillis()),
                String.valueOf(endTime.getTimeInMillis())
        };

        try (Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE));
                    long startMillis = cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART));
                    events.add(new CalendarEvent(title, startMillis));
                }
            }
        } catch (Exception e) {
            Log.e("CalendarFragment", "Error reading calendar events", e);
        }
        return events;
    }

    public void agregarEventoAlCalendario(String title, String location, long startTimeMillis, long endTimeMillis) {
        if (getContext() == null) return;
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTimeMillis)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, "Este es un evento de Agend-In")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivity(intent);
    }
}

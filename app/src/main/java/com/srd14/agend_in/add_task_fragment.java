package com.srd14.agend_in;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class add_task_fragment extends Fragment {

    private EditText editTextName, editTextDate, editTextTime, editTextDescription;
    private Spinner spinnerPriority;
    private ImageButton imageButton, imageButton2;
    private Calendar selectedDate = Calendar.getInstance();

    private Uri imageUri, audioUri;
    private MediaRecorder mediaRecorder;
    private String audioFilePath;
    private boolean isRecording = false;

    private final ActivityResultLauncher<String> requestImagePermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    selectImageFromGallery();
                } else {
                    Toast.makeText(getContext(), "Permission to access images denied", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> requestRecordAudioPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    toggleAudioRecording();
                } else {
                    Toast.makeText(getContext(), "Permission to record audio denied", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    requireContext().getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Toast.makeText(getContext(), "Image selected", Toast.LENGTH_SHORT).show();
                }
            });

    public add_task_fragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        imageButton = view.findViewById(R.id.imageButton);
        imageButton2 = view.findViewById(R.id.imageButton2);

        editTextDate.setOnClickListener(v -> showDatePickerDialog());
        editTextDate.setFocusable(false);

        editTextTime.setOnClickListener(v -> showTimePickerDialog());
        editTextTime.setFocusable(false);

        imageButton.setOnClickListener(v -> checkAndRequestImagePermission());
        imageButton2.setOnClickListener(v -> checkAndRequestRecordAudioPermission());

        Button buttonCancel = view.findViewById(R.id.button5);
        Button buttonAdd = view.findViewById(R.id.button6);

        buttonCancel.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        buttonAdd.setOnClickListener(v -> saveTask());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRecording) {
            stopRecording();
        }
    }

    private void checkAndRequestImagePermission() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            selectImageFromGallery();
        } else {
            requestImagePermissionLauncher.launch(permission);
        }
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void checkAndRequestRecordAudioPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            toggleAudioRecording();
        } else {
            requestRecordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void toggleAudioRecording() {
        if (isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
    }

    private void startRecording() {
        try {
            File audioFile = File.createTempFile("audio", ".3gp", getContext().getCacheDir());
            audioFilePath = audioFile.getAbsolutePath();

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.prepare();
            mediaRecorder.start();

            isRecording = true;
            imageButton2.setImageResource(android.R.drawable.ic_media_pause); // Icono de detener
            Toast.makeText(getContext(), "Recording started...", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("AudioRecord", "prepare() failed", e);
            Toast.makeText(getContext(), "Failed to start recording", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
            } catch (RuntimeException stopException) {
                Log.e("AudioRecord", "Stop failed", stopException);
            }
            mediaRecorder = null;
            isRecording = false;
            audioUri = Uri.fromFile(new File(audioFilePath));
            imageButton2.setImageResource(android.R.drawable.ic_btn_speak_now); // Icono de micrófono
            Toast.makeText(getContext(), "Recording stopped. Audio saved.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (dp, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
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
        new android.app.TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {
                    String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    editTextTime.setText(timeFormatted);
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void saveTask() {
        String name = editTextName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            return;
        }

        String imageUriString = imageUri != null ? imageUri.toString() : null;
        String audioUriString = audioUri != null ? audioUri.toString() : null;

        Task task = new Task(name, date, time, description, priority, imageUriString, audioUriString);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            long newTaskId = AppDatabase.getDatabase(getContext()).taskDao().insert(task);
            task.setId((int) newTaskId);

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

                long startTimeMillis = calendar.getTimeInMillis();
                AlarmScheduler.scheduleTaskAlarm(getContext(), task);
                CalendarUtils.addEventToCalendar(getContext(), name, description, startTimeMillis);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> getParentFragmentManager().popBackStack());
            }
        });
    }
}

package com.srd14.agend_in;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditTaskFragment extends Fragment {

    private EditText editTextName, editTextDate, editTextTime, editTextDescription;
    private Spinner spinnerPriority;
    private ImageButton imageButton, recordButton, playButton, stopButton;
    private ImageView imageViewPreview;
    private LinearLayout audioControls;

    private int taskId = -1;
    private Task currentTask;
    private Uri imageUri, audioUri;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
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
                    imageViewPreview.setImageURI(imageUri);
                    imageViewPreview.setVisibility(View.VISIBLE);
                }
            });

    public EditTaskFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getInt("TASK_ID", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_task_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // --- Find Views ---
        editTextName = view.findViewById(R.id.editTextText);
        editTextDate = view.findViewById(R.id.editTextDate3);
        editTextTime = view.findViewById(R.id.editTextTime2);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        spinnerPriority = view.findViewById(R.id.spinner);
        imageButton = view.findViewById(R.id.imageButton);
        recordButton = view.findViewById(R.id.imageButton2);
        imageViewPreview = view.findViewById(R.id.imageViewPreview);
        audioControls = view.findViewById(R.id.audioControls);
        playButton = view.findViewById(R.id.buttonPlay);
        stopButton = view.findViewById(R.id.buttonStop);
        Button buttonCancel = view.findViewById(R.id.button5);
        Button buttonSave = view.findViewById(R.id.button6);

        // --- Setup Listeners ---
        imageButton.setOnClickListener(v -> checkAndRequestImagePermission());
        recordButton.setOnClickListener(v -> checkAndRequestRecordAudioPermission());
        playButton.setOnClickListener(v -> playAudio());
        stopButton.setOnClickListener(v -> stopAudio());
        buttonCancel.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        buttonSave.setOnClickListener(v -> saveTask());

        if (taskId != -1) {
            loadTaskDetails();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRecording) {
            stopRecording();
        }
        if (mediaPlayer != null) {
            stopAudio();
        }
    }

    private void loadTaskDetails() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            currentTask = AppDatabase.getDatabase(getContext()).taskDao().getTaskById(taskId);
            if (currentTask != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    editTextName.setText(currentTask.getName());
                    editTextDate.setText(currentTask.getDate());
                    editTextTime.setText(currentTask.getTime());
                    editTextDescription.setText(currentTask.getDescription());

                    ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerPriority.getAdapter();
                    if (adapter != null) {
                        for (int i = 0; i < adapter.getCount(); i++) {
                            if (adapter.getItem(i).toString().equals(currentTask.getPriority())) {
                                spinnerPriority.setSelection(i);
                                break;
                            }
                        }
                    }

                    if (currentTask.getImageUri() != null) {
                        imageUri = Uri.parse(currentTask.getImageUri());
                        imageViewPreview.setImageURI(imageUri);
                        imageViewPreview.setVisibility(View.VISIBLE);
                    }
                    if (currentTask.getAudioUri() != null) {
                        audioUri = Uri.parse(currentTask.getAudioUri());
                        audioControls.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void checkAndRequestImagePermission() {
        String permission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

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
            recordButton.setImageResource(android.R.drawable.ic_media_pause);
            Toast.makeText(getContext(), "Recording started...", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("AudioRecord", "prepare() failed", e);
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
            } catch (RuntimeException ex) {
                Log.e("AudioRecord", "stop() failed", ex);
            }
            mediaRecorder = null;
            isRecording = false;
            audioUri = Uri.fromFile(new File(audioFilePath));
            recordButton.setImageResource(android.R.drawable.ic_btn_speak_now);
            audioControls.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Recording stopped.", Toast.LENGTH_SHORT).show();
        }
    }

    private void playAudio() {
        if (audioUri != null && mediaPlayer == null) {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(getContext(), audioUri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(mp -> stopAudio());
                Toast.makeText(getContext(), "Playing audio...", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("AudioPlay", "Could not play audio", e);
                Toast.makeText(getContext(), "Could not play audio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(getContext(), "Audio stopped.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTask() {
        String name = editTextName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(getContext(), "Name, date and time are required", Toast.LENGTH_SHORT).show();
            return;
        }

        currentTask.setName(name);
        currentTask.setDate(date);
        currentTask.setTime(time);
        currentTask.setDescription(description);
        currentTask.setPriority(priority);
        if (imageUri != null) {
            currentTask.setImageUri(imageUri.toString());
        }
        if (audioUri != null) {
            currentTask.setAudioUri(audioUri.toString());
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase.getDatabase(getContext()).taskDao().update(currentTask);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> getParentFragmentManager().popBackStack());
            }
        });
    }
}

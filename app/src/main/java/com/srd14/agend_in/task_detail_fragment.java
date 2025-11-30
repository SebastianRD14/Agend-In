package com.srd14.agend_in;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.srd14.agend_in.EditTaskFragment;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class task_detail_fragment extends Fragment {

    private int taskId = -1;
    private Task currentTask;
    private TextView textName, textDate, textTime, textPriority, textDescription;
    private ImageView imageView;
    private ImageButton audioPlayButton;
    private MediaPlayer mediaPlayer;

    public task_detail_fragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getInt("TASK_ID", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_detail_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textName = view.findViewById(R.id.taskDetailName);
        textDate = view.findViewById(R.id.taskDetailDate);
        textTime = view.findViewById(R.id.taskDetailTime);
        textPriority = view.findViewById(R.id.taskDetailPriority);
        textDescription = view.findViewById(R.id.taskDetailDescription);
        imageView = view.findViewById(R.id.imageView);
        audioPlayButton = view.findViewById(R.id.audioPlayButton);

        if (taskId != -1) {
            loadTaskDetails();
        }

        Button buttonEdit = view.findViewById(R.id.buttonEdit);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);
        Button buttonShare = view.findViewById(R.id.buttonShare);

        buttonEdit.setOnClickListener(v -> {
            EditTaskFragment editFragment = new EditTaskFragment();
            Bundle args = new Bundle();
            args.putInt("TASK_ID", taskId);
            editFragment.setArguments(args);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, editFragment)
                    .addToBackStack(null)
                    .commit();
        });

        buttonDelete.setOnClickListener(v -> deleteCurrentTask());

        buttonShare.setOnClickListener(v -> {
            if (currentTask != null) {
                String shareText = "¡Mira esta tarea!\n\n" +
                        "Nombre: " + currentTask.getName() + "\n" +
                        "Fecha: " + currentTask.getDate() + "\n" +
                        "Hora: " + currentTask.getTime() + "\n\n" +
                        "Descripción: " + currentTask.getDescription();

                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                if (currentTask.getImageUri() != null && !currentTask.getImageUri().isEmpty()) {
                    Uri imageUri = Uri.parse(currentTask.getImageUri());
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    shareIntent.setType("text/plain");
                }

                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tarea: " + currentTask.getName());
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

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

                    // --- Lógica para mostrar imagen y audio ---
                    String imageUriString = currentTask.getImageUri();
                    if (imageUriString != null && !imageUriString.isEmpty()) {
                        Uri imageUri = Uri.parse(imageUriString);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageURI(imageUri);
                    }

                    String audioUriString = currentTask.getAudioUri();
                    if (audioUriString != null && !audioUriString.isEmpty()) {
                        Uri audioUri = Uri.parse(audioUriString);
                        audioPlayButton.setVisibility(View.VISIBLE);
                        audioPlayButton.setOnClickListener(v -> playAudio(audioUri));
                    }
                });
            }
        });
    }

    private void playAudio(Uri audioUri) {
        stopAudio(); // Detenemos cualquier reproducción anterior
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getContext(), audioUri);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                Toast.makeText(getContext(), "Reproduciendo audio...", Toast.LENGTH_SHORT).show();
            });
            mediaPlayer.setOnCompletionListener(mp -> stopAudio());
        } catch (IOException e) {
            Toast.makeText(getContext(), "No se pudo reproducir el audio", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopAudio(); // Liberamos recursos si el fragmento se detiene
    }

    private void deleteCurrentTask() {
        if (currentTask == null) return;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase.getDatabase(getContext()).taskDao().delete(currentTask);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> getParentFragmentManager().popBackStack());
            }
        });
    }
}

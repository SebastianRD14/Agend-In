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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class tasklist_fragment extends Fragment implements TasksAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private TasksAdapter adapter;
    private List<Task> taskList;

    public tasklist_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tasklist_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabAddTask = view.findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new add_task_fragment())
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView = view.findViewById(R.id.recyclerProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTasks();
    }

    private void loadTasks() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            taskList = AppDatabase.getDatabase(getContext()).taskDao().getAllTasks();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter = new TasksAdapter(taskList, this);
                    recyclerView.setAdapter(adapter);
                });
            }
        });
    }

    @Override
    public void onEditClick(int position) {
        Task selectedTask = taskList.get(position);
        int taskId = selectedTask.getId();
        edit_task_fragment editFragment = new edit_task_fragment();
        Bundle args = new Bundle();
        args.putInt("TASK_ID", taskId);
        editFragment.setArguments(args);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDetailsClick(int position) {
        Task selectedTask = taskList.get(position);
        int taskId = selectedTask.getId();
        task_detail_fragment detailFragment = new task_detail_fragment();
        Bundle args = new Bundle();
        args.putInt("TASK_ID", taskId);
        detailFragment.setArguments(args);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDeleteClick(int position) {
        // 1. Obtener la tarea que se va a eliminar
        Task taskToDelete = taskList.get(position);

        // 2. Eliminarla de la base de datos en un hilo secundario
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase.getDatabase(getContext()).taskDao().delete(taskToDelete);
        });

        // 3. Eliminarla de la lista en la UI y notificar al adaptador
        taskList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, taskList.size());
    }
}

package com.srd14.agend_in;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    private List<Task> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDetailsClick(int position);
        void onDeleteClick(int position);
    }

    public TasksAdapter(List<Task> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = items.get(position);
        holder.activityName.setText(currentTask.getName());
        holder.date.setText(currentTask.getDate());
        holder.hour.setText(currentTask.getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView activityName, date, hour;
        Button buttonEditTask, buttonViewDetails;
        ImageButton buttonDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.ActivityName);
            date = itemView.findViewById(R.id.Date);
            hour = itemView.findViewById(R.id.Hour);
            buttonEditTask = itemView.findViewById(R.id.botonEditTask);
            buttonViewDetails = itemView.findViewById(R.id.botonViewDetails);
            buttonDelete = itemView.findViewById(R.id.eliminar);

            buttonEditTask.setOnClickListener(v -> {
                // Se reemplaza getAdapterPosition() por getBindingAdapterPosition()
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onEditClick(position);
                }
            });

            buttonViewDetails.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDetailsClick(position);
                }
            });

            buttonDelete.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(position);
                }
            });
        }
    }
}

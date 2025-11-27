package com.srd14.agend_in;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;
    private static final int VIEW_TYPE_APP_TASK = 1;
    private static final int VIEW_TYPE_CALENDAR_EVENT = 2;

    public CalendarTaskAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Task) {
            return VIEW_TYPE_APP_TASK;
        } else if (items.get(position) instanceof CalendarEvent) {
            return VIEW_TYPE_CALENDAR_EVENT;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_APP_TASK) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_task_item, parent, false);
            return new TaskViewHolder(view);
        } else { // VIEW_TYPE_CALENDAR_EVENT
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_task_item, parent, false);
            return new CalendarEventViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_APP_TASK) {
            Task task = (Task) items.get(position);
            ((TaskViewHolder) holder).bind(task);
        } else {
            CalendarEvent event = (CalendarEvent) items.get(position);
            ((CalendarEventViewHolder) holder).bind(event);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateItems(List<Object> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView taskHour;
        TextView taskDay;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskNameTextView);
            taskDay = itemView.findViewById(R.id.taskDayTextView);
            taskHour = itemView.findViewById(R.id.taskHourTextView);
        }

        public void bind(Task task) {
            taskName.setText(task.getName());
            taskDay.setText(task.getDate());
            taskHour.setText(task.getTime());
        }
    }

    public static class CalendarEventViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle;
        TextView eventTime;
        TextView eventSource;

        public CalendarEventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.taskNameTextView);
            eventTime = itemView.findViewById(R.id.taskHourTextView);
            eventSource = itemView.findViewById(R.id.taskDayTextView);
        }

        public void bind(CalendarEvent event) {
            eventTitle.setText(event.getTitle());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            eventTime.setText(timeFormat.format(new Date(event.getStartTimeMillis())));
            eventSource.setText("Evento del Calendario");
        }
    }
}

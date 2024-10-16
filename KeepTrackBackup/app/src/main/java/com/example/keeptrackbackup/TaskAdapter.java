package com.example.keeptrackbackup;

import com.example.keeptrackbackup.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Tarea> taskList;

    public TaskAdapter(List<Tarea> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tareas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tarea task = taskList.get(position);
        holder.taskNameTextView.setText(task.getNombre());
        // ... (set other views based on task data) ...
    }

    @Override
    public int getItemCount() { // Added getItemCount() method
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskNameTextView;
        // ... (other views for task item) ...

        public ViewHolder(View view) {
            super(view);
            taskNameTextView = view.findViewById(R.id.taskNameTextView); // Assuming you have a TextView with this ID
            // ... (find other views) ...
        }
    }

}

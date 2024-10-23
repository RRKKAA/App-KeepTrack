package com.example.keeptrackbackup.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keeptrackbackup.R;

import java.util.List;

public class SavedTaskAdapter extends RecyclerView.Adapter<SavedTaskAdapter.ViewHolder> {

    private List<SavedTarea> savedTaskList;
    private OnItemLongClickListener onItemLongClickListener;

    public SavedTaskAdapter(List<SavedTarea> savedTaskList) {
        this.savedTaskList = savedTaskList;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(SavedTarea savedTask);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_tareas, parent, false); // Create a layout file for saved task items (saved_task_item.xml)
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavedTarea savedTask = savedTaskList.get(position);

        holder.taskNameTextView.setText(savedTask.getNombre());
        holder.deadlineTimeTextView.setText(savedTask.getHoraLimite().toString());

        if (savedTask.getFechaLimite() != null) {
            holder.deadlineDateTextView.setText(savedTask.getFechaLimite().toString());
            holder.completionDateTextView.setText(savedTask.getFechaComplecion().toString());
        } else {
            holder.deadlineDateTextView.setText(savedTask.getDia()); // Display day for daily tasks
            holder.completionDateTextView.setVisibility(View.GONE); // Hide completion date for daily tasks
        }

        holder.completionHourTextView.setText(savedTask.getHoraComplecion().toString());
        holder.completionStatusTextView.setText(savedTask.isCompletado() ? "Completado" : "Incompleto");

        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(savedTaskList.get(position));
            }
            return true;
        });
    }

    public void removeTask(SavedTarea savedTask) {
        int position = savedTaskList.indexOf(savedTask);
        if (position != -1) {
            savedTaskList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return savedTaskList.size();
    }

    public void updateTasks(List<SavedTarea> newTasks) {
        savedTaskList.clear();
        savedTaskList.addAll(newTasks);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskNameTextView;
        public TextView deadlineTimeTextView;
        public TextView deadlineDateTextView;
        public TextView completionHourTextView;
        public TextView completionDateTextView;
        public TextView completionStatusTextView;

        public ViewHolder(View view) {
            super(view);
            taskNameTextView = view.findViewById(R.id.taskNameTextView); // Update IDs to match your layout
            deadlineTimeTextView = view.findViewById(R.id.deadlineTimeTextView);
            deadlineDateTextView = view.findViewById(R.id.deadlineDateTextView);
            completionHourTextView = view.findViewById(R.id.completionHourTextView);
            completionDateTextView = view.findViewById(R.id.completionDateTextView);
            completionStatusTextView = view.findViewById(R.id.completionStatusTextView);
        }
    }
}
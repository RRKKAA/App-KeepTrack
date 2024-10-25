package com.example.keeptrackbackup.data;

import com.example.keeptrackbackup.R;
import com.example.keeptrackbackup.ui.ModificarTarea;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Tarea> taskList;
    private View fragmentView;
    private TaskAdapter taskAdapter;
    private FragmentManager fragmentManager;

    public TaskAdapter(List<Tarea> taskList, View fragmentView, FragmentManager fragmentManager) {
        this.taskList = taskList;
        this.fragmentView = fragmentView;
        this.fragmentManager = fragmentManager;
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
        holder.taskTimeTextView.setText(task.getHora().toString());

        holder.taskCompletedSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            handleTaskCompletion(task, isChecked);
        });

        holder.itemView.setOnLongClickListener(v -> {
            showTaskOptionsDialog(task, holder.itemView);
            return true; // Consume the long click event
        });

        // ... (set other views based on task data) ...
        if (task.getDiario()) {
            holder.taskDateOrDaysTextView.setText(task.getDias().toString()); // Assuming you have a getDias() method in Tarea
        } else {
            holder.taskDateOrDaysTextView.setText(task.getFecha().toString()); // Assuming you have a getFecha() method in Tarea
        }
        holder.taskCompletedSwitch.setChecked(task.isCompletada());
    }

    private void preventDuplicateDeadlineTask(SavedTarea savedTask) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference historyRef = database.getReference("history");

        historyRef.orderByChild("Nombre").equalTo(savedTask.getNombre())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean exists = false;
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            SavedTarea existingTask = childSnapshot.getValue(SavedTarea.class);
                            if (existingTask != null && existingTask.getFechaLimite().equals(savedTask.getFechaLimite())) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            addToHistory(savedTask);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
    }

    private void preventDuplicateDailyTask(SavedTarea savedTask, Tarea task) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dailyTaskFlagRef = database.getReference("history").child("dailyTaskFlags").child(task.getKey());

        dailyTaskFlagRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists() || !snapshot.getValue(Boolean.class)) {
                    addToHistory(savedTask);
                    dailyTaskFlagRef.setValue(true); // Set the flag for the current day
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private SavedTarea createSavedTarea(Tarea task, boolean completed) {
        SavedTarea savedTask = new SavedTarea();
        savedTask.setNombre(task.getNombre());
        savedTask.setHoraLimite(task.getHora());
        savedTask.setFechaLimite(task.getFecha());
        savedTask.setCompletado(completed);

        if (task.getDiario()) {
            savedTask.setDia(task.getDias().toString()); // Assuming getDias() returns a list of days
            // You might need to handle HoraComplecion and FechaComplecion differently for daily tasks
        } else {
            savedTask.setHoraComplecion(getCurrentTime()); // Assuming you have a getCurrentTime() method
            savedTask.setFechaComplecion(new Date()); // Or getCurrentDate()
        }

        // ... (set other fields) ...

        return savedTask;
    }

    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void handleTaskCompletion(Tarea task, boolean isChecked) {
        String taskKey = task.getKey();

        if (isChecked) {
            SavedTarea savedTask = createSavedTarea(task, true);
            preventDuplicateSavedTask(savedTask, task);

            if (!task.getDiario()) {
                // Use updateChildren to update the task with completed: true
                DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("tasks").child(taskKey);
                HashMap<String, Object> updates = new HashMap<>();
                updates.put("completado", true); // Update only the "completado" field
                taskRef.updateChildren(updates)
                        .addOnSuccessListener(aVoid -> {
                            // Task updated successfully, now delete it
                            task.deleteFromFirebase(taskKey);
                        })
                        .addOnFailureListener(e -> {
                            // Handle error during update
                        });
            }
        } else {
            // ... (Implement logic to remove from history if necessary) ...
        }

        // Update the completion status in Firebase (for daily tasks)
        if (task.getDiario()) {
            task.updateCompletionStatusInFirebase(taskKey, isChecked);
        }
    }

    private void preventDuplicateSavedTask(SavedTarea savedTask, Tarea task) {
        if (!task.getDiario()) {
            preventDuplicateDeadlineTask(savedTask);
        }
    }

    private void addToHistory(SavedTarea savedTask) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference historyRef = database.getReference("history");

        // Check if "history" node exists, create if not
        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    historyRef.setValue(new HashMap<>()); // Create "history" node
                }

                // Save the history entry
                String historyKey = historyRef.push().getKey();
                if (historyKey != null) {
                    historyRef.child(historyKey).setValue(savedTask);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void showTaskOptionsDialog(Tarea task, View itemView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle(R.string.opciones);
        builder.setItems(new CharSequence[]{
                itemView.getContext().getString(R.string.eliminar),
                itemView.getContext().getString(R.string.modificar)
        }, (dialog, which) -> {
            switch (which) {
                case 0: // Delete
                    deleteTask(task);
                    break;
                case 1: // Modify
                    modifyTask(task);
                    break;
            }
        });
        builder.show();
    }

    private void deleteTask(Tarea task) {
        String taskKey = task.getKey(); // Assuming you have a getKey() method in Tarea

        // Delete the task from Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference("tasks").child(taskKey);
        tasksRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Task deleted successfully
                    // You can update the UI or show a success message here
                    // ...
                })
                .addOnFailureListener(e -> {
                    // Handle error
                    // ...
                });

        // Remove the task from the adapter's list
        taskList.remove(task);
        notifyDataSetChanged();
    }

    private void modifyTask(Tarea task) {
        String taskKey = task.getKey();
        ModificarTarea modificarTareaDialog = ModificarTarea.newInstance(taskKey); // Use newInstance()

        modificarTareaDialog.show(fragmentManager, "modificarTareaDialog");
    }

    @Override
    public int getItemCount() { // Added getItemCount() method
        return taskList.size();
    }

    public void updateTasks(List<Tarea> newTasks) {
        taskList.clear();
        taskList.addAll(newTasks);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskNameTextView;
        public TextView taskTimeTextView;
        public TextView taskDateOrDaysTextView;
        public Switch taskCompletedSwitch;
        // ... (other views for task item) ...

        public ViewHolder(View view) {
            super(view);
            taskNameTextView = view.findViewById(R.id.taskNameTextView);
            taskTimeTextView = view.findViewById(R.id.taskTimeTextView);
            taskDateOrDaysTextView = view.findViewById(R.id.taskDateOrDaysTextView);
            taskCompletedSwitch = view.findViewById(R.id.taskCompletedSwitch);// Assuming you have a TextView with this ID
            // ... (find other views) ...
        }
    }

}

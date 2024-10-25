package com.example.keeptrackbackup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.keeptrackbackup.R;
import com.example.keeptrackbackup.data.Tarea;
import com.example.keeptrackbackup.data.SavedTarea;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class Progreso extends Fragment {

    private ProgressBar progressBar;
    private TextView progressText;
    private TextView overdueTasksText;
    private TextView completedTasksText;
    private TextView pendingTasksText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progreso, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        progressText = view.findViewById(R.id.progressText);
        overdueTasksText = view.findViewById(R.id.overdueTasksText);
        completedTasksText = view.findViewById(R.id.completedTasksText);
        pendingTasksText = view.findViewById(R.id.pendingTasksText);

        loadTaskData();

        return view;
    }

    private void loadTaskData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference("tasks");


        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalTasks = 0;
                int completedTasks = 0;
                int overdueTasks = 0;

                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    Tarea tarea = taskSnapshot.getValue(Tarea.class);
                    if (tarea != null) {
                        totalTasks++;
                        if (tarea.isCompletada()) {
                            completedTasks++;
                        } else { // Only check for overdue if not completed
                            // Check for overdue tasks
                            Date deadline = tarea.getFecha();
                            if (deadline != null) {
                                if (deadline.before(new Date())) { // Compare with current date
                                    overdueTasks++;
                                }
                            }
                        }
                    }
                }

                // Calculate progress percentage
                int progress = (totalTasks > 0) ? (completedTasks * 100 / totalTasks) : 0;

                // Update UI elements
                progressBar.setProgress(progress);
                progressText.setText(progress + "% Completado");
                overdueTasksText.setText("Tareas Atrasadas: " + overdueTasks);
                completedTasksText.setText("Tareas Completadas: " + completedTasks);
                pendingTasksText.setText("Tareas Pendientes: " + (totalTasks - completedTasks));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                // ...
            }
        });
    }
}
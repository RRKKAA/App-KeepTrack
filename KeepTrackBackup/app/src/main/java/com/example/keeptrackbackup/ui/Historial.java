package com.example.keeptrackbackup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keeptrackbackup.R;
import com.example.keeptrackbackup.data.SavedTarea;
import com.example.keeptrackbackup.data.SavedTaskAdapter; // Create a SavedTaskAdapter
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Historial extends Fragment implements SavedTaskAdapter.OnItemLongClickListener {

    private RecyclerView historialRecyclerView;
    private TextView noHistoryText;
    private SavedTaskAdapter savedTaskAdapter; // Use SavedTaskAdapter

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial, container, false);

        historialRecyclerView = view.findViewById(R.id.historialRecyclerView);
        noHistoryText = view.findViewById(R.id.noHistoryText);

        historialRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        savedTaskAdapter = new SavedTaskAdapter(new ArrayList<>()); // Initialize with empty list
        historialRecyclerView.setAdapter(savedTaskAdapter);
        savedTaskAdapter.setOnItemLongClickListener(this);

        loadHistoryData();

        return view;
    }

    @Override
    public void onItemLongClick(SavedTarea savedTask) {
        // Show confirmation dialog and delete the task
        new AlertDialog.Builder(requireContext())
                .setTitle("Borrar tarea")
                .setMessage("Seguro que quieres eliminar esta tarea?")
                .setPositiveButton("Borrar", (dialog, which) -> {
                    deleteSavedTask(savedTask);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteSavedTask(SavedTarea savedTask) {
        // Get the task key
        String taskKey = savedTask.getKey(); // Assuming you have a getKey() method in SavedTarea

        // Delete the task from Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference historyRef = database.getReference("history").child(taskKey);
        historyRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Task deleted successfully
                    // Update the UI or show a success message here
                    savedTaskAdapter.removeTask(savedTask); // Assuming you have a method to remove the task from the adapter
                })
                .addOnFailureListener(e -> {
                    // Handle error
                    // ...
                });
    }

    private void loadHistoryData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference historyRef = database.getReference("history");

        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SavedTarea> historyList = new ArrayList<>();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    SavedTarea savedTarea = taskSnapshot.getValue(SavedTarea.class);
                    if (savedTarea != null) {
                        savedTarea.setKey(taskSnapshot.getKey());
                        historyList.add(savedTarea);
                    }
                }

                savedTaskAdapter.updateTasks(historyList);

                // Update visibility of views based on history list size
                if (savedTaskAdapter.getItemCount() == 0) {
                    noHistoryText.setVisibility(View.VISIBLE);
                    historialRecyclerView.setVisibility(View.GONE);
                } else {
                    noHistoryText.setVisibility(View.GONE);
                    historialRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                // ...
            }
        });
    }
}
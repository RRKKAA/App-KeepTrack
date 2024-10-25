package com.example.keeptrackbackup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keeptrackbackup.R;
import com.example.keeptrackbackup.data.Tarea;
import com.example.keeptrackbackup.data.TaskAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaTareasDiarias extends Fragment {

    private RecyclerView ListaTareasDiarias;
    private TextView SinTareasDiarias;
    private List<Tarea> taskList = new ArrayList<>();
    private TaskAdapter taskAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_tareas_diarias, container, false); // Updated layout file

        ListaTareasDiarias = view.findViewById(R.id.ListaTareas2);
        SinTareasDiarias = view.findViewById(R.id.SinTareas2);

        ListaTareasDiarias.setLayoutManager(new LinearLayoutManager(requireContext()));

        taskAdapter = new TaskAdapter(taskList, getView(), getParentFragmentManager());
        ListaTareasDiarias.setAdapter(taskAdapter);

        loadTaskData();

        return view;
    }

    private void loadTaskData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference("tasks");

        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Tarea> taskList = new ArrayList<>();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    Tarea tarea = taskSnapshot.getValue(Tarea.class);
                    if (tarea != null && tarea.getDiario()) { // Filter for diario == true
                        taskList.add(tarea);
                    }
                }

                taskAdapter.updateTasks(taskList);

                // Update visibility of views based on task list size
                if (taskAdapter.getItemCount() == 0) {
                    SinTareasDiarias.setVisibility(View.VISIBLE);
                    ListaTareasDiarias.setVisibility(View.GONE);
                } else {
                    SinTareasDiarias.setVisibility(View.GONE);
                    ListaTareasDiarias.setVisibility(View.VISIBLE);
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
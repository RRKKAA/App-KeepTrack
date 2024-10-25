package com.example.keeptrackbackup.ui;

import com.example.keeptrackbackup.R;
import com.example.keeptrackbackup.data.Tarea;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class Logros extends Fragment {

    private CheckBox allTasksCompletedCheckbox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logros, container, false);

        allTasksCompletedCheckbox = view.findViewById(R.id.allTasksCompletedCheckbox);

        loadTaskData();

        return view;
    }

    private void loadTaskData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference("tasks");

        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean allTasksCompleted = true;

                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    Tarea tarea = taskSnapshot.getValue(Tarea.class);
                    if (tarea != null && !tarea.isCompletada()) {
                        allTasksCompleted = false;
                        break;
                    }
                }

                allTasksCompletedCheckbox.setChecked(allTasksCompleted);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                // ...
            }
        });
    }
}
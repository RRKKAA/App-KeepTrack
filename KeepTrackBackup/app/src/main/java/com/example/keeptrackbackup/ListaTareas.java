package com.example.keeptrackbackup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keeptrackbackup.R;
import com.example.keeptrackbackup.Tarea;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaTareas extends Fragment {

    private RecyclerView ListaTareas;
    private TextView SinTareas;
    private TaskAdapter taskAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_tareas, container, false);

        ListaTareas = view.findViewById(R.id.ListaTareas1);
        SinTareas = view.findViewById(R.id.SinTareas1);

        ListaTareas.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadTaskData();

        if (taskAdapter.getItemCount() == 0) {
            SinTareas.setVisibility(View.VISIBLE);
            ListaTareas.setVisibility(View.GONE);
        } else {
            SinTareas.setVisibility(View.GONE);
            ListaTareas.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void loadTaskData() {
        Gson gson = new Gson();
        List<Tarea> taskList = new ArrayList<>();

        try (FileInputStream fis = requireContext().openFileInput("tarea.json");
             InputStreamReader isr = new InputStreamReader(fis)) {
            taskList = gson.fromJson(isr, new TypeToken<List<Tarea>>() {}.getType());
        } catch (IOException e) {
            // Handle file reading error (e.g., file not found)
            e.printStackTrace();
        }

        // Filter tasks with diario == false
        List<Tarea> filteredTasks = taskList.stream()
                .filter(task -> !task.getDiario())
                .collect(Collectors.toList());

        taskAdapter = new TaskAdapter(filteredTasks); // Initialize adapter here
        ListaTareas.setAdapter(taskAdapter);
    }
}
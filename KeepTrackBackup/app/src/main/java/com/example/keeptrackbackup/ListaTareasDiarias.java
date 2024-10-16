package com.example.keeptrackbackup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaTareasDiarias extends Fragment {

    private RecyclerView ListaTareasDiarias;
    private TextView SinTareasDiarias;
    private TaskAdapter taskAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_tareas_diarias, container, false); // Updated layout file

        ListaTareasDiarias = view.findViewById(R.id.ListaTareas2);
        SinTareasDiarias = view.findViewById(R.id.SinTareas2);

        ListaTareasDiarias.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadTaskData();

        if (taskAdapter.getItemCount() == 0) {
            SinTareasDiarias.setVisibility(View.VISIBLE);
            ListaTareasDiarias.setVisibility(View.GONE);
        } else {
            SinTareasDiarias.setVisibility(View.GONE);
            ListaTareasDiarias.setVisibility(View.VISIBLE);
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

        // Filter tasks with diario == true
        List<Tarea> filteredTasks = taskList.stream()
                .filter(Tarea::getDiario) // Changed filter condition
                .collect(Collectors.toList());

        taskAdapter = new TaskAdapter(filteredTasks); // Initialize adapter here
        ListaTareasDiarias.setAdapter(taskAdapter); // Updated RecyclerView
    }
}
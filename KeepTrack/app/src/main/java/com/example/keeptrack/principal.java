package com.example.keeptrack;

import com.example.keeptrack.FileHelper;
import com.example.keeptrack.Tarea;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class principal extends AppCompatActivity {

    private ListView tareasListView;
    private ArrayAdapter<Tarea> taskAdapter;

    @Override
    protected void onResume() {
        super.onResume();

        List<Tarea> tasks = FileHelper.readTasksFromFile(this); // Read tasks from file

        // Update ListView adapter
        taskAdapter.clear();
        taskAdapter.addAll(tasks);
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tareasListView = findViewById(R.id.lista_tareas);
        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        tareasListView.setAdapter(taskAdapter);

        FloatingActionButton nuevatarea = findViewById(R.id.nuevatarea);
        Button historial = findViewById(R.id.historial);
        Button logros = findViewById(R.id.logros);
        Button progreso = findViewById(R.id.progreso);

        nuevatarea.setOnClickListener(v -> {
            nuevatarea.setVisibility(View.GONE);
            crear_tarea cr = new crear_tarea();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, cr)
                    .addToBackStack(null)
                    .commit();
        });
        historial.setOnClickListener(v -> {
            nuevatarea.setVisibility(View.GONE);
            historial cr = new historial();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, cr)
                    .addToBackStack(null)
                    .commit();
        });
        logros.setOnClickListener(v -> {
            nuevatarea.setVisibility(View.GONE);
            logros cr = new logros();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, cr)
                    .addToBackStack(null)
                    .commit();
        });
        progreso.setOnClickListener(v -> {
            nuevatarea.setVisibility(View.GONE);
            progreso cr = new progreso();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, cr)
                    .addToBackStack(null)
                    .commit();
        });

        //getSupportFragmentManager().beginTransaction()
                //.add(R.id.fragmentos, new tareas())
                //.commit();
    }

    public void setSupportActionBar(Toolbar tb) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }
}
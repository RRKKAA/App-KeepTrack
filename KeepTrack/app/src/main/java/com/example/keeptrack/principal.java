package com.example.keeptrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class principal extends AppCompatActivity {

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
        FloatingActionButton nuevatarea = findViewById(R.id.nuevatarea);
        Button historial = findViewById(R.id.historial);
        Button logros = findViewById(R.id.logros);
        Button progreso = findViewById(R.id.progreso);

        nuevatarea.setOnClickListener(v -> {
            crear_tarea cr = new crear_tarea();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, cr)
                    .addToBackStack(null)
                    .commit();
        });
        historial.setOnClickListener(v -> {
            historial cr = new historial();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, cr)
                    .addToBackStack(null)
                    .commit();
        });
        logros.setOnClickListener(v -> {
            logros cr = new logros();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, cr)
                    .addToBackStack(null)
                    .commit();
        });
        progreso.setOnClickListener(v -> {
            progreso cr = new progreso();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, cr)
                    .addToBackStack(null)
                    .commit();
        });
    }

    public void setSupportActionBar(Toolbar tb) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }
}
package com.example.keeptrackbackup;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PaginaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        Button Tareas1 = findViewById(R.id.BotonTareas1);
        Button Tareas2 = findViewById(R.id.BotonTareas2);
        Button CrearTareas = findViewById(R.id.BotonCrearTareas);
        Button Historial = findViewById(R.id.BotonHistorial);
        Button Logros = findViewById(R.id.BotonLogros);
        Button Progreso = findViewById(R.id.BotonProgreso);

        Tareas1.setOnClickListener(v -> {
            ListaTareas fr = new ListaTareas();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, fr)
                    .addToBackStack(null)
                    .commit();

        });

        Tareas2.setOnClickListener(v -> {
            ListaTareasDiarias fr = new ListaTareasDiarias();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, fr)
                    .addToBackStack(null)
                    .commit();

        });

        CrearTareas.setOnClickListener(v -> {
            CrearTarea fr = new CrearTarea();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, fr)
                    .addToBackStack(null)
                    .commit();

        });

        Historial.setOnClickListener(v -> {
            Historial fr = new Historial();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, fr)
                    .addToBackStack(null)
                    .commit();

        });

        Logros.setOnClickListener(v -> {
            Logros fr = new Logros();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, fr)
                    .addToBackStack(null)
                    .commit();

        });

        Progreso.setOnClickListener(v -> {
            Progreso fr = new Progreso();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentos, fr)
                    .addToBackStack(null)
                    .commit();

        });


    }
}
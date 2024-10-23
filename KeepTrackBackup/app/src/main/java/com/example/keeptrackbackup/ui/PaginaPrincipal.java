package com.example.keeptrackbackup.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.keeptrackbackup.R;
import com.google.android.material.navigation.NavigationView;
          
public class PaginaPrincipal extends AppCompatActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            fragment = null;


            int itemId = item.getItemId(); // Get the menu item ID

            if (itemId == R.id.nav_tareas1) {
                fragment = new ListaTareas();
            } else if (itemId == R.id.nav_tareas2) {
                fragment = new ListaTareasDiarias();
            } else if (itemId == R.id.nav_crear_tareas) {
                fragment = new CrearTarea();
            } else if (itemId == R.id.nav_historial) {
                fragment = new Historial();
            } else if (itemId == R.id.nav_progreso) {
                fragment = new Progreso();
            } else if (itemId == R.id.nav_logros) {
                fragment = new Logros();
            }
                // ... handle other menu items ...


            if (fragment != null) {
                try {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentos, fragment)
                            .addToBackStack(null)
                            .commit();
                } catch (Exception e) {
                    // Handle fragment transaction errors
                    e.printStackTrace();
                }
            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) { // HomeAsUp button ID
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
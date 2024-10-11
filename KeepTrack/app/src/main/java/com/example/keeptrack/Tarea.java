package com.example.keeptrack;

import androidx.annotation.NonNull;

public class Tarea {
    public String nombre;
    public String alerta;
    public String fecha;
    public String horario;

    public Tarea(String nombre, String alerta, String fecha, String horario) {
        this.nombre = nombre;
        this.alerta = alerta;
        this.fecha = fecha;
        this.horario = horario;
    }

    @Override
    public String toString() {
        return nombre; // Return the task name
    }
}
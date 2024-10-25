package com.example.keeptrackbackup.data;

import java.util.Date;
import java.sql.Time;

public class SavedTarea {
    private String Nombre;
    private String HoraLimite;
    private Date FechaLimite; // For deadline-based tasks
    private String Dia; // For daily tasks
    private String HoraComplecion;
    private Date FechaComplecion; // For deadline-based tasks
    private boolean completado;
    private String key; // Add a field to store the key

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SavedTarea() {
        // Default constructor
    }

    // Getters and setters for all fields
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("Nombre cannot be null or empty");
        }
        this.Nombre = nombre;
    }

    public String getHoraLimite() {
        return HoraLimite;
    }

    public void setHoraLimite(String horaLimite) {
        this.HoraLimite = horaLimite;
    }

    public Date getFechaLimite() {
        return FechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.FechaLimite = fechaLimite;
    }

    public String getDia() {
        return Dia;
    }

    public void setDia(String dia) {
        this.Dia = dia;
    }

    public String getHoraComplecion() {
        return HoraComplecion;
    }

    public void setHoraComplecion(String horaComplecion) {
        this.HoraComplecion = horaComplecion;
    }

    public Date getFechaComplecion() {
        return FechaComplecion;
    }

    public void setFechaComplecion(Date fechaComplecion) {
        this.FechaComplecion = fechaComplecion;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }
}
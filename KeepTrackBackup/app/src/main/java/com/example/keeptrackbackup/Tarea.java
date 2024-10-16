package com.example.keeptrackbackup;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class Tarea {
    private String nombre;
    private String alerta;
    private List<String> dias;
    private Date fecha;
    private Time hora;
    private Boolean diario;
    private Boolean completada;

    public Tarea() {
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("Nombre cannot be null or empty");
        }
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setAlerta(String alerta) {
        if (alerta == null || alerta.isEmpty()) {
            throw new IllegalArgumentException("Alerta cannot be null or empty");
        }
        this.alerta = alerta;
    }

    public String getAlerta() {
        return alerta;
    }

    public void setDias(List<String> dias) {
        this.dias = dias;
    }

    public List<String> getDias() {
        return dias;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public Time getHora() {
        return hora;
    }

    public void setDiario(Boolean diario) {
        this.diario = diario;
    }

    public Boolean getDiario() {
        return diario;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada;
    }

    public Boolean getCompletada() {
        return completada;
    }
}

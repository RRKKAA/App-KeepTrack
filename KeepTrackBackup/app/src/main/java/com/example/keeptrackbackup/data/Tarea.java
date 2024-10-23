package com.example.keeptrackbackup.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class Tarea {

    private String key;

    private String nombre;

    private String alerta;

    private List<String> dias;

    private Date fecha;

    private String hora;

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

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getHora() {
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

    public Boolean isCompletada() {
        return completada;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void saveToFirebase(String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference("tasks");
        tasksRef.child(key).setValue(this);
    }

    public void updateCompletionStatusInFirebase(String key, boolean isCompleted) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference("tasks");
        tasksRef.child(key).child("completada").setValue(isCompleted);
    }

    public static void loadFromFirebase(String key, OnTareaLoadedListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference("tasks");

        tasksRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Tarea tarea = snapshot.getValue(Tarea.class);
                listener.onTareaLoaded(tarea);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onTareaLoadError(error.toException());
            }
        });
    }

    public void deleteFromFirebase(String taskKey) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference("tasks").child(taskKey);
        tasksRef.removeValue();
    }

    public interface OnTareaLoadedListener {
        void onTareaLoaded(Tarea tarea);
        void onTareaLoadError(Exception e);
    }
}

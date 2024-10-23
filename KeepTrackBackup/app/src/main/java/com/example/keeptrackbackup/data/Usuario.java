package com.example.keeptrackbackup.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Usuario {
    @PrimaryKey
    @NonNull
    private String nombre;
    @NonNull
    private String correo;
    @NonNull
    private String contrasenia;

    public Usuario() {
    }

    public Usuario(@NonNull String nombre, @NonNull String correo, @NonNull String contrasenia) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
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

    public void setCorreo(String correo) {
        if (correo == null || correo.isEmpty()) {
            throw new IllegalArgumentException("Correo cannot be null or empty");
        }
        this.correo = correo;
    }
    public String getCorreo() {
        return correo;
    }

    public void setContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.isEmpty()) {
            throw new IllegalArgumentException("Contrasenia cannot be null or empty");
        }
        this.contrasenia = contrasenia;
    }
    public String getContrasenia() {
        return contrasenia;
    }


}

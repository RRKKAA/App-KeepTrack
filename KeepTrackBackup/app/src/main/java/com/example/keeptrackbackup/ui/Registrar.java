package com.example.keeptrackbackup.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.keeptrackbackup.R;
import com.example.keeptrackbackup.data.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registrar extends AppCompatActivity {

    private DatabaseReference database;
    private DatabaseReference usersRef;

    public static String sanitizeEmail(String email) {
        return email.replace(".", "_");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        database = FirebaseDatabase.getInstance().getReference();
        usersRef = database.child("users");

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Registrar", "Firebase connection successful");
                // You can perform further actions here if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Registrar", "Firebase connection failed: " + error.getMessage());
                Toast.makeText(Registrar.this, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registrarUsuario(View view) {

        EditText RegistrarNombre = findViewById(R.id.RegistrarNombre);
        EditText RegistrarCorreo = findViewById(R.id.RegistrarCorreo);
        EditText RegistrarContrasenia = findViewById(R.id.RegistrarContrasenia);

        String nombre = RegistrarNombre.getText().toString();
        String correo = RegistrarCorreo.getText().toString();
        String contrasenia = RegistrarContrasenia.getText().toString();
        String correoSinPunto = sanitizeEmail(correo);

        if (nombre.isEmpty() || correo.isEmpty() || contrasenia.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario(nombre, correoSinPunto, contrasenia);

        usersRef.child(correoSinPunto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(Registrar.this, "Un usuario con este correo ya existe", Toast.LENGTH_SHORT).show();
                } else {
                    usersRef.child(correoSinPunto).setValue(usuario)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(Registrar.this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Registrar.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Registrar.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
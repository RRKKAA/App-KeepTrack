package com.example.keeptrackbackup.ui;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference database;
    private DatabaseReference usersRef;
    private boolean nodesCreated = false;

    public static String sanitizeEmail(String email) {
        return email.replace(".", "_");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference();
        usersRef = database.child("users");

        if (!nodesCreated) {
            createNodes(); // Call the method to create nodes
            nodesCreated = true; // Set the flag to true
        }
    }

    private void createNodes() {
        DatabaseReference tasksRef = database.child("tasks");
        DatabaseReference historyRef = database.child("history");
        DatabaseReference dailyTaskFlagsRef = database.child("history").child("dailyTaskFlags");

        tasksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    tasksRef.setValue(new HashMap<>()); // Create "tasks" node
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    historyRef.setValue(new HashMap<>()); // Create "history" node
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        dailyTaskFlagsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    dailyTaskFlagsRef.setValue(new HashMap<>()); // Create "dailyTaskFlags" node
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    public void goToRegistrar(View view) {
        Intent intent = new Intent(this, Registrar.class);
        startActivity(intent);
    }

    public void login(View v){

        EditText campo1 = this.findViewById(R.id.LoginCorreo);
        String correo = campo1.getText().toString();
        String correoSinPunto = sanitizeEmail(correo);

        EditText campo2 = this.findViewById(R.id.LoginContrasenia);
        String contrasenia = campo2.getText().toString();

        usersRef.child(correoSinPunto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    if (usuario != null && usuario.getContrasenia().equals(contrasenia)) {
                        // Authentication successful
                        Toast.makeText(MainActivity.this, "Bienvenido, " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, PaginaPrincipal.class);
                        startActivity(i);
                    } else {
                        // Incorrect password
                        Toast.makeText(MainActivity.this, "Intente de nuevo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // User not found
                    Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
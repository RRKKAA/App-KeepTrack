package com.example.keeptrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login(View v){

        EditText campo1 = this.findViewById(R.id.email);
        String correo = campo1.getText().toString();

        EditText campo2 = this.findViewById(R.id.password);
        String contrasenia = campo2.getText().toString();

        if(correo.equals("c1") && contrasenia.equals("123")){
        Intent i = new Intent(MainActivity.this,principal.class);
        startActivity(i);
        }else{
            Toast.makeText(this,"Error de ingreso", Toast.LENGTH_SHORT).show();
            }

        }
}
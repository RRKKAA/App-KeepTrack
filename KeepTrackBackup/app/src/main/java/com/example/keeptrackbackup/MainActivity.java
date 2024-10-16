package com.example.keeptrackbackup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v){

        EditText campo1 = this.findViewById(R.id.LoginCorreo);
        String correo = campo1.getText().toString();

        EditText campo2 = this.findViewById(R.id.LoginContrasenia);
        String contrasenia = campo2.getText().toString();

        if(correo.equals("c1") && contrasenia.equals("123")){
            Intent i = new Intent(MainActivity.this,PaginaPrincipal.class);
            startActivity(i);
        }else{
            Toast.makeText(this,"Error de ingreso", Toast.LENGTH_SHORT).show();
        }

    }
}
package com.example.keeptrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;

public class principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void setSupportActionBar(Toolbar tb) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }
    public void go_Back(View v){
        Intent i = new Intent(this,principal.class);
        startActivity(i);
    }

    public void new_task(View v){
        Intent i = new Intent(this,crearTarea.class);
        startActivity(i);
    }

    public void new_list(View v){
        Intent i = new Intent(this,crearLista.class);
        startActivity(i);
    }

    public void to_progress(View v){
        Intent i = new Intent(this,progreso.class);
        startActivity(i);
    }

    public void to_history(View v){
        Intent i = new Intent(this,historial.class);
        startActivity(i);
    }

    public void to_achievments(View v){
        Intent i = new Intent(this,logros.class);
        startActivity(i);
    }
}
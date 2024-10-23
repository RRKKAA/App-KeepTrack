package com.example.keeptrackbackup;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class Aplicacion extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        // Initialize other global resources
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

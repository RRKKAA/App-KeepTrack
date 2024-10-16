package com.example.keeptrackbackup;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.time.LocalTime;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CrearTarea extends Fragment {

    private Button crearFechaButton;
    private Button crearHoraButton;
    private Button crearDiasButton;
    private Button crearConfirmarButton;
    private Button crearCancelarButton;
    private List<String> DiasSeleccionados = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_tarea, container, false);

        crearFechaButton = view.findViewById(R.id.CrearFecha);
        crearFechaButton.setOnClickListener(v -> showDatePickerDialog());

        crearHoraButton = view.findViewById(R.id.CrearHora);
        crearHoraButton.setOnClickListener(v -> showTimePickerDialog());

        crearDiasButton = view.findViewById(R.id.CrearDias);
        crearDiasButton.setOnClickListener(v -> showDaySelectionDialog());

        crearConfirmarButton = view.findViewById(R.id.CrearConfirmar);
        crearConfirmarButton.setOnClickListener(v -> createAndSaveTarea());

        crearCancelarButton= view.findViewById(R.id.CrearCancelar);
        crearCancelarButton.setOnClickListener(v -> cancelarTarea());


        return view;
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    crearFechaButton.setText(selectedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute1) -> {
                    // Update button text with selected time
                    String selectedTime = hourOfDay + ":" + minute1;
                    crearHoraButton.setText(selectedTime);
                },
                hour, minute, true); // true for 24-hour format

        timePickerDialog.show();
    }

    private void showDaySelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_dias, null);
        builder.setView(dialogView);

        CheckBox lunes = dialogView.findViewById(R.id.lunes);
        CheckBox martes = dialogView.findViewById(R.id.martes);
        CheckBox miercoles = dialogView.findViewById(R.id.miercoles);
        CheckBox jueves = dialogView.findViewById(R.id.jueves);
        CheckBox viernes = dialogView.findViewById(R.id.viernes);
        CheckBox sabado = dialogView.findViewById(R.id.sabado);
        CheckBox domingo = dialogView.findViewById(R.id.domingo);

        builder.setPositiveButton(R.string.btn_ok, (dialog, which) -> {
            DiasSeleccionados.clear();

            if (lunes.isChecked()) {
                DiasSeleccionados.add("Lun");
            }
            if (martes.isChecked()) {
                DiasSeleccionados.add("Mar");
            }
            if (miercoles.isChecked()) {
                DiasSeleccionados.add("Mie");
            }
            if (jueves.isChecked()) {
                DiasSeleccionados.add("Jue");
            }
            if (viernes.isChecked()) {
                DiasSeleccionados.add("Vie");
            }
            if (sabado.isChecked()) {
                DiasSeleccionados.add("Sab");
            }
            if (domingo.isChecked()) {
                DiasSeleccionados.add("Dom");
            }

            updateButtonText();
        });

        builder.setNegativeButton(R.string.btn_cancelar, null);
        builder.show();
    }

    private void updateButtonText() {
        if (DiasSeleccionados.isEmpty()) {
            crearDiasButton.setText(R.string.seleccione_dias);
        } else {
            String dias = String.join(", ", DiasSeleccionados);
            crearDiasButton.setText(dias);
        }
    }

    private void cancelarTarea(){
        NavController navController = Navigation.findNavController(requireView());
        navController.navigateUp();
    }

    private void createAndSaveTarea() {
        Tarea tarea = new Tarea();

        EditText nombreEditText = getView().findViewById(R.id.CrearNombre);
        if (nombreEditText != null) {
            tarea.setNombre(nombreEditText.getText().toString());
        }

        EditText alertaEditText = getView().findViewById(R.id.CrearAlerta);
        if (alertaEditText != null) {
            tarea.setAlerta(alertaEditText.getText().toString());
        }

        tarea.setDias(DiasSeleccionados);

        String fechaString = crearFechaButton.getText().toString();
        if (fechaString.isEmpty()) {
            tarea.setFecha(null); // Set to null if empty
        } else {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = dateFormat.parse(fechaString);
                tarea.setFecha(fecha);
            } catch (ParseException e) {
                // Handle parsing error
                e.printStackTrace();
            }
        }

        String horaString = crearHoraButton.getText().toString();
        if (horaString.isEmpty()) {
            tarea.setHora(null); // Set to null if empty
        } else {
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                Date horaDate = timeFormat.parse(horaString);
                Time hora = new Time(horaDate.getTime());
                tarea.setHora(hora);
            } catch (ParseException e) {
                // Handle parsing error
                e.printStackTrace();
            }
        }

        tarea.setDiario(((android.widget.Switch) getView().findViewById(R.id.TareaDiaria)).isChecked());
        tarea.setCompletada(false);

        saveTareatoFile(tarea);
        NavController navController = Navigation.findNavController(requireView());
        navController.navigateUp();
    }

    private void saveTareatoFile(Tarea newTarea) {
        Gson gson = new Gson();
        List<Tarea> tareas = new ArrayList<>();

        try (FileInputStream fis = requireContext().openFileInput("tarea.json");
             InputStreamReader isr = new InputStreamReader(fis)) {
            tareas = gson.fromJson(isr, new TypeToken<List<Tarea>>() {}.getType());
        } catch (IOException e) {
            // Handle file reading error (e.g., file not found)
            e.printStackTrace();
        }

        tareas.add(newTarea); // Add the new task

        try (FileOutputStream fos = requireContext().openFileOutput("tarea.json", Context.MODE_PRIVATE);
             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            gson.toJson(tareas, osw); // Save the updated list
        } catch (IOException e) {
            // Handle file saving error
            e.printStackTrace();
        }
    }
}
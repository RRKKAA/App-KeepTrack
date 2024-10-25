package com.example.keeptrackbackup.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.keeptrackbackup.R;
import com.example.keeptrackbackup.data.Tarea;

public class ModificarTarea extends DialogFragment {

    public static final String ARG_TASK_KEY = "task_key";
    private Tarea tarea;
    private String taskKey;

    private Button crearFechaButton;
    private Button crearHoraButton;
    private Button crearDiasButton;
    private List<String> DiasSeleccionados = new ArrayList<>();

    public static ModificarTarea newInstance(String taskKey) {
        ModificarTarea fragment = new ModificarTarea();
        Bundle args = new Bundle();
        args.putString(ARG_TASK_KEY, taskKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_modificar_tarea, null); // Use your layout

        // Initialize views from the layout
        EditText alertaEditText = view.findViewById(R.id.CrearAlerta);
        crearDiasButton = view.findViewById(R.id.CrearDias);
        crearFechaButton = view.findViewById(R.id.CrearFecha);
        crearHoraButton = view.findViewById(R.id.CrearHora);

        // Set up listeners for buttons and other interactions
        crearDiasButton.setOnClickListener(v -> showDaySelectionDialog());
        crearFechaButton.setOnClickListener(v -> showDatePickerDialog());
        crearHoraButton.setOnClickListener(v -> showTimePickerDialog());

        String taskKey = getArguments().getString(ARG_TASK_KEY);
        Tarea.loadFromFirebase(taskKey, new Tarea.OnTareaLoadedListener() {
            @Override
            public void onTareaLoaded(Tarea loadedTarea) {
                tarea = loadedTarea;
            }

            @Override
            public void onTareaLoadError(java.lang.Exception e) {
                // Handle error loading task
                // ...
            }
        });

        builder.setView(view)
                .setTitle("Modificar Tarea")
                .setPositiveButton("Guardar", (dialog, id) -> {
                    // Update task data and save to Firebase
                    String alerta = alertaEditText.getText().toString();
                    String fechaString = crearFechaButton.getText().toString();
                    String horaString = crearHoraButton.getText().toString();

                    // ... (Get values from other views: DiasSeleccionados, fecha, hora) ...

                    if (!alerta.isEmpty()) {
                        tarea.setAlerta(alerta);
                    }
                    if (!DiasSeleccionados.isEmpty()) {
                        tarea.setDias(DiasSeleccionados);
                    }
                    if (!fechaString.isEmpty() && !fechaString.equals(getString(R.string.seleccione_fecha))) {
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date fecha = dateFormat.parse(fechaString);
                            tarea.setFecha(fecha);
                        } catch (ParseException e) {
                            // Handle parsing error
                            e.printStackTrace();
                        }
                    }
                    if (!horaString.isEmpty() && !horaString.equals(getString(R.string.seleccione_hora))) {
                        tarea.setHora(horaString);
                    }
                    // ... (Set other values: fecha, hora) ...

                    tarea.saveToFirebase(taskKey);
                    dismiss(); // Close the dialog
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dismiss(); // Close the dialog
                });

        return builder.create();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String currentDate = crearFechaButton.getText().toString();
        if (!currentDate.equals(getString(R.string.seleccione_fecha))) { // Assuming "Seleccione fecha" is your default button text
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = dateFormat.parse(currentDate);
                calendar.setTime(fecha);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                // Handle parsing error, if any
                e.printStackTrace();
            }
        }

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

        String currentTime = crearHoraButton.getText().toString();
        if (!currentTime.equals(getString(R.string.seleccione_hora))) { // Assuming "Seleccione hora" is your default button text
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                Date horaDate = timeFormat.parse(currentTime);
                calendar.setTime(horaDate);
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
            } catch (ParseException e) {
                // Handle parsing error, if any
                e.printStackTrace();
            }
        }

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

        lunes.setChecked(DiasSeleccionados.contains("Lun"));
        martes.setChecked(DiasSeleccionados.contains("Mar"));
        miercoles.setChecked(DiasSeleccionados.contains("Mie"));
        jueves.setChecked(DiasSeleccionados.contains("Jue"));
        viernes.setChecked(DiasSeleccionados.contains("Vie"));
        sabado.setChecked(DiasSeleccionados.contains("Sab"));
        domingo.setChecked(DiasSeleccionados.contains("Dom"));

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
}
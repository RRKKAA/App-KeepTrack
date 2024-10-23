package com.example.keeptrackbackup.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.keeptrackbackup.R;
import com.example.keeptrackbackup.data.Tarea;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ModificarTarea extends Fragment {

    private static final String ARG_TASK_KEY = "task_key";
    private Tarea tarea;
    private String taskKey;

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
        crearConfirmarButton.setOnClickListener(v -> updateAndSaveTarea());

        crearCancelarButton= view.findViewById(R.id.CrearCancelar);
        crearCancelarButton.setOnClickListener(v -> cancelarTarea());

        taskKey = getArguments().getString(ARG_TASK_KEY);

        Tarea.loadFromFirebase(taskKey, new Tarea.OnTareaLoadedListener() {
            @Override
            public void onTareaLoaded(Tarea loadedTarea) {
                tarea = loadedTarea;
                // Populate UI elements with task data
                populateUIWithTaskData(tarea);
            }

            @Override
            public void onTareaLoadError(Exception e) {
                // Handle error loading task
                // ...
            }
        });


        return view;
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

    private void cancelarTarea(){
        NavController navController = Navigation.findNavController(requireView());
        navController.navigateUp();
    }

    private void populateUIWithTaskData(Tarea task) {
        // Populate UI elements with task data
        EditText nombreEditText = getView().findViewById(R.id.CrearNombre);
        nombreEditText.setText(task.getNombre());

        EditText alertaEditText = getView().findViewById(R.id.CrearAlerta);
        alertaEditText.setText(task.getAlerta());

        DiasSeleccionados = task.getDias();
        updateButtonText();

        if (task.getFecha() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            crearFechaButton.setText(dateFormat.format(task.getFecha()));
        }

        if (task.getHora() != null) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            crearHoraButton.setText(timeFormat.format(task.getHora()));
        }

        ((android.widget.Switch) getView().findViewById(R.id.TareaDiaria)).setChecked(task.getDiario());
    }

    private void updateAndSaveTarea() {

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
            tarea.setHora(horaString);
        }

        tarea.setDiario(((android.widget.Switch) getView().findViewById(R.id.TareaDiaria)).isChecked());
        tarea.setCompletada(false);

        tarea.saveToFirebase(taskKey);

        NavController navController = Navigation.findNavController(requireView());
        navController.navigateUp();
    }

    public static ModificarTarea newInstance(String taskKey) {
        ModificarTarea fragment = new ModificarTarea();
        Bundle args = new Bundle();
        args.putString(ARG_TASK_KEY, taskKey);
        fragment.setArguments(args);
        return fragment;
    }
}
package com.example.keeptrack;

import com.example.keeptrack.FileHelper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.EditText;
import android.widget.TimePicker;


import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link crear_tarea#newInstance} factory method to
 * create an instance of this fragment.
 */
public class crear_tarea extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String selectedDate = "";
    private String selectedTime = "";

    public crear_tarea() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment crear_tarea.
     */
    // TODO: Rename and change types and number of parameters
    public static crear_tarea newInstance(String param1, String param2) {
        crear_tarea fragment = new crear_tarea();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_tarea, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        if (getActivity() != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }

        return inflater.inflate(R.layout.fragment_crear_tarea, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menulista, menu); // Inflate your menu
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goback) {
            requireActivity().onBackPressed();
            // Handle search action
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button selectFecha = view.findViewById(R.id.select_fecha);
        Button selectHorario = view.findViewById(R.id.select_horario);
        Button tareaCrear = view.findViewById(R.id.btn_crear);
        Button tareaCancelar = view.findViewById(R.id.btn_cancelar);

        selectFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                                selectFecha.setText(selectedDate);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
        selectHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        requireContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selectedTime = hourOfDay + ":" + minute;
                                selectHorario.setText(selectedTime); // Update button text
                            }
                        },
                        hour, minute, true); // true for 24-hour format
                timePickerDialog.show();
            }
        });
        tareaCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombreInput = view.findViewById(R.id.nombre); // Replace with your input field IDs
                EditText alertaInput = view.findViewById(R.id.alerta);
                // Get input values (nombre, alerta, selectedDate, selectedTime)
                String nombre = nombreInput.getText().toString();
                String alerta = alertaInput.getText().toString();
                // ...

                Tarea newTask = new Tarea(nombre, alerta, selectedDate, selectedTime);

                List<Tarea> tasks = FileHelper.readTasksFromFile(requireContext());
                tasks.add(newTask);
                FileHelper.writeTasksToFile(requireContext(), tasks);
                requireActivity().onBackPressed();

                // Optionally, navigate back to the previous fragment or activity
                // requireActivity().onBackPressed();
            }
        });
        tareaCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
    }

}
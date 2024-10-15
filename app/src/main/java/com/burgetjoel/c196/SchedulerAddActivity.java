package com.burgetjoel.c196;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SchedulerAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseHelper mydb;
     Spinner schedulerSpinner;
     String name;
     String spinnerChoice;
     String startDateString;
     String endDateString;
     TextView nameTextView, startDateTextView, endDateTextView;
     Button cancelButton, saveButton;
     DatePickerHelper startPicker = new DatePickerHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler_add_term);
         mydb = new DatabaseHelper(this);

         nameTextView = findViewById(R.id.scheduler_add_name);
         startDateTextView = findViewById(R.id.scheduler_add_start_date);
         endDateTextView = findViewById(R.id.scheduler_add_end_date);

         startDateTextView.setOnClickListener(v -> {
             startPicker.showDatePicker(startDateTextView);
         });

         endDateTextView.setOnClickListener(v -> {
             startPicker.showDatePicker(endDateTextView);
         });

         endDateTextView = findViewById(R.id.scheduler_add_end_date);

        schedulerSpinner = findViewById(R.id.scheduler_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.schedulerSpinnerChoices,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schedulerSpinner.setAdapter(adapter);
        schedulerSpinner.setOnItemSelectedListener(this);

        saveButton = findViewById(R.id.scheduler_add_term_save_button);
        cancelButton = findViewById(R.id.scheduler_add_cancel_button);

        saveButton.setOnClickListener(v -> {
            name = nameTextView.getText().toString().trim();
            startDateString = startDateTextView.getText().toString().trim();
            endDateString = endDateTextView.getText().toString().trim();
            mydb.addScheduler(name, startDateString, endDateString, spinnerChoice);
            setResult(Activity.RESULT_OK);
            finish();
        });

        cancelButton.setOnClickListener(v-> finish());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerChoice = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spinnerChoice = parent.getItemAtPosition(1).toString();
    }
}
package com.burgetjoel.c196;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class SchedulerEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
        private DatabaseHelper mydb;
        Spinner schedulerSpinner, termSpinner;
        int id;
        String name;
        String spinnerChoice;
        String startDateString;
        String endDateString;
        String color;
        TextView nameTextView, startDateTextView, endDateTextView;
        Button cancelButton, saveButton;
        DatePickerHelper startPicker = new DatePickerHelper(this);
        ArrayList<String> terms = new ArrayList<>();

    Cursor cursor;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scheduler_edit);
            mydb = new DatabaseHelper(this);

            nameTextView = findViewById(R.id.scheduler_edit_name);
            startDateTextView = findViewById(R.id.scheduler_edit_start_date);
            endDateTextView = findViewById(R.id.scheduler_edit_end_date);
            terms.add("Select Term");

            cursor = mydb.readAllScheduler();
            int numRows = cursor.getCount();
            int numColumns = cursor.getColumnCount();
            String[][] termIds = new String[numRows][numColumns];

            Log.i("cursor count", String.valueOf(cursor.getCount()));
            if (cursor.moveToFirst()) {
                int i = 0;
                do {
                    for (int j = 0; j < numColumns; j++) {
                        termIds[i][j] = cursor.getString(j);
                    }
                    terms.add(cursor.getString(1));
                    i++;
                } while (cursor.moveToNext());
            }

            for (String name: terms) {
                Log.d("name", name);
            }

            startDateTextView.setOnClickListener(v -> startPicker.showDatePicker(startDateTextView));

            endDateTextView.setOnClickListener(v -> startPicker.showDatePicker(endDateTextView));

            endDateTextView = findViewById(R.id.scheduler_edit_end_date);

            schedulerSpinner = findViewById(R.id.scheduler_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.schedulerSpinnerChoices,
                    android.R.layout.simple_spinner_item
            );

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            schedulerSpinner.setAdapter(adapter);
            schedulerSpinner.setOnItemSelectedListener(this);

            termSpinner = findViewById(R.id.scheduler_term_spinner);
            ArrayAdapter<String> termAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    terms
            );
            termSpinner.setAdapter(termAdapter);
            termSpinner.setOnItemSelectedListener(this);

            saveButton = findViewById(R.id.scheduler_edit_term_save_button);
            cancelButton = findViewById(R.id.scheduler_edit_cancel_button);

            saveButton.setOnClickListener(v -> {
                name = nameTextView.getText().toString().trim();
                startDateString = startDateTextView.getText().toString().trim();
                endDateString = endDateTextView.getText().toString().trim();
                mydb.editScheduler(String.valueOf(id), name, startDateString, endDateString, spinnerChoice);
                setResult(Activity.RESULT_OK);
                finish();
            });
            cancelButton.setOnClickListener(v-> finish());
        }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.scheduler_spinner) {
                spinnerChoice = parent.getItemAtPosition(position).toString();
            }

            if (parent.getId() == R.id.scheduler_term_spinner) {

                try{
                    cursor = mydb.readSchedulerByName(termSpinner.getSelectedItem().toString());
                    cursor.moveToFirst();
                    do {
                        nameTextView.setText(cursor.getString(1));
                        startDateTextView.setText(cursor.getString(2));
                        endDateTextView.setText(cursor.getString(3));

                    } while (cursor.moveToNext());

                    nameTextView.setText(cursor.getString(1));

                    startDateTextView.setText(cursor.getString(3));
                    endDateTextView.setText(cursor.getString(4));
                    color = cursor.getString(5);

                }catch (Exception e){
                    Toast.makeText(this, "Error, name not found", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

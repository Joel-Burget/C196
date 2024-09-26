package com.burgetjoel.c196;

import static android.app.PendingIntent.getActivity;

import static com.burgetjoel.c196.NotificationHelper.createNotificationChannel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTermActivity extends AppCompatActivity {

    EditText name_input;
    TextView termStart, termEnd;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        name_input = findViewById(R.id.name_input);
        termStart = findViewById(R.id.termStartDate);
        termEnd = findViewById(R.id.termEndDate);

        DatePickerHelper startPicker = new DatePickerHelper(this);
        AlarmHelper alarmHelper = new AlarmHelper(this);

        termStart.setOnClickListener(v ->{
            startPicker.showDatePicker(termStart);
        });

        termEnd.setOnClickListener(v ->{
            startPicker.showDatePicker(termEnd);
        });

        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(v -> {
            DatabaseHelper myDB = new DatabaseHelper(AddTermActivity.this);
            myDB.addTerm(name_input.getText().toString().trim(), termStart.getText().toString().trim(), termEnd.getText().toString().trim());
            Log.i("Date output is: ", termStart.getText().toString().trim());

            try {

                String[] values = termStart.getText().toString().split("/");  // Split by slash
                if (values.length == 3) {
                    int day = Integer.parseInt(values[0]);
                    int month = Integer.parseInt(values[1]);
                    int year = Integer.parseInt(values[2]);
                    int hour = 8;
                    int minute = 0;

                } else {
                    Log.e("Error", "Date format is incorrect, expected dd/MM/yyyy.");
                }
            } catch (Exception e) {
                Log.e("Error", "Failed to parse date: " + e.getMessage());
            }
            finish();
        });

    }
}
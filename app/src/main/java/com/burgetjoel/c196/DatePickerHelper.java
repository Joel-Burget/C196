package com.burgetjoel.c196;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DatePickerHelper {

    private Context context;

    // Constructor to pass the context
    public DatePickerHelper(Context context) {
        this.context = context;
    }

    // Method to show the DatePickerDialog
    public void showDatePicker(final TextView dateTextView) {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context, (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Update the TextView with the selected date
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateTextView.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
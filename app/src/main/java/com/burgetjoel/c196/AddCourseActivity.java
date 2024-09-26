package com.burgetjoel.c196;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

public class AddCourseActivity extends Activity implements OnItemSelectedListener {

    EditText course_name, instructor_name, instructor_phone, instructor_email, course_notes;
    TextView course_start_date, course_end_date;
    String termID, courseStatus;
    Spinner courseSpinner;
    Button save_button;
    CheckBox checkbox;
    DatePickerHelper startPicker = new DatePickerHelper(this);
    AlarmHelper alarmHelper = new AlarmHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        course_name = findViewById(R.id.course_name_update);
        course_start_date = findViewById(R.id.course_start_date_update);
        course_end_date = findViewById(R.id.course_end_date_update);
        instructor_name = findViewById(R.id.course_instructor_name_update);
        instructor_phone = findViewById(R.id.course_instructor_phone_number_update);
        instructor_email = findViewById(R.id.course_instructor_email_update);
        course_notes = findViewById(R.id.course_notes_update);
        checkbox = findViewById(R.id.course_checkbox);
        getAndSetIntentData();

        //creating spinner adapter
        courseSpinner = findViewById(R.id.course_status_spinner_update);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.termSpinnerChoices,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);
        courseSpinner.setOnItemSelectedListener(this);


        course_start_date.setOnClickListener(v ->{
            startPicker.showDatePicker(course_start_date);
        });

        course_end_date.setOnClickListener(v ->{
            startPicker.showDatePicker(course_end_date);
        });


        save_button = findViewById(R.id.save_edit_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDB = new DatabaseHelper(AddCourseActivity.this);
                myDB.addCourse(termID, course_name.getText().toString().trim(), course_start_date.getText().toString().trim(), course_end_date.getText().toString().trim(),
                        instructor_name.getText().toString().trim(), instructor_phone.getText().toString().trim(), instructor_email.getText().toString().trim(),
                        courseStatus, course_notes.getText().toString().trim());
                try {

                    if(checkbox.isChecked()){
                        String[] startValues = course_start_date.getText().toString().split("/");  // Split by slash
                        if (startValues.length == 3) {
                            int day = Integer.parseInt(startValues[0]);
                            int month = Integer.parseInt(startValues[1]);
                            int year = Integer.parseInt(startValues[2]);
                            int hour = 8;
                            int minute = 0;

                            alarmHelper.scheduleExactAlarm(hour, minute, day, year, month, "Course Start", "Course has started", AlarmReceiver.class);
                        } else {
                            Log.e("Error", "Date format is incorrect, expected dd/MM/yyyy.");
                        }

                        String[] endValues = course_end_date.getText().toString().split("/");  // Split by slash
                        if (endValues.length == 3) {
                            int day = Integer.parseInt(endValues[0]);
                            int month = Integer.parseInt(endValues[1]);
                            int year = Integer.parseInt(endValues[2]);
                            int hour = 8;
                            int minute = 0;
                            alarmHelper.scheduleExactAlarm(hour, minute, day, year, month, "Course End", "Course has ended", AlarmReceiver.class);
                    }

                    } else {
                        Log.e("Error", "Date format is incorrect, expected dd/MM/yyyy.");
                    }
                } catch (Exception e) {
                    Log.e("Error", "Failed to parse date: " + e.getMessage());
                }
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        courseStatus = parent.getItemAtPosition(position).toString();
        Log.i("Course Status should be: ", courseStatus);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        courseStatus = parent.getItemAtPosition(1).toString();
    }

    public void getAndSetIntentData(){
        if(getIntent().hasExtra("termID")){
            termID = getIntent().getStringExtra("termID");
        }else{
            Toast.makeText(this, "No intent data", Toast.LENGTH_SHORT).show();
        }
    }
}
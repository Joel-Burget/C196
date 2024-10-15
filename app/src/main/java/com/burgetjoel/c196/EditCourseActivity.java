package com.burgetjoel.c196;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditCourseActivity extends Activity implements AdapterView.OnItemSelectedListener {
    EditText course_name, course_start_date, course_end_date, instructor_name, instructor_phone, instructor_email, notes_text;
    String courseID, termID, courseName, courseStartDate, courseEndDate, spinnerValue, courseStatus, instructorName, instructorPhone, instructorEmail, notes;
    Spinner courseSpinner;
    Button save_button, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        course_name = findViewById(R.id.course_name_update);
        course_start_date = findViewById(R.id.course_start_date_update);
        course_end_date = findViewById(R.id.course_end_date_update);
        instructor_name = findViewById(R.id.course_instructor_name_update);
        instructor_phone = findViewById(R.id.course_instructor_phone_number_update);
        instructor_email = findViewById(R.id.course_instructor_email_update);
        notes_text = findViewById(R.id.course_notes_update);
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

        save_button = findViewById(R.id.save_edit_button);
        save_button.setOnClickListener(v -> {
            DatabaseHelper myDB = new DatabaseHelper(EditCourseActivity.this);
            myDB.updateCourseData(courseID, course_name.getText().toString().trim(), course_start_date.getText().toString().trim(), course_end_date.getText().toString().trim(),
                    instructor_name.getText().toString().trim(), instructor_phone.getText().toString().trim(), instructor_email.getText().toString().trim(),
                    courseStatus, notes_text.getText().toString().trim());
            setResult(RESULT_OK, null);
            Intent intent = new Intent(EditCourseActivity.this, CourseDetailsActivity.class);
            intent.putExtra("courseID", courseID);
            intent.putExtra("termID", termID);
            intent.putExtra("courseName", course_name.getText().toString().trim());
            intent.putExtra("courseStartDate", course_start_date.getText().toString().trim());
            intent.putExtra("courseEndDate",  course_end_date.getText().toString().trim());
            intent.putExtra("courseStatus", courseStatus);
            intent.putExtra("instructorName", instructor_name.getText().toString().trim());
            intent.putExtra("instructorPhone", instructor_phone.getText().toString().trim());
            intent.putExtra("instructorEmail", instructor_email.getText().toString().trim());
            intent.putExtra("notes", notes_text.getText().toString().trim());

            startActivity(intent);
            finish();
        });

        deleteButton = findViewById(R.id.delete_course_button);
        deleteButton.setOnClickListener(v -> {
            DatabaseHelper myDB = new DatabaseHelper(EditCourseActivity.this);
            myDB.deleteCourseRow(courseID);

            setResult(RESULT_OK);
            finish();
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
        if(getIntent().hasExtra("termID") && getIntent().hasExtra("termID") && getIntent().hasExtra("courseName") && getIntent().hasExtra("courseStartDate") && getIntent().hasExtra("courseEndDate")
                && getIntent().hasExtra("courseStatus") && getIntent().hasExtra("instructorName") && getIntent().hasExtra("instructorPhone") &&
                getIntent().hasExtra("instructorEmail") && getIntent().hasExtra("notes")){
            courseID = getIntent().getStringExtra("courseID");
            termID = getIntent().getStringExtra("termID");
            courseName = getIntent().getStringExtra("courseName");
            courseStartDate = getIntent().getStringExtra("courseStartDate");
            courseEndDate = getIntent().getStringExtra("courseEndDate");
            spinnerValue = getIntent().getStringExtra(courseStatus);

            instructorName = getIntent().getStringExtra("instructorName");
            instructorEmail = getIntent().getStringExtra("instructorEmail");
            instructorPhone = getIntent().getStringExtra("instructorPhone");
            notes = getIntent().getStringExtra("notes");



            course_name.setText(courseName);
            course_start_date.setText(courseStartDate);
            course_end_date.setText(courseEndDate);
            instructor_name.setText(instructorName);
            instructor_phone.setText(instructorPhone);
            instructor_email.setText(instructorEmail);
            notes_text.setText(notes);
        }else{
            Toast.makeText(this, "No intent data", Toast.LENGTH_SHORT).show();
        }
    }
}
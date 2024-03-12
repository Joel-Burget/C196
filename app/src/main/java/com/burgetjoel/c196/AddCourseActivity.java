package com.burgetjoel.c196;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class AddCourseActivity extends Activity implements OnItemSelectedListener {

    EditText course_name, course_start_date, course_end_date, instructor_name, instructor_phone, instructor_email;
    String termID, courseStatus;
    Spinner courseSpinner;
    Button save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        course_name = findViewById(R.id.course_name_input);
        course_start_date = findViewById(R.id.course_start_date_input);
        course_end_date = findViewById(R.id.course_end_date_input);
        instructor_name = findViewById(R.id.course_instructor_name);
        instructor_phone = findViewById(R.id.course_instructor_phone_number);
        instructor_email = findViewById(R.id.course_instructor_email);
        getAndSetIntentData();

        //creating spinner adapter
        courseSpinner = findViewById(R.id.course_status_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.termSpinnerChoices,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);
        courseSpinner.setOnItemSelectedListener(this);

        save_button = findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDB = new DatabaseHelper(AddCourseActivity.this);
                myDB.addCourse(termID, course_name.getText().toString().trim(), course_start_date.getText().toString().trim(), course_end_date.getText().toString().trim(),
                        instructor_name.getText().toString().trim(), instructor_phone.getText().toString().trim(), instructor_email.getText().toString().trim(),
                        courseStatus);
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
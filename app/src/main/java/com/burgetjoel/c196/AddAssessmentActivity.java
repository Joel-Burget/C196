package com.burgetjoel.c196;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddAssessmentActivity extends AppCompatActivity {
    EditText assessmentName, assessmentDate, assessmentStart, assessmentEnd;
    String courseID, radioSelection;
    RadioGroup radioGroup;
    Button addButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        assessmentName = findViewById(R.id.assessment_edit_name_input);
        assessmentDate = findViewById(R.id.assessment_edit_date_input);
        assessmentStart = findViewById(R.id.assessment_edit_start_time_input);
        assessmentEnd = findViewById(R.id.assessment_edit_end_time_input);

        addButton = findViewById(R.id.edit_assessment_button);
        cancelButton = findViewById(R.id.cancel_assessment_button);

        radioGroup = findViewById(R.id.radioGroup);

        getAndSaveIntentData();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                radioSelection = radioButton.getText().toString();
            }
        });

        addButton.setOnClickListener(v ->{
            DatabaseHelper myDB = new DatabaseHelper(AddAssessmentActivity.this);
            myDB.addAssessment(courseID, assessmentName.getText().toString().trim(), radioSelection, assessmentDate.getText().toString().trim(),
                    assessmentStart.getText().toString().trim(), assessmentEnd.getText().toString().trim());
            setResult(1);
            finish();
        });

        cancelButton.setOnClickListener(v ->{
            finish();
        });
    }

    public void getAndSaveIntentData(){
        if(getIntent().hasExtra("courseID")){
            courseID = getIntent().getStringExtra("courseID");
        }else{
            Toast.makeText(this, "No data from intent", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.burgetjoel.c196;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class EditAssessmentActivity extends AppCompatActivity {
    DatabaseHelper assessmentDB;
    String name, start, end, assessmentID, type, date, courseID;
    ArrayList<String> assessmentIDList, nameList, dateList, startList, endList, typeList;
    TextView editNameInput, editStartInput, editEndInput, editDateInput;
    Button saveButton, cancelButton;
    RadioButton performance, objective;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);

        assessmentDB = new DatabaseHelper(EditAssessmentActivity.this);
        assessmentIDList = new ArrayList<>();
        nameList = new ArrayList<>();
        dateList = new ArrayList<>();
        startList = new ArrayList<>();
        endList = new ArrayList<>();
        typeList = new ArrayList<>();

        editNameInput = findViewById(R.id.assessment_edit_name_input);
        editStartInput = findViewById(R.id.assessment_edit_start_time_input);
        editEndInput = findViewById(R.id.assessment_edit_end_time_input);
        editDateInput = findViewById(R.id.assessment_edit_date_input);
        performance = findViewById(R.id.edit_performance);
        objective = findViewById(R.id.edit_objective);
        getAndSetIntentData();

        saveButton = findViewById(R.id.edit_assessment_button);
        saveButton.setOnClickListener(v ->{
            String editedType;
            if(performance.isActivated()){
                editedType = performance.getText().toString().trim();
            }else {
                editedType = objective.getText().toString().trim();
            }
            DatabaseHelper myDB = new DatabaseHelper(EditAssessmentActivity.this);
            myDB.editAssessment(assessmentID, editNameInput.getText().toString().trim(), editedType, editDateInput.getText().toString().trim(), editStartInput.getText().toString().trim(), editEndInput.getText().toString().trim());
            Intent intent = new Intent (EditAssessmentActivity.this, AssessmentDetailsActivity.class);
            intent.putExtra("courseID", courseID);
            intent.putExtra("assessmentID", assessmentID);
            intent.putExtra("assessmentName", editNameInput.getText().toString().trim());
            intent.putExtra("assessmentDate", editDateInput.getText().toString().trim());
            intent.putExtra("startTime", editStartInput.getText().toString().trim());
            intent.putExtra("endTime", editEndInput.getText().toString().trim());
            intent.putExtra("assessmentType", editedType);
            startActivity(intent);
            finish();
        });

        cancelButton = findViewById(R.id.cancel_assessment_button);
        cancelButton.setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void getAndSetIntentData() {
        if (getIntent().hasExtra("assessmentID") && getIntent().hasExtra("courseID") && getIntent().hasExtra("assessmentName") && getIntent().hasExtra("date") &&
        getIntent().hasExtra("startTime") && getIntent().hasExtra("endTime") && getIntent().hasExtra("type")){
            assessmentID = getIntent().getStringExtra("assessmentID");
            courseID = getIntent().getStringExtra("courseID");
            name = getIntent().getStringExtra("assessmentName");
            date = getIntent().getStringExtra("date");
            start = getIntent().getStringExtra("startTime");
            end = getIntent().getStringExtra("endTime");
            type = getIntent().getStringExtra("type");

            editNameInput.setText(getIntent().getStringExtra("assessmentName"));
            editDateInput.setText(getIntent().getStringExtra("date"));
            editStartInput.setText(start);
            editEndInput.setText(end);

            if(Objects.equals(getIntent().getStringExtra("type"), "performance")){
                performance.toggle();
            } else {
                objective.toggle();
            }
        } else {
            Toast.makeText(this, "No intent data", Toast.LENGTH_SHORT).show();
        }
    }
}
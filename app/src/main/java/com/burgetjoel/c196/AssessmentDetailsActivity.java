package com.burgetjoel.c196;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AssessmentDetailsActivity extends AppCompatActivity {
    DatabaseHelper assessmentDB;
    String name, date, start, end, courseID, id, type;
    ArrayList<String> courseIDArray, assessmentID, assessmentName, assessmentDate, startTime, endTime, assessmentType;
    TextView assessmentNameDetail, assessmentDateDetails, assessmentStartDetails, assessmentEndDetails, assessmentTypeDetails;
    AssessmentAdapter customAdaptor;
    FloatingActionButton editAssessmentButton, delete;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            activityResult -> {
                int result = activityResult.getResultCode();
                Intent data = activityResult.getData();

                if(result == RESULT_OK){
                    recreate();
                }
            }
    );
    private void openSecondActivity() {
        Intent intent = new Intent(this, EditAssessmentActivity.class);
        intent.putExtra("courseID", courseID);
        intent.putExtra("assessmentID", id);
        intent.putExtra("assessmentName", name);
        intent.putExtra("date", date);
        intent.putExtra("startTime", start);
        intent.putExtra("endTime", end);
        intent.putExtra("type", type);
        activityResultLauncher.launch(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        editAssessmentButton = findViewById(R.id.editAssessmentFAB);

        assessmentDB = new DatabaseHelper(AssessmentDetailsActivity.this);
        assessmentID = new ArrayList<>();
        assessmentName = new ArrayList<>();
        assessmentDate = new ArrayList<>();
        startTime = new ArrayList<>();
        endTime = new ArrayList<>();
        assessmentType = new ArrayList<>();

        assessmentNameDetail = findViewById(R.id.assessment_name_details);
        assessmentDateDetails = findViewById(R.id.assessment_date_details);
        assessmentStartDetails = findViewById(R.id.assessment_start_details);
        assessmentEndDetails = findViewById(R.id.assessment_end_details);
        assessmentTypeDetails = findViewById(R.id.assessment_type_details);

        customAdaptor = new AssessmentAdapter(this, AssessmentDetailsActivity.this, courseIDArray, assessmentID, assessmentName, assessmentDate,
                startTime, endTime, assessmentType);

        getAndSetIntentData();

        editAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondActivity();
            }
        });

        delete = findViewById(R.id.deleteAssessmentFAB);
        delete.setOnClickListener(v->{
            DatabaseHelper myDB = new DatabaseHelper(AssessmentDetailsActivity.this);
            myDB.deleteAssessmentRow(String.valueOf(Integer.parseInt(id)));
            finish();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            recreate();
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public void getAndSetIntentData(){
        if( getIntent().hasExtra("courseID") && getIntent().hasExtra("assessmentID") && getIntent().hasExtra("assessmentName")
               && getIntent().hasExtra("assessmentDate") && getIntent().hasExtra("startTime")
                && getIntent().hasExtra("endTime") && getIntent().hasExtra("assessmentType")){
            courseID = getIntent().getStringExtra("courseID");
            id = getIntent().getStringExtra("assessmentID");
            name = getIntent().getStringExtra("assessmentName");
            date = getIntent().getStringExtra("assessmentDate");
            start = getIntent().getStringExtra("startTime");
            end = getIntent().getStringExtra("endTime");
            type = getIntent().getStringExtra("assessmentType");

            assessmentNameDetail.setText(getIntent().getStringExtra("assessmentName"));
            assessmentDateDetails.setText(getIntent().getStringExtra("assessmentDate"));
            assessmentStartDetails.setText(getIntent().getStringExtra("startTime"));
            assessmentEndDetails.setText(getIntent().getStringExtra("endTime"));
            assessmentTypeDetails.setText(getIntent().getStringExtra("assessmentType"));
        }else {
            Toast.makeText(this, "No intent data received", Toast.LENGTH_SHORT).show();
        }
    }
}
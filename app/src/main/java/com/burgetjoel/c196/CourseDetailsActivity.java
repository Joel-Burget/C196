package com.burgetjoel.c196;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {
    DatabaseHelper assessmentDB;

    TextView courseNameText, courseStartDateText, courseEndDateText, courseStatusText, instructorNameText, instructorPhoneText, instructorEmailText, notes_text;
    String termIDString, courseIDString, courseName, courseStart, courseEnd, courseStatus, instructorName, instructorPhone, instructorEmail, notesString;
    AssessmentAdapter customAdaptor;
    RecyclerView recyclerView;
    ArrayList<String> courseID, assessmentID, assessmentName, assessmentDate, assessmentStart, assessmentEnd, assessmentType, notesArray;
    FloatingActionButton addAssessmentButton;
    Button shareButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        assessmentDB = new DatabaseHelper(CourseDetailsActivity.this);
        assessmentID = new ArrayList<>();
        courseID = new ArrayList<>();
        assessmentName = new ArrayList<>();
        assessmentDate = new ArrayList<>();
        assessmentStart = new ArrayList<>();
        assessmentEnd = new ArrayList<>();
        assessmentType = new ArrayList<>();
        notesArray = new ArrayList<>();

        courseNameText = findViewById(R.id.detail_course_name_text);
        courseStartDateText = findViewById(R.id.detail_course_start_date_text);
        courseEndDateText = findViewById(R.id.detail_course_end_date_text);
        courseStatusText = findViewById(R.id.detail_course_status_text);
        instructorNameText = findViewById(R.id.detail_instructor_name_text);
        instructorPhoneText = findViewById(R.id.detail_instructor_phone_text);
        instructorEmailText = findViewById(R.id.detail_instructor_email_text);
        notes_text = findViewById(R.id.detail_notes_text);
        shareButton = findViewById(R.id.share_button);

        recyclerView = findViewById(R.id.examRecycler);
        customAdaptor = new AssessmentAdapter(this, this, courseID, assessmentID, assessmentName, assessmentDate, assessmentStart, assessmentEnd, assessmentType);
        recyclerView.setAdapter(customAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getAndSetIntentData();
        Log.i("Course ID", "Course ID is: " + courseIDString);
        storeAssessmentsInArrays(Integer.parseInt(courseIDString));

        addAssessmentButton = findViewById(R.id.addAssessmentButton);
        addAssessmentButton.setOnClickListener(v ->{
            Intent intent = new Intent(this, AddAssessmentActivity.class);
            intent.putExtra("courseID", courseIDString);
            startActivityForResult(intent, 1);
        });

        addAssessmentButton = findViewById(R.id.update_course_button);
        addAssessmentButton.setOnClickListener(v ->{
            Intent intent = new Intent(this, EditCourseActivity.class);
            intent.putExtra("courseID", courseIDString);
            intent.putExtra("termID", termIDString);
            intent.putExtra("courseName", courseName);
            intent.putExtra("assessmentDate", assessmentDate);
            intent.putExtra("courseStartDate", courseStart);
            intent.putExtra("courseEndDate", courseEnd);
            intent.putExtra("courseStatus", courseStatus);
            intent.putExtra("instructorName", instructorName);
            intent.putExtra("instructorPhone", instructorPhone);
            intent.putExtra("instructorEmail", instructorEmail);
            intent.putExtra("notes", notesString);

            activityResultLauncher.launch(intent);
        });

        shareButton.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_TEXT, notesString);

            try{
                startActivity(Intent.createChooser(intent, "Share via"));
            }catch(Exception e){
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void getAndSetIntentData() {
        if(getIntent().hasExtra("courseID") && getIntent().hasExtra("courseName") && getIntent().hasExtra("courseStartDate") && getIntent().hasExtra("courseEndDate")
        && getIntent().hasExtra("courseStatus") && getIntent().hasExtra("instructorName") && getIntent().hasExtra("instructorPhone") && getIntent().hasExtra("instructorEmail")
        && getIntent().hasExtra("notes")){
            courseIDString = getIntent().getStringExtra("courseID");
            courseName = getIntent().getStringExtra("courseName");
            courseStart = getIntent().getStringExtra("courseStartDate");
            courseEnd = getIntent().getStringExtra("courseEndDate");
            courseStatus = getIntent().getStringExtra("courseStatus");
            instructorName = getIntent().getStringExtra("instructorName");
            instructorEmail = getIntent().getStringExtra("instructorEmail");
            instructorPhone = getIntent().getStringExtra("instructorPhone");
            notesString = getIntent().getStringExtra("notes");

            courseNameText.setText(courseName);
            courseStartDateText.setText(courseStart);
            courseEndDateText.setText(courseEnd);
            courseStatusText.setText(courseStatus);
            instructorNameText.setText(instructorName);
            instructorPhoneText.setText(instructorPhone);
            instructorEmailText.setText(instructorEmail);
            notes_text.setText(notesString);
        }else{
            Toast.makeText(this, "No intent data", Toast.LENGTH_SHORT).show();
        }
    }
    void storeAssessmentsInArrays(int ID){
        Cursor cursor = assessmentDB.readAssessments(ID);

        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Assessment Data", Toast.LENGTH_SHORT).show();
        }else{
            cursor.moveToFirst();
            do {
                assessmentID.add(cursor.getString(0));
                courseID.add(cursor.getString(1));
                assessmentName.add(cursor.getString(2));
                assessmentDate.add(cursor.getString(3));
                assessmentStart.add(cursor.getString(4));
                assessmentEnd.add(cursor.getString(5));
                assessmentType.add(cursor.getString(6));
            } while (cursor.moveToNext());
        }
    }
}
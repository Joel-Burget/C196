package com.burgetjoel.c196;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TermDetailsActivity extends AppCompatActivity {
    DatabaseHelper courseDB;
    TextView termNameText, termStartText, termEndText;
    String name, startDate, endDate, termID;
    FloatingActionButton addCourseButton;
    CourseAdaptor customAdaptor;
    RecyclerView recyclerView;
    ArrayList<String> courseID, courseName, courseStart, courseEnd, courseStatus, courseInstructor, courseInstructorPhone, courseInstructorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        //Set Actionbar
        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle(name);
        }
        courseDB = new DatabaseHelper(TermDetailsActivity.this);

        courseID = new ArrayList<>();
        courseName = new ArrayList<>();
        courseEnd = new ArrayList<>();
        courseStart = new ArrayList<>();
        courseStatus = new ArrayList<>();
        courseInstructor = new ArrayList<>();
        courseInstructorPhone = new ArrayList<>();
        courseInstructorEmail = new ArrayList<>();


        termNameText = findViewById(R.id.detail_term_name_text);
        termStartText = findViewById(R.id.detail_term_start_date_text);
        termEndText = findViewById(R.id.detail_term_end_date_text);

        recyclerView = findViewById(R.id.courseRecycler);

        customAdaptor = new CourseAdaptor(this, TermDetailsActivity.this, courseID, courseName, courseStart, courseEnd, courseStatus,
                courseInstructor, courseInstructorPhone, courseInstructorEmail);
        recyclerView.setAdapter(customAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(TermDetailsActivity.this));

        addCourseButton = findViewById(R.id.addCourseButton);
        // getting data from intent
        getAndSetIntentData();
        storeCoursesInArrays(Integer.parseInt(termID));

         addCourseButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(TermDetailsActivity.this, AddCourseActivity.class);
                 intent.putExtra("termID", termID);
                 startActivity(intent);
             }
         });
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("term_id") && getIntent().hasExtra("Term Name") && getIntent().hasExtra("Term Start Date") && getIntent().hasExtra("Term End Date")){
            //getting intent data
            termID = getIntent().getStringExtra("term_id");
            name = getIntent().getStringExtra("Term Name");
            startDate = getIntent().getStringExtra("Term Start Date");
            endDate = getIntent().getStringExtra("Term End Date");

            //setting intent data
            termNameText.setText(name);
            termStartText.setText(startDate);
            termEndText.setText(endDate);

        }else{
            Toast.makeText(this, "No data from intent", Toast.LENGTH_SHORT).show();
        }
    }

    void storeCoursesInArrays(int ID){
        Cursor cursor = courseDB.readCourses(ID);

        if (cursor.getCount() == 0){
            Toast.makeText(this, "No Course Data", Toast.LENGTH_SHORT).show();
        }else{
            cursor.moveToFirst();
            courseID.add(cursor.getString(1));
            courseName.add(cursor.getString(2));
            courseStart.add(cursor.getString(3));
            courseEnd.add(cursor.getString(4));
            courseStatus.add(cursor.getString(5));
            courseInstructor.add(cursor.getString(6));
            courseInstructorPhone.add(cursor.getString(7));
            courseInstructorEmail.add(cursor.getString(8));
            while(cursor.moveToNext()){
                courseID.add(cursor.getString(1));
                courseName.add(cursor.getString(2));
                courseStart.add(cursor.getString(3));
                courseEnd.add(cursor.getString(4));
                courseStatus.add(cursor.getString(5));
                courseInstructor.add(cursor.getString(6));
                courseInstructorPhone.add(cursor.getString(7));
                courseInstructorEmail.add(cursor.getString(8));
            }
        }
    }
}
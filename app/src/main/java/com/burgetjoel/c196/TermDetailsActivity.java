package com.burgetjoel.c196;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class TermDetailsActivity extends AppCompatActivity {
    DatabaseHelper courseDB;
    TextView termNameText, termStartText, termEndText;
    String name, startDate, endDate, termID;
    FloatingActionButton addCourseButton;
    FloatingActionButton editCourseButton;
    CourseAdaptor customAdaptor;
    RecyclerView recyclerView;
    ArrayList<String> termIDArray, courseID, courseName, courseStart, courseEnd, courseStatus, courseInstructor, courseInstructorPhone, courseInstructorEmail, notesArray;

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

        termIDArray = new ArrayList<>();
        courseID = new ArrayList<>();
        courseName = new ArrayList<>();
        courseEnd = new ArrayList<>();
        courseStart = new ArrayList<>();
        courseStatus = new ArrayList<>();
        courseInstructor = new ArrayList<>();
        courseInstructorPhone = new ArrayList<>();
        courseInstructorEmail = new ArrayList<>();
        notesArray = new ArrayList<>();


        termNameText = findViewById(R.id.detail_course_name_text);
        termStartText = findViewById(R.id.detail_course_start_date_text);
        termEndText = findViewById(R.id.detail_course_status_text);

        recyclerView = findViewById(R.id.courseRecycler);

        customAdaptor = new CourseAdaptor(this, TermDetailsActivity.this, courseID, courseName, courseStart, courseEnd, courseStatus,
                courseInstructor, courseInstructorPhone, courseInstructorEmail, notesArray);
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

         editCourseButton = findViewById(R.id.edit_course_button);
         editCourseButton.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(TermDetailsActivity.this, UpdateTermActivity.class);
                 intent.putExtra("term_id", termID);
                 intent.putExtra("termName", name);
                 intent.putExtra("termStartDate", startDate);
                 intent.putExtra("termEndDate", endDate);
                 startActivityForResult(intent, 1);
             }
         });
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            finish();
            recreate();
        }
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
            do {
                termIDArray.add(cursor.getString(1));
                courseID.add(cursor.getString(0));
                Log.i("Course ID from storeCoursesInArrays: ", "storeCoursesInArrays: " + cursor.getString(1));
                courseName.add(cursor.getString(2));
                courseStart.add(cursor.getString(3));
                courseEnd.add(cursor.getString(4));
                courseStatus.add(cursor.getString(5));
                courseInstructor.add(cursor.getString(6));
                courseInstructorPhone.add(cursor.getString(7));
                courseInstructorEmail.add(cursor.getString(8));
                notesArray.add(cursor.getString(9));
            } while (cursor.moveToNext());
        }
    }
}
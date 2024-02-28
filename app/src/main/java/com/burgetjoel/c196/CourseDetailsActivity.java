package com.burgetjoel.c196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {

    TextView courseNameText, course_start_date_text, course_end_date_text, instructor_text, instructor_phone_text, instructor_email_text;

    CourseAdaptor customAdaptor;
    ArrayList<String> courseID, courseName, courseStart, courseEnd, instructorName, instructorEmail, instructorPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

    }
}
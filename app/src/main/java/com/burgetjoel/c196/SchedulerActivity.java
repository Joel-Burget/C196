package com.burgetjoel.c196;

import android.database.Cursor;
import android.os.Bundle;
import android.sax.Element;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SchedulerActivity extends AppCompatActivity {
    ArrayList courseList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);

        
        DatabaseHelper myDb = new DatabaseHelper(this);
        Cursor cursor = myDb.readAllCourses();

        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                String name = cursor.getString(2);
                String startDate = cursor.getString(3);
                String endDate = cursor.getString(4);
                createCourse course = new createCourse(name, startDate, endDate);
                courseList.add(course);
            }
        }
        for (Object course : courseList) {
            Log.i("Course Names: ", ((createCourse) course).getName());
            Log.i("Course Start Dates: ", ((createCourse) course).getStartDate());
            Log.i("Course End Dates: ", ((createCourse) course).getEndDate());
        }



    }

}

 class createCourse {
    String name;
    String startDate;
    String endDate;

    public createCourse(String name, String startDate, String endDate){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public String getName(){
        return name;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getEndDate(){
        return endDate;
    }
}
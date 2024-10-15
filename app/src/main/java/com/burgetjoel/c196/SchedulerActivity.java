package com.burgetjoel.c196;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SchedulerActivity extends AppCompatActivity {
    ArrayList<SchedulerCourse> courseList = new ArrayList<>();
    String name, startDateString, endDateString;
    String color;
    String colorToMap;
    TextView currentMonth;
    List<Date> datesToHighlight = new ArrayList<>();
    static HashMap<Date, String> colorMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        Toolbar myToolbar =  findViewById(R.id.scheduler_toolbar);
        setSupportActionBar(myToolbar);

        RecyclerView recyclerView = findViewById(R.id.calendar_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 7);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseHelper myDb = new DatabaseHelper(this);
        Cursor cursor = myDb.readAllScheduler();
        Log.i("Cursor Count", String.valueOf(cursor.getCount()));
        if(cursor.getCount() > 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            cursor.moveToFirst();
            name = cursor.getString(1);
            startDateString = cursor.getString(2);
            endDateString = cursor.getString(3);
            color = cursor.getString(4);
        }else{
            while(cursor.moveToNext()){
                name = cursor.getString(1);
                startDateString = cursor.getString(2);
                endDateString = cursor.getString(3);
                color = cursor.getString(4);
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CalendarAdapter adapter = new CalendarAdapter(getWeeksForMonth(Calendar.getInstance().get(Calendar.MONTH), 2024));
        recyclerView.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month = month_date.format(calendar.getTime());
        currentMonth = findViewById(R.id.current_month);
        currentMonth.setText(month);

        cursor = myDb.readAllScheduler();
        cursor.moveToFirst();
        name = cursor.getString(1);
        startDateString = cursor.getString(2);
        endDateString = cursor.getString(3);
        color = cursor.getString(4);
        courseList.add(new SchedulerCourse(name, startDateString, endDateString, color));
        do {
            name = cursor.getString(1);
            startDateString = cursor.getString(2);
            endDateString = cursor.getString(3);
            color = cursor.getString(4);
            courseList.add(new SchedulerCourse(name, startDateString, endDateString, color));
        } while (cursor.moveToNext());

        Log.i("Course List Size:", String.valueOf(courseList.size()));

        if(!courseList.isEmpty()){
            for(int i = 0; i < courseList.size(); i++){
                startDateString = courseList.get(i).getStartDate();
                endDateString = courseList.get(i).getEndDate();
                colorToMap = courseList.get(i).getColor();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = sdf.parse(startDateString);
                    endDate = sdf.parse(endDateString);

                    // Get the list of dates in range
                    List<Date> datesInRange = getDatesBetween(startDate, endDate);

                    // Add these dates to the datesToHighlight list
                    addDatesToHighlight(datesInRange);

                    // Iterate over all days of the current month
                    for (int j = 1; j <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); j++) {
                        calendar.set(Calendar.DAY_OF_MONTH, j);
                        Date currentDate = clearTime(calendar.getTime());

                        // Iterate over all days in the month (from DateUtils)
                        for (Date day : DateUtils.getAllDaysOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))) {
                            // Check if the day is within startDate and endDate
                            if (isBetweenDates(day, startDate, endDate)) {
                                // Add to colorMap only if it is within the range
                                colorMap.put(clearTime(day), colorToMap);
                            }
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }
        }
//        for (Date day : datesToHighlight) {
//            Log.i(String.valueOf(day), colorToMap);
//            colorMap.put(clearTime(day), colorToMap);
//        }
        if(!datesToHighlight.isEmpty()){
            adapter.updateDatesToHighlight(datesToHighlight);
//            adapter.updateColorMap(colorMap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.scheduler_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.scheduler_add) {
            // Perform action when button is clicked
            startActivity(new Intent(this, SchedulerAddActivity.class));
            return true;
        }

        if (id == R.id.scheduler_edit) {
            // Perform action when button is clicked
            startActivity(new Intent(this, SchedulerEditActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<List<Date>> getWeeksForMonth(int month, int year) {
        List<List<Date>> weeks = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        // Set calendar to the first day of the current month
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Get the day of the week the month starts on
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Move the calendar to the start of the week (Sunday)
        calendar.add(Calendar.DAY_OF_MONTH, - (firstDayOfWeek - Calendar.SUNDAY));

        // Iterate until the next month
        while (calendar.get(Calendar.MONTH) != month + 1 || calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            List<Date> week = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                week.add(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            weeks.add(week);
        }

        return weeks;
    }

    private boolean isBetweenDates(Date targetDate, Date startDate, Date endDate) {
        //converting to milliseconds for comparison
        long targetMillis = targetDate.getTime();
        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();

        //checking if target date is between start and end dates
        return targetMillis >= startMillis && targetMillis <= endMillis;
    }

    private void addDatesToHighlight(List<Date> dates){
        for(Date date : dates){
            if(!datesToHighlight.contains(date)){
                datesToHighlight.add(date);
            }
        }
    }

    private List<Date> getDatesBetween(Date startDate, Date endDate){
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while(!calendar.getTime().after(endDate)){
            dateList.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        if(!dateList.contains(endDate)){
            dateList.add(endDate);
        }
        return dateList;
    }

    public static Date clearTime(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static HashMap<Date, String> getColorMap(){
        return colorMap;
    }
}


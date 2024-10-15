package com.burgetjoel.c196;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
    static List<String> getDates(String startDate, String endDate){
        List<String> dates = new ArrayList<>();

        // Convert the start and end dates to Date objects
        try{
            Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);

            while(calendar.getTime().before(end)){
                dates.add(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                calendar.add(Calendar.DATE, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return dates;
    }

    public static List<Date> getAllDaysOfMonth(int year, int month){
        List<Date> daysInMonth = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for(int day = 1; day <= maxDay; day++){
            calendar.set(Calendar.DAY_OF_MONTH, day);
            daysInMonth.add(calendar.getTime());
        }

        return daysInMonth;
    }
}

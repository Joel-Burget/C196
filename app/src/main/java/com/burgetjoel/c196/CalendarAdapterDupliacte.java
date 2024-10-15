package com.burgetjoel.c196;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapterDupliacte extends RecyclerView.Adapter<CalendarAdapterDupliacte.WeekViewHolder> {
    private List<List<Date>> weeks;
    public ArrayList<TextView> days = new ArrayList<>();
    public CalendarAdapterDupliacte(List<List<Date>> weeks) {
        this.weeks = weeks;

        String sunday, monday, tuesday, wednesday, thursday, friday, saturday;
        sunday = "sunday";
        monday = "monday";
        tuesday = "tuesday";
        wednesday = "wednesday";
        thursday = "thursday";
        friday = "friday";
        saturday = "saturday";

        Log.i("Days were created: ", days.size() + "");
    }
    static Calendar calendar = Calendar.getInstance();

    public CalendarAdapterDupliacte(){

    }


    @NonNull
    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_week, parent, false);
        return new WeekViewHolder(view, days);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {
        List<Date> week = weeks.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());

        // Set text for each day in the week
        holder.sunday.setText(sdf.format(week.get(0)));
        holder.monday.setText(sdf.format(week.get(1)));
        holder.tuesday.setText(sdf.format(week.get(2)));
        holder.wednesday.setText(sdf.format(week.get(3)));
        holder.thursday.setText(sdf.format(week.get(4)));
        holder.friday.setText(sdf.format(week.get(5)));
        holder.saturday.setText(sdf.format(week.get(6)));
        
//        days.add(holder.sunday);
//        days.add(holder.monday);
//        days.add(holder.tuesday);
//        days.add(holder.wednesday);
//        days.add(holder.thursday);
//        days.add(holder.friday);
//        days.add(holder.saturday);
//        Log.i("Days were added: ", days.size() + "");
    }

    @Override
    public int getItemCount() {
        return weeks.size();
    }

    public static class WeekViewHolder extends RecyclerView.ViewHolder {
        TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday;
        private static ArrayList<TextView> days = new ArrayList<>();
        public WeekViewHolder(@NonNull View itemView, ArrayList<TextView> days) {
            super(itemView);
            WeekViewHolder.days = days;

            sunday = itemView.findViewById(R.id.sunday_day_text);
            monday = itemView.findViewById(R.id.monday_day_text);
            tuesday = itemView.findViewById(R.id.tuesday_day_text);
            wednesday = itemView.findViewById(R.id.wednesday_day_text);
            thursday = itemView.findViewById(R.id.thursday_day_text);
            friday = itemView.findViewById(R.id.friday_day_text);
            saturday = itemView.findViewById(R.id.saturday_day_text);

            days.add(sunday);
            days.add(monday);
            days.add(tuesday);
            days.add(wednesday);
            days.add(thursday);
            days.add(friday);
            days.add(saturday);

            Log.i("CalendarAdaptor line 103 ", WeekViewHolder.days.size() + "");
        }

        public void highlightDates(List<Date> datesToHighlight, int color) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());

            Log.i("Size in Adapter class 109", datesToHighlight.size() + "");
            Log.i("highlightDates line 110", days.size() + "");
            for (Date date : datesToHighlight) {
                Log.i("Attempting to highlight", sdf.format(date));
                for (int i = 0; i < days.size(); i++) {
                    Log.i("Day", days.get(i).getText().toString());
                    int dayInt = Integer.parseInt(days.get(i).getText().toString());
                    calendar.set(Calendar.DAY_OF_MONTH, dayInt);
                    Log.i("Checking if date is between", "True/False");
                    Log.i("Date", sdf.format(date));
                    Log.i("Target Date", days.get(i).getText().toString());
                    // Check if the TextView date matches one of the dates to highlight
                    if (sdf.format(date).equals(days.get(i).getText().toString())) {
                        Log.i("Day was added", "Added successfully");
                        days.get(i).setBackgroundColor(color);
                    } else {
                        Log.i("Color Change", "was not attempted: ");
                    }
                }
            }
        }

//        public ArrayList<TextView> getDays(){
//            return this.days;
//        }
     }

}
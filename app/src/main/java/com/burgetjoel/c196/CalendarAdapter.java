package com.burgetjoel.c196;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.WeekViewHolder> {
    private final List<List<Date>> weeks;
    private List<Date> datesToHighlight = new ArrayList<>();
//    private HashMap<Date, String> colorMap = new HashMap<>();

    public CalendarAdapter(List<List<Date>> weeks) {
        this.weeks = weeks;
    }

    @NonNull
    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_week, parent, false);
        return new WeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {
        List<Date> week = weeks.get(position);
        holder.bindWeek(week, SchedulerActivity.getColorMap());
    }

    public void updateDatesToHighlight(List<Date> datesToHighlight){
        this.datesToHighlight = datesToHighlight;
    }

//    public void updateColorMap(HashMap<Date, String> map){
//        colorMap.putAll(map);
//    }

    @Override
    public int getItemCount() {
        return weeks.size();
    }

    public static class WeekViewHolder extends RecyclerView.ViewHolder {
        TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday;

        public WeekViewHolder(@NonNull View itemView) {
            super(itemView);

            sunday = itemView.findViewById(R.id.sunday_day_text);
            monday = itemView.findViewById(R.id.monday_day_text);
            tuesday = itemView.findViewById(R.id.tuesday_day_text);
            wednesday = itemView.findViewById(R.id.wednesday_day_text);
            thursday = itemView.findViewById(R.id.thursday_day_text);
            friday = itemView.findViewById(R.id.friday_day_text);
            saturday = itemView.findViewById(R.id.saturday_day_text);

        }

        public void bindWeek(List<Date> week, HashMap<Date, String> colorMap){
            SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());

            sunday.setText(sdf.format(week.get(0)));
            monday.setText(sdf.format(week.get(1)));
            tuesday.setText(sdf.format(week.get(2)));
            wednesday.setText(sdf.format(week.get(3)));
            thursday.setText(sdf.format(week.get(4)));
            friday.setText(sdf.format(week.get(5)));
            saturday.setText(sdf.format(week.get(6)));

            List<TextView> dayViews = List.of(sunday, monday, tuesday, wednesday, thursday, friday, saturday);
            for (int i = 0; i < week.size(); i++) {
                Date dayDate = SchedulerActivity.clearTime(week.get(i));

                // Check if date exists in datesToHighlight
                if (colorMap.containsKey(dayDate)) {
                    // Check if the date has a color in colorMap and it's not null
                    String colorString = colorMap.get(dayDate);
                    if (colorString != null) {
                        try {
                            dayViews.get(i).setBackgroundColor(Color.parseColor(colorString));

                        } catch (IllegalArgumentException e) {
                            Log.e("ColorParseError", "Invalid color string: " + colorString);
                        }
                    } else {
                        Log.w("ColorWarning", "No color found for date: " + dayDate);
                    }
                } else {
                    // Set background color to transparent if the date is not in datesToHighlight
                    dayViews.get(i).setBackgroundColor(Color.TRANSPARENT);
                }
            }

        }
     }
}
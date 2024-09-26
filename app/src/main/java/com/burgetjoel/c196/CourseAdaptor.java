package com.burgetjoel.c196;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CourseAdaptor extends RecyclerView.Adapter<CourseAdaptor.CourseViewHolder> {
    private final Context context;
    Activity activity;
    private final ArrayList<String> courseID;
    private final ArrayList<String> courseName;
    private final ArrayList<String> courseStart;
    private final ArrayList<String> courseEnd;
    private final ArrayList<String> courseStatus;
    private final ArrayList<String> instructorName;
    private final ArrayList<String> instructorPhone;
    private final ArrayList<String> instructorEmail;
    private final ArrayList<String> notesArray;

    Animation translate_anim;

    public  CourseAdaptor(Context context, Activity activity, ArrayList courseID, ArrayList courseName, ArrayList courseStart, ArrayList courseEnd, ArrayList courseStatus,
                          ArrayList instructorName, ArrayList instructorPhone, ArrayList instructorEmail, ArrayList notesArray){
        this.context = context;
        this.activity = activity;
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseStatus = courseStatus;
        this.instructorName = instructorName;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
        this.notesArray = notesArray;

    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.course_row, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.course_name_text.setText(String.valueOf(courseName.get(position)));
        holder.course_start_text.setText(String.valueOf(courseStart.get(position)));
        holder.course_end_text.setText(String.valueOf(courseEnd.get(position)));
        holder.courseStatusText.setText(String.valueOf((courseStatus.get(position))));
        holder.instructor_name_text.setText(String.valueOf(instructorName.get(position)));
        holder.instructor_phone_text.setText(String.valueOf(instructorPhone.get(position)));
        holder.instructor_email_text.setText(String.valueOf(instructorEmail.get(position)));
        holder.notes_text.setText(String.valueOf(notesArray.get(position)));

        holder.courseLayout.setOnClickListener(v ->{
            Intent intent = new Intent(context, CourseDetailsActivity.class);
            intent.putExtra("courseID", String.valueOf(courseID.get(position)));
            intent.putExtra("courseName", String.valueOf(courseName.get(position)));
            intent.putExtra("courseStartDate", String.valueOf(courseStart.get(position)));
            intent.putExtra("courseEndDate", String.valueOf(courseEnd.get(position)));
            intent.putExtra("courseStatus", String.valueOf(courseStatus.get(position)));
            intent.putExtra("instructorName", String.valueOf(instructorName.get(position)));
            intent.putExtra("instructorEmail", String.valueOf(instructorEmail.get(position)));
            intent.putExtra("instructorPhone", String.valueOf(instructorPhone.get(position)));
            intent.putExtra("notes", String.valueOf(notesArray.get(position)));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return courseID.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder{
        TextView course_name_text, course_start_text, course_end_text, courseStatusText, instructor_name_text, instructor_phone_text, instructor_email_text, notes_text;
        LinearLayout courseLayout;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            course_name_text = itemView.findViewById(R.id.course_name_text);
            course_start_text = itemView.findViewById(R.id.assessment_date_text);
            course_end_text = itemView.findViewById(R.id.assessment_start_time_text);
            courseStatusText = itemView.findViewById(R.id.course_status_text);
            instructor_name_text = itemView.findViewById(R.id.instructor_name_text);
            instructor_phone_text = itemView.findViewById(R.id.instructor_phone_text);
            instructor_email_text = itemView.findViewById(R.id.instructor_email_text);
            notes_text = itemView.findViewById(R.id.notes_text);
            courseLayout = itemView.findViewById(R.id.courselayout);
            //adding animation
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_amin);
            courseLayout.setAnimation(translate_anim);
        }
    }


}

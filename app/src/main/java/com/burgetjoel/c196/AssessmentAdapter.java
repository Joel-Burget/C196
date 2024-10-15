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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    private final Context context;
    Activity activity;
    private final ArrayList<String> courseIDArray;
    private final ArrayList<String> assessmentIDArray;
    private final ArrayList<String> assessmentNameArray;
    private final ArrayList<String> assessmentDateArray;
    private final ArrayList<String> assessmentStartTimeArray;
    private final ArrayList<String> assessmentEndTimeArray;
    private final ArrayList<String> assessmentTypeArray;

    Animation translate_anim;

    AssessmentAdapter(Activity activity, Context context, ArrayList<String> courseIDArray, ArrayList<String> assessmentIDArray, ArrayList<String> assessmentNameArray,
                      ArrayList<String> assessmentDateArray , ArrayList<String> assessmentStartTimeArray, ArrayList<String> assessmentEndTimeArray, ArrayList<String> assessmentTypeArray){
        this.activity = activity;
        this.context = context;
        this.courseIDArray = courseIDArray;
        this.assessmentIDArray = assessmentIDArray;
        this.assessmentNameArray = assessmentNameArray;
        this.assessmentDateArray = assessmentDateArray;
        this.assessmentStartTimeArray = assessmentStartTimeArray;
        this.assessmentEndTimeArray = assessmentEndTimeArray;
        this.assessmentTypeArray = assessmentTypeArray;
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.assessment_row, parent, false);
        return new AssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        holder.assessmentNameText.setText(String.valueOf(assessmentNameArray.get(position)));
        holder.assessmentDateText.setText(String.valueOf(assessmentDateArray.get(position)));
        holder.assessmentTypeText.setText(String.valueOf(assessmentTypeArray.get(position)));
        holder.assessmentStartTimeText.setText(String.valueOf(assessmentStartTimeArray.get(position)));
        holder.assessmentEndTimeText.setText(String.valueOf(assessmentEndTimeArray.get(position)));

        holder.assessmentLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, AssessmentDetailsActivity.class);
            intent.putExtra("courseID", String.valueOf(courseIDArray.get(position)));
            intent.putExtra("assessmentID", String.valueOf(assessmentIDArray.get(position)));
            intent.putExtra("assessmentName", String.valueOf(assessmentNameArray.get(position)));
            intent.putExtra("assessmentDate", String.valueOf(assessmentDateArray.get(position)));
            intent.putExtra("startTime", String.valueOf(assessmentStartTimeArray.get(position)));
            intent.putExtra("endTime", String.valueOf(assessmentEndTimeArray.get(position)));
            intent.putExtra("assessmentType", String.valueOf(assessmentTypeArray.get(position)));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        if(assessmentIDArray != null){
            return assessmentIDArray.size();
        }else{
            return 0;
        }
    }

    public class AssessmentViewHolder extends RecyclerView.ViewHolder{
        TextView assessmentNameText, assessmentDateText, assessmentStartTimeText, assessmentEndTimeText, assessmentTypeText;
        LinearLayout assessmentLayout;
        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            assessmentNameText = itemView.findViewById(R.id.assessment_name_text);
            assessmentDateText = itemView.findViewById(R.id.assessment_date_text);
            assessmentStartTimeText = itemView.findViewById(R.id.assessment_start_time_text);
            assessmentEndTimeText = itemView.findViewById(R.id.assessment_end_time_text);
            assessmentTypeText = itemView.findViewById(R.id.assessment_type_text);
            assessmentLayout = itemView.findViewById(R.id.assessment_layout);

            //adding animation
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_amin);
            assessmentLayout.setAnimation(translate_anim);

        }
    }
}



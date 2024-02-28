package com.burgetjoel.c196;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList term_id, term_name, term_start_date, term_end_date;
    Animation translate_anim;

    TermAdapter(Activity activity, Context context, ArrayList term_id, ArrayList term_name, ArrayList term_start_date, ArrayList term_end_date){
        this.activity = activity;
        this.context = context;
        this.term_id = term_id;
        this.term_name = term_name;
        this.term_start_date = term_start_date;
        this.term_end_date = term_end_date;
    }
    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.term_row, parent, false);
        return new TermViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, final int position) {

        holder.term_name_text.setText(String.valueOf(term_name.get(position)));
        holder.term_start_date_text.setText(String.valueOf(term_start_date.get(position)));
        holder.term_end_date_text.setText(String.valueOf(term_end_date.get(position)));

        holder.termLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, TermDetailsActivity.class);
            intent.putExtra("term_id", String.valueOf(term_id.get(position)));
            intent.putExtra("Term Name", String.valueOf(term_name.get(position)));
            intent.putExtra("Term Start Date", String.valueOf(term_start_date.get(position)));
            intent.putExtra("Term End Date", String.valueOf(term_end_date.get(position)));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return term_id.size();
    }

    public class TermViewHolder extends RecyclerView.ViewHolder{
        TextView  term_name_text, term_start_date_text, term_end_date_text;
        LinearLayout termLayout;
        public TermViewHolder(@NonNull View itemView) {
            super(itemView);
            term_name_text = itemView.findViewById(R.id.term_name_text);
            term_start_date_text = itemView.findViewById(R.id.course_start_date_text);
            term_end_date_text = itemView.findViewById(R.id.course_end_date_text);
            termLayout = itemView.findViewById(R.id.termlayout);
            //adding animation
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_amin);
            termLayout.setAnimation(translate_anim);
        }
    }
}

package com.s2paa.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.s2paa.Model.Exam_Schedule;
import com.s2paa.R;
import com.s2paa.Utils.TTextView;

import java.util.ArrayList;

/**
 * Created by admin on 7/31/2017.
 */

public class ExamAdapters extends  RecyclerView.Adapter<ExamAdapters.ViewHolder> {


    public ArrayList<Exam_Schedule> list;
    Context context;
    public ExamAdapters(Context context, ArrayList<Exam_Schedule> list){
        this.context=context;
        this.list=list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TTextView examdate,subjects,classroom,from_time,to_time;

        public ViewHolder(View view) {
            super(view);
            examdate = (TTextView) view.findViewById(R.id.examdate);
            subjects = (TTextView) view.findViewById(R.id.subjects);
            classroom = (TTextView) view.findViewById(R.id.classroom);
            from_time = (TTextView) view.findViewById(R.id.from_time);
            to_time = (TTextView) view.findViewById(R.id.to_time);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_schedule_items, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Exam_Schedule exam_schedule = list.get(position);
        holder.examdate.setText(exam_schedule.exam_date);
        holder.to_time.setText(exam_schedule.to_time);
        holder.from_time.setText(exam_schedule.from_time);
        holder.classroom.setText("Classroom "+exam_schedule.classes);
        holder.subjects.setText(exam_schedule.subject);
//        holder.cardCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onCardClickListener!=null){
//                    onCardClickListener.onClickCategory(category);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

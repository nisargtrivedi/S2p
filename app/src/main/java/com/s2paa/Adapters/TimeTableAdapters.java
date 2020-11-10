package com.s2paa.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.s2paa.Model.TimeTable_Model;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.DateUtils;
import com.s2paa.Utils.TTextView;

import java.util.ArrayList;

/**
 * Created by admin on 7/31/2017.
 */

public class TimeTableAdapters extends  RecyclerView.Adapter<TimeTableAdapters.ViewHolder> {

    private String[] colors={"#3498db","#2ecc71","#9b59b6","#f1c40f","#1abc9c","#2980b9","#8e44ad","#e41c1c","#752ecc","#2ecc53"};
    public ArrayList<TimeTable_Model> list;
    Context context;
    AppPreferences preferences;
    public TimeTableAdapters(Context context,ArrayList<TimeTable_Model> list){
        this.context=context;
        this.list=list;
        preferences=new AppPreferences(context);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TTextView teacher_name,time,subject_name;

        public ViewHolder(View view) {
            super(view);
            teacher_name = (TTextView) view.findViewById(R.id.teacher);
            time = (TTextView) view.findViewById(R.id.time);
            subject_name = (TTextView) view.findViewById(R.id.subjects);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timetable_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TimeTable_Model timeTable_model = list.get(position);


   // for(int i=0;i<colors.length;i++){

   // }

        if(preferences.getString("login_type").toString().equalsIgnoreCase("student")) {
            holder.teacher_name.setText(timeTable_model.teacher_name);
            holder.subject_name.setText(timeTable_model.subject_name);
            holder.time.setText(DateUtils.getTimeFromTime(timeTable_model.start_time)+" - " +DateUtils.getTimeFromTime(timeTable_model.end_time));
            holder.time.setBackgroundColor(Color.parseColor(colors[position]));
        }
        else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent")) {
            holder.teacher_name.setText(timeTable_model.teacher_name);
            holder.subject_name.setText(timeTable_model.subject_name);
            holder.time.setText(DateUtils.getTimeFromTime(timeTable_model.start_time)+" - " +DateUtils.getTimeFromTime(timeTable_model.end_time));
            holder.time.setBackgroundColor(Color.parseColor(colors[position]));
        }
        else if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher")) {
            holder.teacher_name.setText(timeTable_model.clas + "-" + timeTable_model.section);
            holder.subject_name.setText(timeTable_model.subject_name);
            holder.time.setText(DateUtils.getTimeFromTime(timeTable_model.start_time) + " - " + DateUtils.getTimeFromTime(timeTable_model.end_time));
            holder.time.setBackgroundColor(Color.parseColor(colors[position]));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

package com.s2paa.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.s2paa.Listener.AttendanceListener;
import com.s2paa.Model.AttendanceModel;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.TTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by admin on 7/31/2017.
 */

public class AttendanceAdapters extends  RecyclerView.Adapter<AttendanceAdapters.ViewHolder> {


    public ArrayList<AttendanceModel> list;

    AttendanceListener attendanceListener;
    Context context;
    AppPreferences preferences;



    public AttendanceAdapters(Context context, ArrayList<AttendanceModel> list){
        this.context=context;
        this.list=list;
        preferences=new AppPreferences(context);
        //object=new AttendanceModel();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TTextView standard,division,student_name,present_absent,check_box_present_absent;
        RelativeLayout main_item;
        de.hdodenhof.circleimageview.CircleImageView img;
        public ViewHolder(View view) {
            super(view);
            img = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.img);
            student_name = (TTextView) view.findViewById(R.id.student_name);
            present_absent = (TTextView) view.findViewById(R.id.present_absent);
            check_box_present_absent = (TTextView) view.findViewById(R.id.check_box_present_absent);
            main_item=(RelativeLayout) view.findViewById(R.id.main_item);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.attendance_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AttendanceModel attendanceModel = list.get(position);
        //attendanceModel.present_absent="Absent";
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        attendanceModel.date=fDate.toString().trim();
        holder.student_name.setText(attendanceModel.student_name);
        holder.present_absent.setText(attendanceModel.present_absent);
        Glide.with(context).load(attendanceModel.image_url).into(holder.img);
        holder.main_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.check_box_present_absent.isSelected()==false) {
                   holder.check_box_present_absent.setSelected(true);
                    holder.present_absent.setText("Present");
                    holder.check_box_present_absent.setBackgroundResource(R.drawable.checkbox_selected);
                    attendanceModel.present_absent="Present";
                        composeJSONfromSQLite();

//                    object.student_id=attendanceModel.student_id;
//                    object.present_absent="Present";
//                    object.date=attendanceModel.date;
                }
                else {
                    holder.check_box_present_absent.setSelected(false);
                    holder.present_absent.setText("Absent");
                    holder.check_box_present_absent.setBackgroundResource(R.drawable.checkbox);
                    attendanceModel.present_absent="Absent";
                    composeJSONfromSQLite();
                }

            }
        });
        if(attendanceModel.present_absent.toString().equalsIgnoreCase("Present")){
            holder.check_box_present_absent.setSelected(true);
            holder.present_absent.setText("Present");
            holder.check_box_present_absent.setBackgroundResource(R.drawable.checkbox_selected);
        }else {
            holder.check_box_present_absent.setSelected(false);
            holder.present_absent.setText("Absent");
            holder.check_box_present_absent.setBackgroundResource(R.drawable.checkbox);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnAttendanceClickListener (AttendanceListener attendanceClickListener) {
        attendanceListener=attendanceClickListener;
    };

    public  String  composeJSONfromSQLite()
    {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();

        for(int i=0;i<list.size();i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("std_id", list.get(i).student_id);
            map.put("status", list.get(i).present_absent);
            map.put("dates", list.get(i).date);
            wordList.add(map);
        }
         Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

}

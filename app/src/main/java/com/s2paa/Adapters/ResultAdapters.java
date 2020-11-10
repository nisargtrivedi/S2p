package com.s2paa.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.s2paa.Listener.AttendanceListener;
import com.s2paa.Model.Result;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.EEditText;
import com.s2paa.Utils.TTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class ResultAdapters extends  RecyclerView.Adapter<ResultAdapters.ViewHolder> {


    public List<Result> list;

    AttendanceListener attendanceListener;
    Context context;
    AppPreferences preferences;



    public ResultAdapters(Context context, List<Result> list){
        this.context=context;
        this.list=list;
        preferences=new AppPreferences(context);
        //object=new AttendanceModel();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TTextView standard,division,student_name,present_absent;
        public EEditText check_box_present_absent;
        RelativeLayout main_item;
        de.hdodenhof.circleimageview.CircleImageView img;
        public ViewHolder(View view) {
            super(view);
            img = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.img);
            student_name = (TTextView) view.findViewById(R.id.student_name);
            present_absent = (TTextView) view.findViewById(R.id.present_absent);
            check_box_present_absent = (EEditText) view.findViewById(R.id.check_box_present_absent);
            main_item=(RelativeLayout) view.findViewById(R.id.main_item);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.result_add_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Result attendanceModel = list.get(position);
        //attendanceModel.present_absent="Absent";
        Date cDate = new Date();
//        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
//        attendanceModel.date=fDate.toString().trim();
        holder.student_name.setText(attendanceModel.student_name);
        holder.present_absent.setText("Roll No. "+attendanceModel.student_rollno);
        holder.check_box_present_absent.setText(attendanceModel.mark_obtained);
        Glide.with(context).load(attendanceModel.student_img).into(holder.img);
        holder.main_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attendanceModel.mark_obtained=holder.check_box_present_absent.getText().toString();
                composeJSONfromSQLite();
            }
        });
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
            map.put("mark_id", list.get(i).mark_id+"");
//            map.put("class_id", list.get(i).class_id+"");
//            map.put("subject_id", list.get(i).exam_subject_id+"");
//            map.put("exam_id", list.get(i).exam_id+"");
            map.put("mark_obtained", list.get(i).mark_obtained+"");
            wordList.add(map);
        }
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        Log.i("RESULT ADD",gson.toJson(wordList));
        return gson.toJson(wordList);
    }

}

package com.s2paa.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.s2paa.Activities.Result_;
import com.s2paa.Model.ExamList;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.TTextView;

import java.util.ArrayList;

/**
 * Created by admin on 7/31/2017.
 */

public class ResultExamListAdapter extends  RecyclerView.Adapter<ResultExamListAdapter.ViewHolder> {


    public ArrayList<ExamList> list;
    Context context;
    AppPreferences preferences;
    public ResultExamListAdapter(Context context, ArrayList<ExamList> list){
        this.context=context;
        this.list=list;
        preferences=new AppPreferences(context);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TTextView date,reason,detail;
        RelativeLayout data;

        public ViewHolder(View view) {
            super(view);
            date = (TTextView) view.findViewById(R.id.date);
            reason = (TTextView) view.findViewById(R.id.reason);
            detail = (TTextView) view.findViewById(R.id.detail);
            data=(RelativeLayout)view.findViewById(R.id.data);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.leave_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ExamList annoucement = list.get(position);
        holder.date.setText(annoucement.date);
        holder.reason.setText(annoucement.name);
        holder.detail.setText("Total Marks "+annoucement.total_marks);
        holder.data.setOnClickListener(new View.OnClickListener() {
            @Override

                    public void onClick(View v) {
//                Toast.makeText(context,annoucement.class_id,Toast.LENGTH_LONG).show();
                        //context.startActivity(new Intent(context,ExamSchedule_.class));
                if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher")) {
                    Intent intent = new Intent(context, Result_.class);
                    intent.putExtra("EXAM", annoucement);
                    context.startActivity(intent);
                }
                else if(preferences.getString("login_type").toString().equalsIgnoreCase("student") || preferences.getString("login_type").toString().equalsIgnoreCase("parent") ) {
                    Intent intent = new Intent(context, Result_.class);
                    intent.putExtra("EXAM", annoucement);
                    context.startActivity(intent);
                }

                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

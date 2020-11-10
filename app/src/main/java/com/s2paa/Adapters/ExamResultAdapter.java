package com.s2paa.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.s2paa.Model.ExamResult;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.TTextView;

import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class ExamResultAdapter extends  RecyclerView.Adapter<ExamResultAdapter.ViewHolder> {


    public List<ExamResult> list;
    Context context;
    AppPreferences preferences;
    public ExamResultAdapter(Context context, List<ExamResult> list){
        this.context=context;
        this.list=list;
        preferences=new AppPreferences(context);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TTextView subject,marks,total_marks,grade;
        public de.hdodenhof.circleimageview.CircleImageView img;
        LinearLayout data;

        public ViewHolder(View view) {
            super(view);
            subject = (TTextView) view.findViewById(R.id.subject);
            marks = (TTextView) view.findViewById(R.id.marks);
            total_marks = (TTextView) view.findViewById(R.id.total_marks);
            grade=(TTextView) view.findViewById(R.id.grade);
            data=(LinearLayout)view.findViewById(R.id.data);
            img=(de.hdodenhof.circleimageview.CircleImageView)view.findViewById(R.id.img);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.result_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ExamResult annoucement = list.get(position);

        if(preferences.getString("login_type").equalsIgnoreCase("student") || preferences.getString("login_type").equalsIgnoreCase("parent") ){
            holder.marks.setText(annoucement.mark_obtained+"");
            holder.total_marks.setText(annoucement.mark_total+"");
            holder.subject.setText(annoucement.subject);
            holder.grade.setText(annoucement.grade);
            holder.img.setVisibility(View.GONE);
            //Glide.with(context).load(annoucement.student_img).into(holder.img);
        }
        else if(preferences.getString("login_type").equalsIgnoreCase("teacher")) {

            holder.marks.setText(annoucement.mark_obtained + "");
            holder.total_marks.setText(annoucement.mark_total + "");
            holder.subject.setText(annoucement.student_name + "\nRoll No." + annoucement.student_roll +"\n "+annoucement.subject);
            holder.grade.setText(annoucement.grade);
            Glide.with(context).load(annoucement.student_img).into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

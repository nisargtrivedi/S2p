package com.s2paa.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.s2paa.Model.Remark;
import com.s2paa.R;
import com.s2paa.Utils.TTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 7/31/2017.
 */

public class RemarkAdapters extends  RecyclerView.Adapter<RemarkAdapters.ViewHolder> {


    public ArrayList<Remark> list;
    Context context;
    Date date=new Date();
    public RemarkAdapters(Context context, ArrayList<Remark> list){
        this.context=context;
        this.list=list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TTextView class_name_with_section,details,annoucement_date,subject_charactor;

        public ViewHolder(View view) {
            super(view);
            class_name_with_section = (TTextView) view.findViewById(R.id.class_name_with_section);
            details = (TTextView) view.findViewById(R.id.details);
            annoucement_date=(TTextView)view.findViewById(R.id.annoucement_date);
            subject_charactor=(TTextView)view.findViewById(R.id.subject_charactor);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.announcement_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Remark annoucement = list.get(position);
        holder.class_name_with_section.setText(annoucement.title);
        holder.details.setText(annoucement.details);

        holder.subject_charactor.setVisibility(View.GONE);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1=format.parse(annoucement.api_date);
            if(date.getDate()==date1.getDate()) {
                    holder.annoucement_date.setVisibility(View.GONE);
            }else{
                holder.annoucement_date.setVisibility(View.VISIBLE);
                holder.annoucement_date.setText(annoucement.api_date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            date=format.parse(annoucement.api_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Log.i("date",dateString);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

package com.s2paa.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.s2paa.Activities.HomeWork;
import com.s2paa.Listener.HomeWorkClick;
import com.s2paa.Listener.RecycleViewItemClick;
import com.s2paa.Model.Home_Work;
import com.s2paa.R;
import com.s2paa.Utils.TTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;

import static java.util.Collections.sort;

/**
 * Created by admin on 8/1/2017.
 */

public class HomeWorkAdapter extends RecyclerView.Adapter<HomeWorkAdapter.ViewHolder> {


    public ArrayList<Home_Work> list;
    HomeWorkClick recycleViewItemClick;
    Context context;
    public HomeWorkAdapter(Context context,ArrayList<Home_Work> list){
        this.context=context;
        this.list=list;
        //
        // list2=list;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TTextView date,subject_character,subject_name,subject_details,homework_date;
        LinearLayout main;

        public ViewHolder(View view) {
            super(view);
         //   date = (TTextView) view.findViewById(R.id.homework_date);
            subject_character = (TTextView) view.findViewById(R.id.subject_charactor);
            subject_name = (TTextView) view.findViewById(R.id.subjectname);
            subject_details = (TTextView) view.findViewById(R.id.subject_details);
            homework_date=(TTextView)view.findViewById(R.id.homework_date);
            main=(LinearLayout)view.findViewById(R.id.main);
        }
    }
    @Override
    public HomeWorkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_work_item, parent, false);

        return new HomeWorkAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeWorkAdapter.ViewHolder holder, int position) {
        final Home_Work home_work = list.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = home_work.date;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);
        holder.homework_date.setText(todayString+"");

        try {
            if(home_work.date.after(sdf.parse(holder.homework_date.getText().toString()))){
                Log.i("date",home_work.date .after(sdf.parse(holder.homework_date.getText().toString()))+"");
                    holder.homework_date.setVisibility(View.GONE);
            }else{
                holder.homework_date.setVisibility(View.VISIBLE);
            }
            holder.subject_name.setText(home_work.subject_name);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.subject_details.setText(home_work.subject_details);
        if(!TextUtils.isEmpty(home_work.subject_name))
        {
            home_work.subject_character=home_work.subject_name.toUpperCase().charAt(0)+"";
        }
        holder.subject_character.setText(home_work.subject_character);
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recycleViewItemClick!=null){
                    recycleViewItemClick.onClick(home_work);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnCatClickListener (HomeWorkClick classActivity) {
        recycleViewItemClick=classActivity;
    };

}

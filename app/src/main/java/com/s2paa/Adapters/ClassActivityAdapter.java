package com.s2paa.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.s2paa.Listener.RecycleViewItemClick;
import com.s2paa.Model.ClassActivity;
import com.s2paa.R;
import com.s2paa.Utils.DateUtils;
import com.s2paa.Utils.TTextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 7/31/2017.
 */

public class ClassActivityAdapter extends  RecyclerView.Adapter<ClassActivityAdapter.ViewHolder> {


    public ArrayList<ClassActivity> list;
    Context context;
    RecycleViewItemClick recycleViewItemClick;
    Date date=new Date();
    int count=0;
    public ClassActivityAdapter(Context context, ArrayList<ClassActivity> list){
        this.context=context;
        this.list=list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TTextView date,class_name,details;
        public LinearLayout main_view;
        public ViewHolder(View view) {
            super(view);

            date = (TTextView) view.findViewById(R.id.date);
            class_name = (TTextView) view.findViewById(R.id.class_name);
            details = (TTextView) view.findViewById(R.id.details);
            main_view=(LinearLayout)view.findViewById(R.id.main_view);
           // main_view.setOnClickListener(readMoreListener);

        }


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.class_activity_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final ClassActivity classActivity = list.get(position);
        holder.class_name.setText(classActivity.title);
        holder.details.setText(classActivity.details);
        Log.i("DATE_______",date+"");
       // Log.i("DATE2_______",classActivity.create_at+"");

        if((date.getDate()==(classActivity.create_at.getDate())))
        {
            holder.date.setVisibility(View.GONE);
         //   holder.date.setText(DateUtils.GETLOCALE_DATE(classActivity.create_at));
        }else
        {
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(DateUtils.GETLOCALE_DATE(classActivity.create_at));
        }

        date= classActivity.create_at;
        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recycleViewItemClick!=null){
                    recycleViewItemClick.onitemClick(classActivity);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnCatClickListener (RecycleViewItemClick classActivity) {
           recycleViewItemClick=classActivity;
    };






}

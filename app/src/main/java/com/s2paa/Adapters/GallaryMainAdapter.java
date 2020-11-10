package com.s2paa.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.s2paa.Activities.GallaryMainPage_;
import com.s2paa.Activities.Gallery_;
import com.s2paa.Model.GallaryObjects;
import com.s2paa.Model.Home_Work;
import com.s2paa.R;
import com.s2paa.Utils.TTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 8/1/2017.
 */

public class GallaryMainAdapter extends RecyclerView.Adapter<GallaryMainAdapter.ViewHolder> {


    public List<GallaryObjects> list;

    Context context;
    public GallaryMainAdapter(Context context, List<GallaryObjects> list){
        this.context=context;
        this.list=list;
        //
        // list2=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TTextView date,event_name;
        public de.hdodenhof.circleimageview.CircleImageView imageView;
        RelativeLayout item_data;

        public ViewHolder(View view) {
            super(view);
         //   date = (TTextView) view.findViewById(R.id.homework_date);
            imageView = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.event_main_img);
            event_name = (TTextView) view.findViewById(R.id.event_name);
            date = (TTextView) view.findViewById(R.id.ddate);
            item_data=(RelativeLayout)view.findViewById(R.id.item_data);
        }
    }
    @Override
    public GallaryMainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_main_item, parent, false);

        return new GallaryMainAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GallaryMainAdapter.ViewHolder holder, int position) {
        final GallaryObjects home_work = list.get(position);

        holder.date.setText(home_work.gallery_date);
        holder.event_name.setText(home_work.title);
        Glide.with(context).load(home_work.main_img).into(holder.imageView);
        holder.item_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Gallery_.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

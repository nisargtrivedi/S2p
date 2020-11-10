package com.s2paa.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.s2paa.Listener.RecycleViewItemClick;
import com.s2paa.Model.subUserDetails;
import com.s2paa.R;
import com.s2paa.Utils.TTextView;

import java.util.ArrayList;

/**
 * Created by admin on 7/31/2017.
 */

public class ParentAdapter extends  RecyclerView.Adapter<ParentAdapter.ViewHolder> {


    public ArrayList<subUserDetails> list;
    Context context;
    RecycleViewItemClick recycleViewItemClick;

    public ParentAdapter(Context context, ArrayList<subUserDetails> list){
        this.context=context;
        this.list=list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public de.hdodenhof.circleimageview.CircleImageView img;
        public TTextView phno,name,email;
        public LinearLayout main_view;
        public ViewHolder(View view) {
            super(view);

            phno = (TTextView) view.findViewById(R.id.phno);
            name = (TTextView) view.findViewById(R.id.name);
            email = (TTextView) view.findViewById(R.id.email);
            img = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.img);
            //main_view=(LinearLayout)view.findViewById(R.id.main_view);
           // main_view.setOnClickListener(readMoreListener);

        }


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.parents_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final subUserDetails classActivity = list.get(position);

        holder.name.setText(classActivity.name);
        holder.email.setText(classActivity.email);
        holder.phno.setText(classActivity.phone);
        Glide.with(context).load(classActivity.profile_img).into(holder.img);
//        holder.main_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(recycleViewItemClick!=null){
//                    recycleViewItemClick.onitemClick(classActivity);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnCatClickListener (RecycleViewItemClick classActivity) {
           recycleViewItemClick=classActivity;
    };






}

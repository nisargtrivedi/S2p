package com.s2paa.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.s2paa.Listener.AttendanceListener;
import com.s2paa.Model.Comment;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.TTextView;

import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class CommentAdapters extends  RecyclerView.Adapter<CommentAdapters.ViewHolder> {


    public List<Comment> list;

    AttendanceListener attendanceListener;
    Context context;
    AppPreferences preferences;



    public CommentAdapters(Context context, List<Comment> list){
        this.context=context;
        this.list=list;
        preferences=new AppPreferences(context);
        //object=new AttendanceModel();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TTextView user_name,date,comment;
        RelativeLayout main_item;
        de.hdodenhof.circleimageview.CircleImageView img;
        public ViewHolder(View view) {
            super(view);
            img = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.img);
            user_name = (TTextView) view.findViewById(R.id.user_name);
            date = (TTextView) view.findViewById(R.id.date);
            comment = (TTextView) view.findViewById(R.id.comment);
            main_item=(RelativeLayout) view.findViewById(R.id.main_item);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.comment_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Comment attendanceModel = list.get(position);
        //attendanceModel.present_absent="Absent";

        holder.user_name.setText(attendanceModel.user_name);
        holder.date.setText(attendanceModel.create_at);
        holder.comment.setText(attendanceModel.comments);
        Glide.with(context).load(attendanceModel.img_url).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }





}

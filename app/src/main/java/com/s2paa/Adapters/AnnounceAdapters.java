package com.s2paa.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.s2paa.Model.Annoucement;
import com.s2paa.R;
import com.s2paa.Utils.TTextView;

import java.util.ArrayList;

/**
 * Created by admin on 7/31/2017.
 */

public class AnnounceAdapters extends  RecyclerView.Adapter<AnnounceAdapters.ViewHolder> {


    public ArrayList<Annoucement> list;
    Context context;
    public AnnounceAdapters(Context context, ArrayList<Annoucement> list){
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
        final Annoucement annoucement = list.get(position);
        holder.class_name_with_section.setText(annoucement.class_name_with_section);
        holder.details.setText(annoucement.details);
       // String msg= annoucement.create_timestamp;
        holder.annoucement_date.setText(annoucement.create_timestamp);
        String[] data=annoucement.class_name_with_section.split("-");
        holder.subject_charactor.setText(data[1]+"");

//        Log.i("date",dateString);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

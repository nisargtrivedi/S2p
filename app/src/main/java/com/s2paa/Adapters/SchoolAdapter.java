package com.s2paa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.s2paa.Listener.AttendanceListener;
import com.s2paa.Listener.SchoolListener;
import com.s2paa.Model.School;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.TTextView;

import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class SchoolAdapter extends BaseAdapter{


    public List<School> list;

    AttendanceListener attendanceListener;
    Context context;
    AppPreferences preferences;
    LayoutInflater inflater;
    SchoolListener schoolListener;



    public SchoolAdapter(Context context, List<School> list){
        this.context=context;
        this.list=list;
        preferences=new AppPreferences(context);
        inflater=LayoutInflater.from(context);
        //object=new AttendanceModel();
    }

    @Override
    public int getCount() {
      return   list.size();
    }

    @Override
    public School getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){

            convertView=inflater.inflate(R.layout.school_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
                holder=(ViewHolder)convertView.getTag();
        }
        final School attendanceModel = list.get(position);
        holder.school_item.setText(attendanceModel.school_name);
//        holder.data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SMS.ENDPOINT=attendanceModel.school_url;
//                context.startActivity(new Intent(context, Login_.class));
//            }
//        });
        return convertView;
    }

    public class ViewHolder   {

        public TTextView school_item;
        public LinearLayout data;
        public ViewHolder(View view) {
           // super(view);
            school_item = (TTextView) view.findViewById(R.id.school_item);
            data=(LinearLayout)view.findViewById(R.id.data);

        }
    }
//
//
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(context)
//                .inflate(R.layout.school_item, parent, false);
//
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
//        final School attendanceModel = list.get(position);
//        holder.school_item.setText(attendanceModel.school_name);
//        holder.data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SMS.ENDPOINT=attendanceModel.school_url;
//                context.startActivity(new Intent(context, Login_.class));
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }

}

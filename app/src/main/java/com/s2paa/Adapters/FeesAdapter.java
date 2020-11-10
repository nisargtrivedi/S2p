package com.s2paa.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.s2paa.Model.Fees;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/31/2017.
 */

public class FeesAdapter extends  RecyclerView.Adapter<FeesAdapter.ViewHolder> {


    public List<Fees> list=new ArrayList<>();

    Context context;
    AppPreferences preferences;



    public FeesAdapter(Context context, List<Fees> list){
        this.context=context;
        this.list=list;
        preferences=new AppPreferences(context);
        //object=new AttendanceModel();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView term,type,amount,fees_status;


        public ViewHolder(View view) {
            super(view);

            term = (TextView) view.findViewById(R.id.term);
            type = (TextView) view.findViewById(R.id.type);
            amount = (TextView) view.findViewById(R.id.amount);
            fees_status = (TextView) view.findViewById(R.id.fees_status);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.fees_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Fees attendanceModel = list.get(position);
        //attendanceModel.present_absent="Absent";
        holder.term.setText(attendanceModel.title);

       // holder.amount.setText(attendanceModel.amount);
        holder.fees_status.setText(attendanceModel.fees_status);

//        if(!attendanceModel.type.equalsIgnoreCase("cash")){
//            holder.type.setText(attendanceModel.type + "  Cheque No:-" + attendanceModel.cheque_number);
//        }else{
//            holder.type.setText(attendanceModel.type);
//        }
        if(attendanceModel.fees_status.equalsIgnoreCase("paid")){
            holder.fees_status.setTextColor(Color.GREEN);
        }else{
            holder.fees_status.setTextColor(Color.RED);

        }
//        if(attendanceModel.present_absent.toString().equalsIgnoreCase("Present")){
//            holder.check_box_present_absent.setSelected(true);
//            holder.present_absent.setText("Present");
//            holder.check_box_present_absent.setBackgroundResource(R.drawable.checkbox_selected);
//        }else {
//            holder.check_box_present_absent.setSelected(false);
//            holder.present_absent.setText("Absent");
//            holder.check_box_present_absent.setBackgroundResource(R.drawable.checkbox);
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

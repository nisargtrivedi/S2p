package com.s2paa.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.s2paa.Application.SMS;
import com.s2paa.Listener.HomeWorkClick;
import com.s2paa.Listener.LeaveClick;
import com.s2paa.Model.Leave;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.Messages;
import com.s2paa.Utils.TTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 7/31/2017.
 */

public class LeaveAdapter extends  RecyclerView.Adapter<LeaveAdapter.ViewHolder> {


    public ArrayList<Leave> list;
    Context context;
    AppPreferences preferences;
    LeaveClick recycleViewItemClick;
    public LeaveAdapter(Context context, ArrayList<Leave> list){
        this.context=context;
        preferences=new AppPreferences(context);
        this.list=list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TTextView date,reason,detail;
        public Button accept;
        public RelativeLayout data;

        public ViewHolder(View view) {
            super(view);
            date = (TTextView) view.findViewById(R.id.date);
            reason = (TTextView) view.findViewById(R.id.reason);
            detail = (TTextView) view.findViewById(R.id.detail);
            accept=(Button)view.findViewById(R.id.accept);
            data=(RelativeLayout)view.findViewById(R.id.data);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.leave_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Leave annoucement = list.get(position);
        holder.date.setText(annoucement.leave_date);
        holder.reason.setText(annoucement.title);
        holder.detail.setText(annoucement.details);
        if(preferences.getString("login_type").equalsIgnoreCase("teacher")){
            if (annoucement.leave_status.equals("1"))
                holder.accept.setVisibility(View.GONE);
                else
                holder.accept.setVisibility(View.VISIBLE);
        }else{
            holder.accept.setVisibility(View.GONE);
        }

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(preferences.getString("login_type").equalsIgnoreCase("teacher"))
                    LoadData(preferences.getString("teacher_id"),"","1",annoucement.id);
            }
        });
        holder.data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recycleViewItemClick!=null){
                    recycleViewItemClick.onitemClick(annoucement);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private void LoadData(final String teacher_id, final String comment, final String status, final String leave_id){
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, SMS.LEAVE_APPROVE_BY_TEACHER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------",response);
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(context,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(context,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("comments",comment);
                params.put("status",status+"");
                params.put("teacher_id",teacher_id+"");
                params.put("leave_application",leave_id+"");
                // params.put("student_id","20");
//                params.put("user_type",preferences.getString("login_type"));
              //  Log.i("student_id--------",preferences.getString("student_id"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void setOnCatClickListener (LeaveClick classActivity) {
        recycleViewItemClick=classActivity;
    };

}

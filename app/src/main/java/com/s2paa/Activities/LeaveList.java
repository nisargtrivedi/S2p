package com.s2paa.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.s2paa.Adapters.LeaveAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Listener.HomeWorkClick;
import com.s2paa.Listener.LeaveClick;
import com.s2paa.Model.Home_Work;
import com.s2paa.Model.Leave;
import com.s2paa.R;
import com.s2paa.Utils.Messages;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/1/2017.
 */

@EActivity(R.layout.leave_list)
public class LeaveList extends BaseActivity {

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    RecyclerView rv_leave;

    LeaveAdapter adapter;
    ArrayList<Leave> list=new ArrayList<>();
    Leave leave;

    @ViewById
    TTextView total;

    @AfterViews
    protected void init()
    {
        load();
     leave=new Leave();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Leave List");
        LoadData();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    public String getURL()
    {
        if(preferences.getString("login_type").toString().equalsIgnoreCase("student"))
          return   SMS.STUDENT_LEAVE;
        else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
           return SMS.PARENTS_LEAVE;
        else if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher"))
            return SMS.PARENTS_LEAVE;
        else
            return SMS.PARENTS_LEAVE;
    }

    private void LoadData(){
        final ProgressDialog pDialog = new ProgressDialog(LeaveList.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, getURL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------",response);
                            Leave user=new Leave();
                            list=user.getLeave(object.getJSONArray("Response"));
                            adapter=new LeaveAdapter(LeaveList.this,list);
                            rv_leave.setAdapter(adapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_leave.setLayoutManager(layoutManager);
                            rv_leave.setItemAnimator(new DefaultItemAnimator());
                            total.setText("Total "+ list.size()+" Leaves");

                            adapter.setOnCatClickListener(new LeaveClick() {
                                @Override
                                public void onitemClick(Leave homeworkActivity) {
                                    Intent intent=new Intent(LeaveList.this,DetailsActivity_.class);
                                    intent.putExtra("Leave", homeworkActivity);
                                    startActivity(intent);
                                }
                            });
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(LeaveList.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(LeaveList.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("student_id",preferences.getString("student_id"));
               // params.put("student_id","20");
//                params.put("user_type",preferences.getString("login_type"));
                Log.i("student_id--------",preferences.getString("student_id"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveList.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


}

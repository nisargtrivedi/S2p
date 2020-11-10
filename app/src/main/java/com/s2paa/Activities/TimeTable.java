package com.s2paa.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobandme.ada.Entity;
import com.s2paa.Adapters.TimeTableAdapters;
import com.s2paa.Application.SMS;
import com.s2paa.Model.TimeTable_Model;
import com.s2paa.R;
import com.s2paa.Utils.AppLogger;
import com.s2paa.Utils.ExceptionsHelper;
import com.s2paa.Utils.Messages;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 7/31/2017.
 */


@EActivity(R.layout.timetable)
public class TimeTable extends BaseActivity {

    @ViewById
    TabLayout tablayout;

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    RecyclerView RVtimetable;

    TimeTableAdapters adapters;
    TimeTable_Model model= new TimeTable_Model();

    ArrayList<TimeTable_Model> list= new ArrayList<>();;

    @AfterViews
    protected void init()
    {
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
         txtActionTitle.setText("Time Table");
        tablayout.addTab(tablayout.newTab().setText(getString(R.string.TIME_TABLE_MONDAY)));
        tablayout.addTab(tablayout.newTab().setText(getString(R.string.TIME_TABLE_TUESDAY)));
        tablayout.addTab(tablayout.newTab().setText(getString(R.string.TIME_TABLE_WEDNESDAY)));
        tablayout.addTab(tablayout.newTab().setText(getString(R.string.TIME_TABLE_THURSDAY)));
        tablayout.addTab(tablayout.newTab().setText(getString(R.string.TIME_TABLE_FRIDAY)));
        tablayout.addTab(tablayout.newTab().setText(getString(R.string.TIME_TABLE_SATURDAY)));


        LoadData();
        filter("day=?",new String[]{"Monday"},null);
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               // viewPager.setCurrentItem(tab.getPosition());
               // Toast.makeText(TimeTable.this,""+tab.getPosition(),Toast.LENGTH_LONG).show();
                showData(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }


            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void showData(int position){
        switch (position){
            case 0 :
               filter("day=?",new String[]{"Monday"},null);
                break;
            case 1 :
                //LoadData("tuesday");
                filter("day=?",new String[]{"Tuesday"},null);
                break;
            case 2 :
                filter("day=?",new String[]{"Wednesday"},null);
                break;
            case 3 :
                filter("day=?",new String[]{"Thursday"},null);
                break;
            case 4 :
                filter("day=?",new String[]{"Friday"},null);
                break;
            case 5 :
                filter("day=?",new String[]{"Saturday"},null);
                break;

            default:Toast.makeText(TimeTable.this,"Error to select",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void LoadData(){
        final ProgressDialog pDialog = new ProgressDialog(TimeTable.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS.TIME_TABLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        //list.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------",response);
                            TimeTable_Model user=new TimeTable_Model();
                            list=user.ReponseData(object.getJSONArray("Response"));
                            //list=user.ReponseData(object.getJSONObject("response").getJSONArray("tuesday"));
                            //list=user.ReponseData(object.getJSONObject("response").getJSONArray("wednesday"));
                            AppLogger.info("List--------"+list);
                            addDataToDB();
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(TimeTable.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(TimeTable.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(preferences.getString("login_type").toString().equalsIgnoreCase("student")) {
                    params.put("class_id", preferences.getString("class"));
                    params.put("section_id", preferences.getString("section"));
                }
                else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent")) {
                    params.put("class_id", preferences.getString("class_id"));
                    params.put("section_id", preferences.getString("section_id"));
                }
                else if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher")){
                    params.put("teacher_id", preferences.getString("teacher_id"));
                }
                Log.i("PARAMETER",params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimeTable.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void addDataToDB() {

        try {

            if (list.size() > 0) {

                dataContext.timeTable_modelObjectSet.fill();
                for (int i = 0; i < dataContext.timeTable_modelObjectSet.size(); i++)
                    dataContext.timeTable_modelObjectSet.get(i).setStatus(Entity.STATUS_DELETED);

                dataContext.timeTable_modelObjectSet.save();

                dataContext.timeTable_modelObjectSet.addAll(list);
                dataContext.timeTable_modelObjectSet.save();
            }
        }
        catch (Exception ex) {
            ExceptionsHelper.manage(TimeTable.this,ex);
        }

    }
    private void filter(String filter,String[] arg,String oby){

        try {
            dataContext.timeTable_modelObjectSet.fill(filter,arg,oby);
            list.clear();
            list.addAll(dataContext.timeTable_modelObjectSet);
            adapters.notifyDataSetChanged();
        }catch (Exception e){

        }
        adapters=new TimeTableAdapters(TimeTable.this,dataContext.timeTable_modelObjectSet);
        RVtimetable.setAdapter(adapters);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RVtimetable.setLayoutManager(layoutManager);
        RVtimetable.setItemAnimator(new DefaultItemAnimator());
    }

}

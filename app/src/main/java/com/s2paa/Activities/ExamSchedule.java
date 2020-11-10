package com.s2paa.Activities;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Adapters.ExamAdapters;
import com.s2paa.Model.ExamList;
import com.s2paa.Model.Exam_Schedule;
import com.s2paa.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by admin on 8/1/2017.
 */

@EActivity(R.layout.exam_schedule)
public class ExamSchedule extends BaseActivity {

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    RecyclerView examschedule;

    Exam_Schedule exam_schedule;
    ArrayList<Exam_Schedule> list;
    ExamAdapters adapters;

    ExamList examList;
    @AfterViews
    protected void init(){
        list=new ArrayList<>();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        load();
        txtActionTitle.setText("Exam Schedule");
        examList=new ExamList();
        examList= (ExamList) getIntent().getSerializableExtra("Class");
        try {
            //dataContext.examScheduleObjectSet.fill();
            if(preferences.getString("login_type").equalsIgnoreCase("parent")){
                dataContext.examScheduleObjectSet.fill("exam_id=? and class_id=?",new String[]{examList.exam_id,"1"},null);
            }
            else
            dataContext.examScheduleObjectSet.fill("exam_id=? and class_id=?",new String[]{examList.exam_id,preferences.getString("class").toString()},null);
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        Log.i("GET EXAM SCHEDULE--",dataContext.examScheduleObjectSet+"");
        adapters=new ExamAdapters(ExamSchedule.this,dataContext.examScheduleObjectSet);
        examschedule.setAdapter(adapters);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        examschedule.setLayoutManager(layoutManager);
        examschedule.setItemAnimator(new DefaultItemAnimator());
        //loadData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }
//    private void loadData()
//    {
//        final ProgressDialog pDialog = new ProgressDialog(ExamSchedule.this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS.EXAM_SCHEDULE,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.hide();
//                        pDialog.dismiss();
//                        list.clear();
//                        try {
//                            JSONObject object = new JSONObject(response);
//                            Log.i("Exam Response--------",response);
//                            Exam_Schedule user=new Exam_Schedule();
//                            list=user.ReponseData(object.getJSONArray("Response"));
//                      //      AppLogger.info("List--------"+list);
//                            adapters=new ExamAdapters(ExamSchedule.this,list);
//                            examschedule.setAdapter(adapters);
//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                            examschedule.setLayoutManager(layoutManager);
//                            examschedule.setItemAnimator(new DefaultItemAnimator());
//                        } catch (JSONException e) {
//                            pDialog.hide();
//                            pDialog.dismiss();
//                            if(!e.getMessage().trim().equals(""))
//                                Messages.ShowMessage(ExamSchedule.this,e.getMessage().toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
//                        pDialog.dismiss();
//                        Messages.ShowMessage(ExamSchedule.this,"problem on network connection");
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//
//                params.put("exam_id",examList.exam_id);
////                if(!preferences.getString("class").equalsIgnoreCase(""))
////                    params.put("class_id",preferences.getString("class"));
////                else
////                    params.put("class_id","");
//                Log.i("PARAMETER---",params.toString());
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(ExamSchedule.this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                7000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//
//    }
//        exam_schedule=new Exam_Schedule();
//        exam_schedule.exam_date="Monday,24 July 2017";
//        exam_schedule.subject="Maths";
//        exam_schedule.classes="Classes:10";
//        exam_schedule.from_time="7:00 AM";
//        exam_schedule.to_time="8:00 AM";
//        list.add(exam_schedule);
//        list.add(exam_schedule);
//        list.add(exam_schedule);
//        list.add(exam_schedule);
//        list.add(exam_schedule);
//        list.add(exam_schedule);
//        list.add(exam_schedule);




}

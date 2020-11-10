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
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Adapters.ExamListAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Model.ExamList;
import com.s2paa.Model.Exam_Schedule;
import com.s2paa.R;
import com.s2paa.Utils.Messages;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/31/2017.
 */

@EActivity(R.layout.exam)
public class ShowExamList extends BaseActivity {

    @ViewById
    RecyclerView rv_exam;
    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    TTextView total;


    ArrayList<ExamList> list=new ArrayList<>();
    ArrayList<ExamList> examListArrayList = new ArrayList<>();
    ArrayList<Exam_Schedule> exam_scheduleArrayList=new ArrayList<>();

    ExamListAdapter adapter;
    @AfterViews
    public void init(){
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Exam List");
        getExams();
        try {
            dataContext.examListObjectSet.fill();
            adapter=new ExamListAdapter(ShowExamList.this,dataContext.examListObjectSet);
            rv_exam.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            rv_exam.setLayoutManager(layoutManager);
            rv_exam.setItemAnimator(new DefaultItemAnimator());

            total.setText("Total Exam "+dataContext.examListObjectSet.size()+"");
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void getExams() {
        final ProgressDialog pDialog = new ProgressDialog(ShowExamList.this);
        pDialog.setMessage("Loading Details...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                SMS.EXAM_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        //list.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            ExamList standard = new ExamList();
                            examListArrayList = standard.ReponseData(object.getJSONArray("Response"));
                            exam_scheduleArrayList=standard.list;
                            Log.i("EXAM_SCHEDULE_SIZE---",exam_scheduleArrayList.size()+"");
                            addData();

                            Log.i("Response-----", object.getJSONArray("Response") + "" + examListArrayList.size());
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(ShowExamList.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(ShowExamList.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ShowExamList.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Background
    public void addData() {
        try {
            dataContext.examListObjectSet.fill();
            dataContext.examScheduleObjectSet.fill();
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }

        if (examListArrayList.size() > 0) {

            try {
                dataContext.examListObjectSet.fill();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < dataContext.examListObjectSet.size(); i++)
                dataContext.examListObjectSet.get(i).setStatus(Entity.STATUS_DELETED);

            try {
                dataContext.examListObjectSet.save();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            dataContext.examListObjectSet.addAll(examListArrayList);
            try {
                dataContext.examListObjectSet.save();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
        }
        if(exam_scheduleArrayList.size()>0){
            try {
                dataContext.examScheduleObjectSet.fill();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < dataContext.examScheduleObjectSet.size(); i++)
                dataContext.examScheduleObjectSet.get(i).setStatus(Entity.STATUS_DELETED);

            try {
                dataContext.examScheduleObjectSet.save();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            dataContext.examScheduleObjectSet.addAll(exam_scheduleArrayList);
            try {
                dataContext.examScheduleObjectSet.save();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
        }
    }
//    private void LoadData(){
//        final ProgressDialog pDialog = new ProgressDialog(ShowExamList.this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.POST, getURL(),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.hide();
//                        pDialog.dismiss();
//                        try {
//                            JSONObject object = new JSONObject(response);
//                            Log.i("Response--------",response);
//                            Exam_Schedule exam_schedule=new Exam_Schedule();
//                            list=exam_schedule.
////                            Leave user=new Leave();
////                            list=user.getLeave(object.getJSONArray("Response"));
////                            adapter=new LeaveAdapter(ShowExamList.this,list);
////                            rv_leave.setAdapter(adapter);
////                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
////                            rv_leave.setLayoutManager(layoutManager);
////                            rv_leave.setItemAnimator(new DefaultItemAnimator());
////                            total.setText("Total "+ list.size()+" Leaves");
//
//
//                        } catch (JSONException e) {
//                            pDialog.hide();
//                            pDialog.dismiss();
//                            if(!e.getMessage().trim().equals(""))
//                                Messages.ShowMessage(ShowExamList.this,e.getMessage().toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
//                        pDialog.dismiss();
//                        Messages.ShowMessage(ShowExamList.this,"problem on network connection");
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                Log.i("student_id--------",preferences.getString("student_id"));
//                params.put("student_id",preferences.getString("student_id"));
//                // params.put("student_id","20");
////                params.put("user_type",preferences.getString("login_type"));
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(ShowExamList.this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                7000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//
//    }
}

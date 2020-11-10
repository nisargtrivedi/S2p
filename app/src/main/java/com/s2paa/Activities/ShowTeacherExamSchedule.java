package com.s2paa.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Adapters.ExamAdapters;
import com.s2paa.Model.ExamList;
import com.s2paa.Model.Exam_Schedule;
import com.s2paa.Model.Standard;
import com.s2paa.R;
import com.s2paa.Utils.ExceptionsHelper;
import com.s2paa.Utils.KeyBoardHandling;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by admin on 9/1/2017.
 */

@EActivity(R.layout.teacher_exam_schedule)
public class ShowTeacherExamSchedule extends BaseActivity {

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    RecyclerView examschedule;

    @ViewById
    TTextView standard;

    Exam_Schedule exam_schedule;
    ArrayList<Exam_Schedule> list;
    ExamAdapters adapters;

    ExamList examList;
    int class_id;

    @AfterViews
    protected void init(){
        load();
        list=new ArrayList<>();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Exam Schedule");
        examList=new ExamList();
        examList= (ExamList) getIntent().getSerializableExtra("Class");
       // loadData();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Click
    public void standard()
    {
        startActivityForResult(new Intent(getApplicationContext(), StandardAct_.class), 100);
    }
//    private void loadData()
//    {
//        final ProgressDialog pDialog = new ProgressDialog(ShowTeacherExamSchedule.this);
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
//                            //      AppLogger.info("List--------"+list);
//                            adapters=new ExamAdapters(ShowTeacherExamSchedule.this,list);
//                            examschedule.setAdapter(adapters);
//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                            examschedule.setLayoutManager(layoutManager);
//                            examschedule.setItemAnimator(new DefaultItemAnimator());
//                        } catch (JSONException e) {
//                            pDialog.hide();
//                            pDialog.dismiss();
//                            if(!e.getMessage().trim().equals(""))
//                                Messages.ShowMessage(ShowTeacherExamSchedule.this,e.getMessage().toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
//                        pDialog.dismiss();
//                        Messages.ShowMessage(ShowTeacherExamSchedule.this,"problem on network connection");
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("name",examList.name);
//                params.put("class_id",class_id+"");
//                Log.i("Parameter-->",params.toString());
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(ShowTeacherExamSchedule.this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                7000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            try {
                KeyBoardHandling.hideSoftKeyboard(this);
                Standard schoolItem = null;
                if (data != null && data.getExtras() != null) {
                    schoolItem = (Standard) data.getExtras().getSerializable("STANDARD");
                }
                if (schoolItem == null) {
                    return;
                }
                ((TTextView) findViewById(R.id.standard)).setText(schoolItem.name);
                class_id=Integer.parseInt(schoolItem.class_id);
            } catch (Exception ex) {
                ExceptionsHelper.manage(ex);
            }
        }
        try {
            dataContext.examScheduleObjectSet.fill("exam_id=? and class_id=?",new String[]{examList.exam_id,class_id+""},null);
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
                            adapters=new ExamAdapters(ShowTeacherExamSchedule.this,dataContext.examScheduleObjectSet);
                            examschedule.setAdapter(adapters);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            examschedule.setLayoutManager(layoutManager);
                            examschedule.setItemAnimator(new DefaultItemAnimator());
        //loadData();
    }
}

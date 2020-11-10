package com.s2paa.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.s2paa.Adapters.ExamResultAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Model.Division;
import com.s2paa.Model.ExamList;
import com.s2paa.Model.ExamResult;
import com.s2paa.Model.Standard;
import com.s2paa.Model.Subject;
import com.s2paa.R;
import com.s2paa.Utils.ExceptionsHelper;
import com.s2paa.Utils.KeyBoardHandling;
import com.s2paa.Utils.Messages;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 8/21/2017.
 */

@EActivity(R.layout.result)
public class Result extends BaseActivity {


    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    TTextView result;
    @ViewById
            RecyclerView rv_result;

    @ViewById
    TTextView standard;

    @ViewById
    TTextView division;

    @ViewById
    TTextView subject;

    @ViewById
    TTextView total_marks;

    @ViewById
    TTextView avg_marks;

    @ViewById
    LinearLayout extra_data;

    @ViewById
            TTextView _total;

    @ViewById
            TTextView _grade;

    @ViewById
            TTextView itemAdd;

    @ViewById
            TTextView result_show;


    List<ExamResult> list=new ArrayList<>();
    ExamResultAdapter examResultAdapter;
    int class_id;
    int sec_id;
    int sub_id;
    String student_id;
    ExamList exam_id;
    int total=0;
    int count=0;
    int total_markss=0;
    String choose;

    @AfterViews
    public void init()
    {
        load();
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Result");


        exam_id=(ExamList)getIntent().getSerializableExtra("EXAM");
        if(preferences.getString("login_type").equalsIgnoreCase("student") || preferences.getString("login_type").equalsIgnoreCase("parent")){
            extra_data.setVisibility(View.GONE);
            Load_STUDENT_DATA(exam_id.exam_id);
        }
        if(preferences.getString("login_type").equalsIgnoreCase("teacher")){
            result.setVisibility(View.VISIBLE);
            itemAdd.setVisibility(View.VISIBLE);

        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        itemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddResult_.class).putExtra("exam_id",exam_id));
            }
        });

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), choose_result_.class), 104);
            }
        });
    }

    private void Load_STUDENT_DATA(final String exid)
    {
        final ProgressDialog pDialog = new ProgressDialog(Result.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST, SMS.GET_MARKS_BY_STUDENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();

                        total=0;
                        count=0;
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------", response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            list = Arrays.asList(mGson.fromJson(object.getJSONArray("Response")+"", ExamResult[].class));
                            examResultAdapter = new ExamResultAdapter(Result.this, list);
                            rv_result.setAdapter(examResultAdapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_result.setLayoutManager(layoutManager);
                            rv_result.setItemAnimator(new DefaultItemAnimator());

                            if(preferences.getString("login_type").equalsIgnoreCase("teacher")) {
                                if (choose.equalsIgnoreCase("SUBJECT WISE")) {
                                    total = list.get(0).mark_obtained;

                                    for (ExamResult result : list) {
                                        if (total < result.mark_obtained) {
                                            total = result.mark_obtained;
                                        }
                                    }
                                    for (ExamResult result : list) {
                                        if (total == result.mark_obtained)
                                            count++;
                                    }
                                    _total.setText("Higher Marks is");
                                    total_marks.setText(" " + total);
                                    _grade.setText("Total Student");

                                    avg_marks.setText(count+"");
                                } else {
                                    for (ExamResult result : list) {
                                        total = total + result.mark_obtained;
                                        total_markss = total_markss + result.mark_total;

                                    }
                                    total_marks.setText(total + "");
                                    if(total_markss!=0) {
                                        double avg = (total * 100) / total_markss;
                                        avg_marks.setText(avg + "%");
                                        _grade.setText("Percentage");
                                    }else{
                                        _grade.setText("0 Percentage");
                                    }
                                }
                            }else{
                                for (ExamResult result : list) {
                                    total = total + result.mark_obtained;
                                    total_markss=total_markss+result.mark_total;
                                }
                                if(total_markss==0)
                                    return;
                                double avg=(total*100)/total_markss;
                                avg_marks.setText(avg+"%");
                                _grade.setText("Percentage");
                                total_marks.setText(total + "");
                                //avg_marks.setText("A");

                            }
                            Log.i("TOTAL MARKS-->",total+"");

                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Result.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Result.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                if(preferences.getString("login_type").equalsIgnoreCase("student")) {
                    params.put("exam_id", exid+"");
                    params.put("class_id", preferences.getString("class")+"");
                    params.put("student_id", preferences.getString("student_id")+"");
                }
                if(preferences.getString("login_type").equalsIgnoreCase("parent")) {
                    params.put("exam_id", exid+"");
                    params.put("class_id", "1"+"");
                    params.put("student_id", "1");
                }
                if(preferences.getString("login_type").equalsIgnoreCase("teacher")) {
                    params.put("exam_id", exid+"");
                    params.put("class_id", class_id+"");
                    params.put("student_id", student_id+"");
                }
                Log.i("PARAMETER", params.toString());
                // params.put("student_id","20");
//                params.put("user_type",preferences.getString("login_type"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Result.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    private void LoadData(final int clas_id, final int su_id, final int se_id, final String ex_id) {
        final ProgressDialog pDialog = new ProgressDialog(Result.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST, SMS.GET_MARKS_BY_SUBJECT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        //list.clear();
                        total=0;
                        count=0;
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------", response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            list = Arrays.asList(mGson.fromJson(object.getJSONArray("Response")+"", ExamResult[].class));
                            examResultAdapter = new ExamResultAdapter(Result.this, list);
                            rv_result.setAdapter(examResultAdapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_result.setLayoutManager(layoutManager);
                            rv_result.setItemAnimator(new DefaultItemAnimator());

                            if(choose.equalsIgnoreCase("SUBJECT WISE")) {
                                if (list.size() > 0) {
                                    total = list.get(0).mark_obtained;
                                    for (ExamResult result : list) {
                                        if (total < result.mark_obtained)
                                            total = result.mark_obtained;
                                    }
                                    for (ExamResult result : list) {
                                        if (total == result.mark_obtained)
                                            count++;
                                    }
                                }

                                    _total.setText("Higher Marks is");
                                    total_marks.setText(" " + total);
                                    _grade.setText("Total Student");
                                    avg_marks.setText(count + "");
                                } else {
                                    for (ExamResult result : list) {
                                        total = total + result.mark_obtained;
                                        total_markss = total_markss + result.mark_total;
                                    }
                                    total_marks.setText(total + "");
                                    if (total_markss == 0)
                                        return;

                                    double avg = (total * 100) / total_markss;
                                    avg_marks.setText(avg + "");
                                    _grade.setText("Percentage");
                                }


                            Log.i("TOTAL MARKS-->",total+"");

                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Result.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Result.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                if(preferences.getString("login_type").equalsIgnoreCase("teacher")) {
                    params.put("exam_id", ex_id+"");
                    params.put("class_id", clas_id+"");
                    params.put("section_id", se_id+"");
                    params.put("subject_id", su_id+"");
                }else if(preferences.getString("login_type").equalsIgnoreCase("parent")){
                    params.put("exam_id", ex_id+"");
                    params.put("class_id", clas_id+"");
                    params.put("section_id", se_id+"");
                    params.put("subject_id", su_id+"");
                    params.put("student_id","1");

                }
                Log.i("PARAMETER", params.toString());
                // params.put("student_id","20");
//                params.put("user_type",preferences.getString("login_type"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Result.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Click
    public void standard()
    {
        startActivityForResult(new Intent(getApplicationContext(), StandardAct_.class), 100);
    }

    @Click
    public void division()
    {
        startActivityForResult(new Intent(getApplicationContext(), DivisionAct_.class).putExtra("class_id",class_id+""), 101);
    }

    @Click
    public void subject()
    {
        if(choose!=null){
        if(choose.equalsIgnoreCase("SUBJECT WISE"))
            startActivityForResult(new Intent(getApplicationContext(), SubjectData_.class).putExtra("class_id",class_id+"").putExtra("section_id",sec_id+""), 102);
        else if(choose.equalsIgnoreCase("STUDENT WISE"))
            startActivityForResult(new Intent(getApplicationContext(), StudentAct_.class).putExtra("class_id",class_id+"").putExtra("section_id",sec_id+""), 105);
        }else
            return;
    }

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
        else if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            try {
                KeyBoardHandling.hideSoftKeyboard(this);
                Division schoolItem = null;
                if (data != null && data.getExtras() != null) {
                    schoolItem = (Division) data.getExtras().getSerializable("DIVISION");

                }
                if (schoolItem == null) {
                    return;
                }
                ((TTextView) findViewById(R.id.division)).setText(schoolItem.name);
                sec_id=Integer.parseInt(schoolItem.section_id);
            } catch (Exception ex) {
                ExceptionsHelper.manage(ex);
            }
        }
        else if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            try {
                KeyBoardHandling.hideSoftKeyboard(this);
                Subject schoolItem = null;
                if (data != null && data.getExtras() != null) {
                    schoolItem = (Subject) data.getExtras().getSerializable("SUBJECT");

                }
                if (schoolItem == null) {
                    return;
                }
                ((TTextView) findViewById(R.id.subject)).setText(schoolItem.name);
                sub_id=schoolItem.subject_id;
                LoadData(class_id,sub_id,sec_id,exam_id.exam_id);

            } catch (Exception ex) {
                ExceptionsHelper.manage(ex);
            }
        }
        else if (requestCode == 104 && resultCode == Activity.RESULT_OK) {
            try {
                KeyBoardHandling.hideSoftKeyboard(this);
                String schoolItem = null;
                if (data != null && data.getExtras() != null) {
                    schoolItem = data.getExtras().getString("SELECT");

                }
                if (schoolItem == null) {
                    return;
                }
              //  ((TTextView) findViewById(R.id.result)).setText(schoolItem);
               choose=schoolItem;
                //LoadData(class_id,sub_id,sec_id,exam_id.exam_id);
                if(choose.equalsIgnoreCase("SUBJECT WISE")){
                        subject.setText("Subject");
                    result_show.setText("Show result Subject wise");
                }else if(choose.equalsIgnoreCase("STUDENT WISE")){
                    subject.setText("Roll no");
                    result_show.setText("Show result Student wise");
                }

            } catch (Exception ex) {
                ExceptionsHelper.manage(ex);
            }
        }
        else if (requestCode == 105 && resultCode == Activity.RESULT_OK) {
            try {
                KeyBoardHandling.hideSoftKeyboard(this);
                String schoolItem = null;
                if (data != null && data.getExtras() != null) {
                    schoolItem = (String) data.getExtras().getSerializable("ROLLNO");

                }
                if (schoolItem == null) {
                    return;
                }
               // ((TTextView) findViewById(R.id.subject)).setText(schoolItem.name);
                student_id=schoolItem;
                //LoadData(class_id,sub_id,sec_id,exam_id.exam_id);
                result_show.setText("Show result Student wise");
                Load_STUDENT_DATA(exam_id.exam_id);

            } catch (Exception ex) {
                ExceptionsHelper.manage(ex);
            }
        }
    }


}

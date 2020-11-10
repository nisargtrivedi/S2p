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
import android.widget.Button;
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
import com.s2paa.Adapters.ResultAdapters;
import com.s2paa.Application.SMS;
import com.s2paa.Model.*;
import com.s2paa.Model.Result;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
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
 * Created by admin on 9/26/2017.
 */

@EActivity(R.layout.add_result)
public class AddResult extends BaseActivity {


    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtActionTitle;

    @ViewById
    RecyclerView rv_update_result;

    @ViewById
    TTextView standard;
    @ViewById
    TTextView division;
    @ViewById
    TTextView subject;

    @ViewById
    Button save;

    int class_id;
    int sec_id;
    int sub_id;

    ResultAdapters adapters;

    List<Result> list=new ArrayList<>();
    ExamList exam_id;
    @AfterViews
    public void init(){
        load();
        exam_id=(ExamList)getIntent().getSerializableExtra("exam_id");
        preferences = new AppPreferences(AddResult.this);
        setSupportActionBar(toolbar);
        showActionBack();


        txtActionTitle.setText("Add Result");
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

    @Click
    public void division()
    {
        startActivityForResult(new Intent(getApplicationContext(), DivisionAct_.class).putExtra("class_id",class_id+""), 101);
    }

    @Click
    public void subject(){
        startActivityForResult(new Intent(getApplicationContext(), SubjectData_.class).putExtra("class_id",class_id+"").putExtra("section_id",sec_id+""), 102);
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
                getResult();

            } catch (Exception ex) {
                ExceptionsHelper.manage(ex);
            }
        }

    }


    @Click
    public void save(){
        SaveResult();
    }

    private void getResult() {
        final ProgressDialog pDialog = new ProgressDialog(AddResult.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST, SMS.ADD_RESULT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();

                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------", response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            list = Arrays.asList(mGson.fromJson(object.getJSONArray("Response")+"", com.s2paa.Model.Result[].class));
                            adapters = new ResultAdapters(AddResult.this, list);
                            rv_update_result.setAdapter(adapters);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_update_result.setLayoutManager(layoutManager);
                            rv_update_result.setItemAnimator(new DefaultItemAnimator());


                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(AddResult.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(AddResult.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id", class_id+"");
                params.put("section_id", sec_id+"");
                params.put("subject_id", sub_id+"");
                params.put("exam_id", exam_id.exam_id+"");

                Log.i("PARAMETER", params.toString());
                // params.put("student_id","20");
//                params.put("user_type",preferences.getString("login_type"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddResult.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void SaveResult() {
        final ProgressDialog pDialog = new ProgressDialog(AddResult.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST, SMS.UPDATE_RESULT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();

                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------", response);



                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(AddResult.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(AddResult.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("results", adapters.composeJSONfromSQLite()+"");


                Log.i("PARAMETER", params.toString());
                // params.put("student_id","20");
//                params.put("user_type",preferences.getString("login_type"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddResult.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

}

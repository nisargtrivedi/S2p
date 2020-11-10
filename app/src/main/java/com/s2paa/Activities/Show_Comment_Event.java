package com.s2paa.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.s2paa.Adapters.CommentAdapters;
import com.s2paa.Application.SMS;
import com.s2paa.Model.Comment;
import com.s2paa.R;
import com.s2paa.Utils.EEditText;
import com.s2paa.Utils.KeyBoardHandling;
import com.s2paa.Utils.Messages;

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

@EActivity(R.layout.comment_list)
public class Show_Comment_Event extends BaseActivity {


    @ViewById
    RecyclerView rv_comment;

    @ViewById
    EEditText txt_comment;

    @ViewById
    Button btn_save_comment;

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtActionTitle;

    List<Comment> list=new ArrayList<>();
    CommentAdapters adapters;
    String exid;
    @AfterViews
    public void init(){
        exid=getIntent().getStringExtra("event_id");


        setSupportActionBar(toolbar);
//        toolbar.setTitle("");
        showActionBack();

        //Glide.with(this).load(preferences.getString("img").toString()).into(img);
        //getSupportActionBar().setTitle("");

        txtActionTitle.setText("Event Comment");
        load();
        if(exid!=null)
            LoadData(exid);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    @Click
    public void btn_save_comment(){
        if(!TextUtils.isEmpty(txt_comment.getText().toString().trim()))
            if(preferences.getString("login_type").equalsIgnoreCase("student"))
                SaveComment(txt_comment.getText().toString(),preferences.getString("login_type"),preferences.getString("student_id"),exid);
            else if(preferences.getString("login_type").equalsIgnoreCase("teacher"))
                SaveComment(txt_comment.getText().toString(),preferences.getString("login_type"),preferences.getString("teacher_id"),exid);
            else if(preferences.getString("login_type").equalsIgnoreCase("parent"))
                SaveComment(txt_comment.getText().toString(),preferences.getString("login_type"),preferences.getString("parent_id"),exid);

        txt_comment.setText("");
        KeyBoardHandling.hideSoftKeyboard(Show_Comment_Event.this);
        LoadData(exid);
    }

    private void LoadData(final String ex_id) {
        final ProgressDialog pDialog = new ProgressDialog(Show_Comment_Event.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST, SMS.GET_COMMENT,
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
                            list = Arrays.asList(mGson.fromJson(object.getJSONArray("Response")+"", Comment[].class));
                            adapters = new CommentAdapters(Show_Comment_Event.this, list);
                            rv_comment.setAdapter(adapters);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_comment.setLayoutManager(layoutManager);
                            rv_comment.setItemAnimator(new DefaultItemAnimator());


                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Show_Comment_Event.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Show_Comment_Event.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                    params.put("event_id", ex_id+"");

                Log.i("PARAMETER", params.toString());
                // params.put("student_id","20");
//                params.put("user_type",preferences.getString("login_type"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Show_Comment_Event.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void SaveComment(final String commment,final String user_type,final String user_id,final String event_id) {
        final ProgressDialog pDialog = new ProgressDialog(Show_Comment_Event.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST, SMS.EVENT_COMMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();

                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------", response);
//                            GsonBuilder builder = new GsonBuilder();
//                            Gson mGson = builder.create();
//                            list = Arrays.asList(mGson.fromJson(object.getJSONArray("Response")+"", Comment[].class));
//                            adapters = new CommentAdapters(Show_Comment_Event.this, list);
//                            rv_comment.setAdapter(adapters);
//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                            rv_comment.setLayoutManager(layoutManager);
//                            rv_comment.setItemAnimator(new DefaultItemAnimator());


                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Show_Comment_Event.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Show_Comment_Event.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("comments", commment+"");
                params.put("user_type", user_type+"");
                params.put("user_id", user_id+"");
                params.put("event_id", event_id+"");

                Log.i("PARAMETER", params.toString());
                // params.put("student_id","20");
//                params.put("user_type",preferences.getString("login_type"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Show_Comment_Event.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }



}

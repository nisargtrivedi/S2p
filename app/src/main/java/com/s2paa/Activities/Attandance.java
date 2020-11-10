package com.s2paa.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
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
import com.s2paa.Adapters.AttendanceAdapters;
import com.s2paa.Application.SMS;
import com.s2paa.Model.AttendanceModel;
import com.s2paa.Model.Division;
import com.s2paa.Model.Standard;
import com.s2paa.R;
import com.s2paa.Utils.DateUtils;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/2/2017.
 */

@EActivity(R.layout.teacher_attandance)
public class Attandance extends BaseActivity {

    @ViewById
    TTextView todaydate;

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    RecyclerView attendance_list;
    @ViewById
    TTextView standard;

    @ViewById

    TTextView division;

    @ViewById
    AppCompatButton save;

    ArrayList<AttendanceModel> list=new ArrayList<>();

    AttendanceModel model=new AttendanceModel();
    AttendanceAdapters adapters;
    int class_id;
    int sec_id;
    //RecyclerView standard_list;

    @AfterViews
    protected void init(){
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Attendance");
        todaydate.setText(DateUtils.GETLOCALE_DATE());

        // LoadData();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
    private void LoadData(){
        final ProgressDialog pDialog = new ProgressDialog(Attandance.this);
        pDialog.setMessage("Loading Attendance Details...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SMS.GET_STUDENT_FOR_CLASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        list.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response---",response);
                            AttendanceModel attendanceModel=new AttendanceModel();
                            list=attendanceModel.getAttendanceData(object.getJSONArray("Response"));
                            adapters=new AttendanceAdapters(Attandance.this,list);
                            attendance_list.setAdapter(adapters);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            attendance_list.setLayoutManager(layoutManager);
                            attendance_list.setItemAnimator(new DefaultItemAnimator());
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Attandance.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Attandance.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id",class_id+"");
                params.put("section_id",sec_id+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Attandance.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

//        adapters.setOnAttendanceClickListener(new AttendanceListener() {
//            @Override
//            public void Item_Click() {
//
//            }
//        });
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
                LoadData();
            } catch (Exception ex) {
                ExceptionsHelper.manage(ex);
            }
        }
    }

//    public String composeJSONfromSQLite(){
//        ArrayList<HashMap<String, String>> wordList;
//        wordList = new ArrayList<HashMap<String, String>>();
//
//        for(int i=0;i<5;i++){
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("std_id", "1");
//                map.put("status", "present");
//                map.put("dates", "2017-07-31");
//                wordList.add(map);
//        }
//        Gson gson = new GsonBuilder().create();
//        //Use GSON to serialize Array List to JSON
//        return gson.toJson(wordList);
//    }

    @Click
    public void save()
    {
        final ProgressDialog pDialog = new ProgressDialog(Attandance.this);
        pDialog.setMessage("Loading Attendance Details...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SMS.SAVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------------",object.toString());
//
                            ArrayList<AttendanceModel> list=new ArrayList<>();
                            AttendanceModel model=new AttendanceModel();
                            list=model.SaveAttendanceData(object.getJSONArray("Response"));

                            //Log.i("Response--------------",object.getJSONObject("Response").getString("status")+"");
                            Toast.makeText(getApplicationContext(),object.getString("message").toString()+"",Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Attandance.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("attedance",adapters.composeJSONfromSQLite().toString());
                Log.i("DATAA----",adapters.composeJSONfromSQLite());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Attandance.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


}

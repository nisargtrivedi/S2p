package com.s2paa.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
import com.s2paa.Application.SMS;
import com.s2paa.Model.AttendanceModel;
import com.s2paa.Model.Division;
import com.s2paa.Model.Standard;
import com.s2paa.R;
import com.s2paa.Utils.DateUtils;
import com.s2paa.Utils.EEditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/22/2017.
 */

@EActivity(R.layout.add_home_work)
public class AddTeacherHomeWork extends BaseActivity {

    @ViewById
    TTextView today_date;

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtActionTitle;

    @ViewById
    EEditText HomeWork;

    @ViewById
    EEditText HomeWorkTitle;

    @ViewById
    TTextView standard;

    @ViewById
    TTextView division;

    @ViewById
    LinearLayout select_student_layout;

    @ViewById
    AppCompatRadioButton class_section;

    @ViewById
    AppCompatRadioButton single_student;

    @ViewById
    AppCompatSpinner select_student;

    int class_id;
    int sec_id;
    String home_work_type="1";
    Date cDate = new Date();
    String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

    ArrayAdapter<String> adapters;
    ArrayList<AttendanceModel> list=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    @AfterViews
    protected void init(){
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Add Home work");
        today_date.setText(DateUtils.GETLOCALE_DATE());
        load();
        // LoadData();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        single_student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked==true)
                    select_student_layout.setVisibility(View.VISIBLE);
                home_work_type="2";
            }
        });
        class_section.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked==true)
                    select_student_layout.setVisibility(View.GONE);
                    home_work_type="1";
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
        //initData();
    }
    private void LoadData(){
        final ProgressDialog pDialog = new ProgressDialog(AddTeacherHomeWork.this);
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
                            AttendanceModel attendanceModel=new AttendanceModel();
                            list=attendanceModel.getAttendanceData(object.getJSONArray("Response"));
                            //adapters=new AttendanceAdapters(AddTeacherHomeWork.this,list);
                            for (int i=0;i<list.size();i++){
                                name.add(list.get(i).student_name);
                            }
                            adapters=new ArrayAdapter<String>(AddTeacherHomeWork.this,android.R.layout.simple_list_item_1,name);
                            select_student.setAdapter(adapters);
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(AddTeacherHomeWork.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(AddTeacherHomeWork.this,"problem on network connection");
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

        RequestQueue requestQueue = Volley.newRequestQueue(AddTeacherHomeWork.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Click
    public void save()
    {
        if(!TextUtils.isEmpty(HomeWork.getText().toString()) && !TextUtils.isEmpty(HomeWorkTitle.getText().toString().trim()))
            SaveData();
        if(TextUtils.isEmpty(HomeWorkTitle.getText().toString().trim()))
                HomeWorkTitle.setError("please enter home work title");
        if(TextUtils.isEmpty(HomeWork.getText().toString().trim()))
            HomeWorkTitle.setError("please enter home work");
    }

    private void SaveData(){
        final ProgressDialog pDialog = new ProgressDialog(AddTeacherHomeWork.this);
        pDialog.setMessage("Loading ...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SMS.SAVE_HOMEWORK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();

                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("ResponseHomeWork-->",response);
                            String ans=object.getString("message").toString();
                            Toast.makeText(AddTeacherHomeWork.this,ans+"",Toast.LENGTH_LONG).show();
                            finish();
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(AddTeacherHomeWork.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(AddTeacherHomeWork.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id",class_id+"");
                params.put("section_id",sec_id+"");
                if(home_work_type.toString().equalsIgnoreCase("2"))
                    params.put("student_id",list.get(select_student.getSelectedItemPosition()).student_id+"");
                else
                    params.put("student_id","");

                params.put("teacher_id",preferences.getString("teacher_id").toString()+"");
                params.put("date",fDate+"");
                params.put("home_work_type",home_work_type+"");
                params.put("homework",HomeWork.getText().toString().trim()+"");
                params.put("homeworktitle",HomeWorkTitle.getText().toString().trim()+"");
                Log.i("Parameter",params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddTeacherHomeWork.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}
//    Save HomeWork
//url : ENDPOINT +"saveHomework";
//        parameter : student_id,teacher_id,date,home_work_type,homework,class_id,section_id
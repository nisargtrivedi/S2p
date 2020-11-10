package com.s2paa.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.s2paa.R;
import com.s2paa.Utils.EEditText;
import com.s2paa.Utils.Messages;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/29/2017.
 */

@EActivity(R.layout.add_parent_leave)
public class AddParentLeave extends BaseActivity {


    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    EEditText leave_title;

    @ViewById
    Spinner leave_student;

    @ViewById
    EEditText leave_date;

    @ViewById
    EEditText leave_days;

    @ViewById
    EEditText leave_detail;

    @ViewById
    AppCompatButton save;

    @ViewById
    TTextView itemAdd;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;
    ArrayList<AttendanceModel> list=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();

    ArrayAdapter<String> adapters;
    @AfterViews
    protected void init()
    {
        load();
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Leave Request");
        itemAdd.setVisibility(View.VISIBLE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        LoadData();
    }
    public void setCurrentDateOnView() {

        if (leave_date.getText() != null && leave_date.getText().toString() != null) {
            String date[] = leave_date.getText().toString().trim().split("-");
            if (date != null && date.length >= 3) {
                year = Integer.parseInt(date[2]);
                month = Integer.parseInt(date[1]) - 1;
                day = Integer.parseInt(date[0]);
            } else {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            }
        } else {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        leave_date.setText(new StringBuilder()
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));
    }
    @Click
    public void leave_date()
    {
        setCurrentDateOnView();
        showDialog(DATE_DIALOG_ID);
    }
    @Click
    public void save()
    {
        if(TextUtils.isEmpty(leave_title.getText().toString().trim()))
        {
            leave_title.setError("Enter Leave Title");
        }
        if(TextUtils.isEmpty(leave_date.getText().toString().trim()))
        {
            leave_date.setError("Enter Leave Date");
        }
        if(TextUtils.isEmpty(leave_days.getText().toString().trim()))
        {
            leave_title.setError("Enter Leave Days");
        }
        if(TextUtils.isEmpty(leave_detail.getText().toString().trim()))
        {
            leave_detail.setError("Enter Leave Details");
        }
        if(!TextUtils.isEmpty(leave_title.getText().toString().trim()) && !TextUtils.isEmpty(leave_date.getText().toString().trim()) && !TextUtils.isEmpty(leave_days.getText().toString().trim()) && !TextUtils.isEmpty(leave_detail.getText().toString().trim())){
                saveData();
        }
    }
    public String getURL()
    {
        if(preferences.getString("login_type").toString().equalsIgnoreCase("student"))
            return   SMS.STUDENT_LEAVE;
        else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
            return SMS.SAVE_LEAVE;
        else
            return SMS.STUDENT_LEAVE;
    }
    private void saveData(){
        final ProgressDialog pDialog = new ProgressDialog(AddParentLeave.this);
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
                            finish();
//                            Leave user=new Leave();
//                            list=user.getLeave(object.getJSONArray("Response"));
//                            adapter=new LeaveAdapter(LeaveList.this,list);
//                            rv_leave.setAdapter(adapter);
//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                            rv_leave.setLayoutManager(layoutManager);
//                            rv_leave.setItemAnimator(new DefaultItemAnimator());
//                            total.setText("Total "+ list.size()+" Leaves");


                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(AddParentLeave.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(AddParentLeave.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("student_id",preferences.getString("student_id"));
                params.put("title",leave_title.getText().toString().trim());
                params.put("details",leave_detail.getText().toString().trim());
                params.put("parent_id",preferences.getString("parent_id"));
                params.put("day_of_no",leave_days.getText().toString().trim());
                params.put("leave_date",leave_date.getText().toString().trim());
                Log.i("Parameter--------",params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddParentLeave.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {


        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            leave_date.setText(new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day)
                    .append(" "));

        }
    };



    private void LoadData(){
        final ProgressDialog pDialog = new ProgressDialog(AddParentLeave.this);
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
                            adapters=new ArrayAdapter<String>(AddParentLeave.this,android.R.layout.simple_list_item_1,name);
                            leave_student.setAdapter(adapters);
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(AddParentLeave.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(AddParentLeave.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id","1");
                params.put("section_id","1");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddParentLeave.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}

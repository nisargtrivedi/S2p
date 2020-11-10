package com.s2paa.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.s2paa.Application.SMS;
import com.s2paa.Model.Division;
import com.s2paa.Model.Standard;
import com.s2paa.Model.Subject;
import com.s2paa.R;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/24/2017.
 */

@EActivity(R.layout.add_teacher_class_activity)
public class AddTeacherClassActivity extends BaseActivity {


    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    TTextView save;

    @ViewById
    TTextView title;

    @ViewById
    TTextView class_details;

    @ViewById
    EEditText details;

    @ViewById
    TTextView subject;

    @ViewById
    TTextView date;

    int class_id;
    int sec_id,sub_id;

    @AfterViews
    public void init()
    {
        load();

        txtActionTitle.setText("Add New Class Activites");
        setSupportActionBar(toolbar);
        showActionBack();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());
       date.setText(formatted);
    }
    @Click
    public void save(){
        if(TextUtils.isEmpty(title.getText().toString())){
            title.setError("please enter title");
        }
        if(TextUtils.isEmpty(details.getText().toString())){
            title.setError("please enter details");
        }
        if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(details.getText().toString())){
            LoadData();
        }
    }
    private void LoadData(){
        final ProgressDialog pDialog = new ProgressDialog(AddTeacherClassActivity.this);
        pDialog.setMessage("Loading Details...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SMS.SAVE_CLASS_ACTIVITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        //list.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response---",response);
                            Toast.makeText(AddTeacherClassActivity.this,object.getJSONObject("Response").getString("message").toString(),Toast.LENGTH_LONG).show();
                            finish();
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(AddTeacherClassActivity.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(AddTeacherClassActivity.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id",class_id+"");
                params.put("section_id",sec_id+"");
                params.put("title",title.getText().toString()+"");
                params.put("details",details.getText().toString().trim()+"");
                //params.put("teacher_id",preferences.getString("teacher_id"));
                params.put("date",date.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddTeacherClassActivity.this);
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
    public void title()
    {
        startActivityForResult(new Intent(getApplicationContext(), StandardAct_.class), 100);
    }

    @Click
    public void subject(){
        startActivityForResult(new Intent(getApplicationContext(), SubjectData_.class).putExtra("class_id",class_id+"").putExtra("section_id",sec_id+""), 102);
    }
    @Click
    public void class_details()
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
                ((TTextView) findViewById(R.id.title)).setText(schoolItem.name);
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
                ((TTextView) findViewById(R.id.class_details)).setText(schoolItem.name);
                sec_id=Integer.parseInt(schoolItem.section_id);
               // LoadData();
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
                //LoadData();
            } catch (Exception ex) {
                ExceptionsHelper.manage(ex);
            }
        }

    }
}

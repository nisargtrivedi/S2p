package com.s2paa.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
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
import com.s2paa.Adapters.SchoolAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Model.School;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.EEditText;
import com.s2paa.Utils.Messages;

import org.androidannotations.annotations.AfterViews;
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
 * Created by admin on 9/22/2017.
 */

@EActivity(R.layout.school_list)
public class MainPage extends BaseActivity  {


    @ViewById
    AppCompatSpinner rv_school;

    @ViewById
    EEditText school;

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtActionTitle;

    List<School> list=new ArrayList<>();
    SchoolAdapter schoolAdapter;
    int lock=0;
    @AfterViews
    public void init(){
        load();
        preferences = new AppPreferences(MainPage.this);
        setSupportActionBar(toolbar);
        txtActionTitle.setText("Select School");
        if (!TextUtils.isEmpty(preferences.getString("name"))) {
            startActivity(new Intent(MainPage.this, Mainscreen_.class));
            finish();
        }
        school.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                   LoadData();
                }
                return false;
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void LoadData() {
        final ProgressDialog pDialog = new ProgressDialog(MainPage.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST, SMS.MAIN_SCHOOL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        //list.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------", response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            list = Arrays.asList(mGson.fromJson(object.getJSONArray("Response")+"", School[].class));
//                            List<School> arSchoolList=new ArrayList<>();
//                            School school=new School();
//                            school.school_id=0;
//                            school.school_name="-------Select School------";
//                            school.school_url="";
//                            arSchoolList.add(0,school);
//                            arSchoolList.addAll(list);
//                            schoolAdapter=new SchoolAdapter(MainPage.this,arSchoolList);
//                            rv_school.setAdapter(schoolAdapter);

//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                            rv_school.setLayoutManager(layoutManager);
//                            rv_school.setItemAnimator(new DefaultItemAnimator());
//                            rv_school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                                    if(position>0) {
//                                        School school = (School) parent.getItemAtPosition(position);
//
//                                        SMS.ENDPOINT = school.school_url;
//
//                                    }
//                                    }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> parent) {
//
//                                }
//                            });
                           for(School sc:list){
                               if(sc.school_code.equalsIgnoreCase(school.getText().toString()))
                               {
                                   lock=1;
                                   SMS sms= new SMS(sc.school_url,MainPage.this);
                                   startActivity(new Intent(MainPage.this, Login_.class).putExtra("school",sc.school_id));
                                   finish();
                               }
                           }
                           if(lock==0){
                               school.setText("");
                               school.setError("enter school code");
                               school.requestFocus();
                           }

                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(MainPage.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(MainPage.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                // params.put("student_id","20");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainPage.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


}

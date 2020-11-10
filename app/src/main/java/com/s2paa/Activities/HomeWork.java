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
import com.s2paa.Adapters.HomeWorkAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Listener.HomeWorkClick;
import com.s2paa.Listener.RecycleViewItemClick;
import com.s2paa.Model.ClassActivity;
import com.s2paa.Model.Home_Work;
import com.s2paa.R;
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
 * Created by admin on 8/1/2017.
 */

@EActivity(R.layout.home_work)
public class HomeWork extends BaseActivity {

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    TTextView itemAdd;
    @ViewById
    RecyclerView homework;

    HomeWorkAdapter adapter;
    ArrayList<Home_Work> list=new ArrayList<>();
    ArrayList<Home_Work> list2=new ArrayList<>();
    Home_Work home_work;
    @AfterViews
    protected void init()
    {
        load();
        home_work=new Home_Work();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Home Work");
        LoadData();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher")){
            itemAdd.setVisibility(View.VISIBLE);
        }else if(preferences.getString("login_type").toString().equalsIgnoreCase("student") || preferences.getString("login_type").equalsIgnoreCase("parent")){
            itemAdd.setVisibility(View.GONE);
        }
    }
    public String URL()
    {
        String url="";
        if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher"))
            url=SMS.HOME_WORK_TEACHER;
        else if(preferences.getString("login_type").toString().equalsIgnoreCase("student"))  {
            url = SMS.HOME_WORK;
        }
        else if(preferences.getString("login_type").equalsIgnoreCase("parent"))
        {
            url = SMS.HOME_WORK;
        }
        return url;
    }
    @Click
    protected void itemAdd()
    {
        startActivity(new Intent(HomeWork.this,AddTeacherHomeWork_.class));
    }
    private void LoadData(){
        final ProgressDialog pDialog = new ProgressDialog(HomeWork.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        list.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------",response);
                            Home_Work user=new Home_Work();
                            if(preferences.getString("login_type").toString().equalsIgnoreCase("student"))
                                    list=user.StudentReponseData(object.getJSONArray("Response"));
                           if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher"))
                                    list=user.ReponseData(object.getJSONArray("Response"));
                            if(preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
                                list=user.StudentReponseData(object.getJSONArray("Response"));


                            adapter=new HomeWorkAdapter(HomeWork.this,list);
                            homework.setAdapter(adapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            homework.setLayoutManager(layoutManager);
                            homework.setItemAnimator(new DefaultItemAnimator());

                            adapter.setOnCatClickListener(new HomeWorkClick() {
                                @Override
                                public void onClick(Home_Work homeworkActivity) {
                                    Intent intent=new Intent(HomeWork.this,DetailsActivity_.class);
                                    intent.putExtra("Home", homeworkActivity);
                                    startActivity(intent);
                                }
                            });
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(HomeWork.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(HomeWork.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id",preferences.getString("student_id"));
//                params.put("user_type",preferences.getString("login_type"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HomeWork.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}

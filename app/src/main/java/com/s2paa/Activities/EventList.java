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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobandme.ada.Entity;
import com.s2paa.Adapters.EventAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Model.EventObjects;
import com.s2paa.R;
import com.s2paa.Utils.ExceptionsHelper;
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


@EActivity(R.layout.event)
public class EventList extends BaseActivity {

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    RecyclerView rv_event;
    List<EventObjects> list=new ArrayList<>();
    EventAdapter  eventAdapter;
    @AfterViews
    public void init()
    {
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Event");
        LoadEvents();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }

    public void LoadEvents()
    {
        final ProgressDialog pDialog = new ProgressDialog(EventList.this);
        pDialog.setMessage("Loading.....");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SMS.GET_EVENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response Event------>",response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            list = Arrays.asList(mGson.fromJson(object.getJSONArray("Response")+"", EventObjects[].class));
                            eventAdapter = new EventAdapter(EventList.this, list);
                            rv_event.setAdapter(eventAdapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_event.setLayoutManager(layoutManager);
                            rv_event.setItemAnimator(new DefaultItemAnimator());
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(EventList.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(EventList.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(preferences.getString("login_type").toString().equalsIgnoreCase("student"))
                params.put("user_id",preferences.getString("student_id"));
                else if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher"))
                    params.put("user_id",preferences.getString("teacher_id"));
                else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
                    params.put("user_id",preferences.getString("parent_id"));

                params.put("user_type",preferences.getString("login_type"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(EventList.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void addData()
    {
            try {
                    dataContext.eventObjectSet.fill();
                    for (int i = 0; i < dataContext.eventObjectSet.size(); i++) {
                        dataContext.eventObjectSet.get(i).setStatus(Entity.STATUS_DELETED);
                    }
                    dataContext.eventObjectSet.save();
                    dataContext.eventObjectSet.addAll(list);
                    dataContext.eventObjectSet.save();
            } catch (Exception ex) {
                ExceptionsHelper.manage(EventList.this, ex);
            }

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}

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
import com.s2paa.Adapters.AnnounceAdapters;
import com.s2paa.Application.SMS;
import com.s2paa.Model.Annoucement;
import com.s2paa.R;
import com.s2paa.Utils.Messages;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by admin on 8/1/2017.
 */

@EActivity(R.layout.announcement)
public class AnnouncementActivity extends BaseActivity {

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    RecyclerView annoucement_list;

    Annoucement obj=new Annoucement();
    ArrayList<Annoucement> list=new ArrayList<>();
    AnnounceAdapters adapters;

    @AfterViews
    protected void init(){
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Announcement");
        LoadData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }

    private void LoadData(){
//        obj.class_name_with_section="Nursery - A";
//        obj.details="Nice test Announcement.....";
//
//        list.add(obj);
//        list.add(obj);
//        list.add(obj);
//        list.add(obj);
//        list.add(obj);
//        list.add(obj);
        final ProgressDialog pDialog = new ProgressDialog(AnnouncementActivity.this);
        pDialog.setMessage("Loading Details...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SMS.ANNOUNCEMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);

                            JSONArray array=object.getJSONArray("Response");
                            for(int i=0;i<array.length();i++){
                                Annoucement annoucement=new Annoucement();
                                annoucement.class_name_with_section=array.getJSONObject(i).getString("class");
                                annoucement.details=array.getJSONObject(i).getString("message");
                                long da=Long.parseLong(array.getJSONObject(i).getString("create_timestamp"));
                                Date date = new Date(da*1000L);
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                                sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                                String formattedDate = sdf.format(date);
                                System.out.println(formattedDate);
                                try {
                                    annoucement.create_timestamp = formattedDate;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                list.add(annoucement);
                            }
                            adapters=new AnnounceAdapters(AnnouncementActivity.this,list);
                            annoucement_list.setAdapter(adapters);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            annoucement_list.setLayoutManager(layoutManager);
                            annoucement_list.setItemAnimator(new DefaultItemAnimator());
//                              Log.i("Response--------------",object.getJSONObject("Response").toString());
//                            Log.i("Response--------------",object.getJSONObject("Response").getString("status")+"");
//                            Toast.makeText(getApplicationContext(),object.getJSONObject("Response").getString("status").toString()+"",Toast.LENGTH_LONG).show();
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
                        Messages.ShowMessage(AnnouncementActivity.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("login_type",preferences.getString("login_type"));
                Log.i("DATAA----",preferences.getString("login_type"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AnnouncementActivity.this);
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

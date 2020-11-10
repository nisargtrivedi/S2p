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
import com.s2paa.Adapters.FeesAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Model.Fees;
import com.s2paa.R;
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
 * Created by nisarg on 09/11/17.
 */


@EActivity(R.layout.fees)
public class FeesList extends BaseActivity {

    @ViewById
    RecyclerView fees_list;

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;


    FeesAdapter adapter;

    List<Fees> feesArrayList;

    @AfterViews
    public void init()
    {
        load();
        feesArrayList=new ArrayList<>();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Fees List");

        LoadData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void LoadData(){
        final ProgressDialog pDialog = new ProgressDialog(FeesList.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, SMS.FEES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------",response);
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();

                            feesArrayList = Arrays.asList(mGson.fromJson(object.getJSONArray("Response")+"", Fees[].class));


                            adapter=new FeesAdapter(FeesList.this,feesArrayList);
                            fees_list.setAdapter(adapter);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            fees_list.setLayoutManager(layoutManager);
                            fees_list.setItemAnimator(new DefaultItemAnimator());



                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(FeesList.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(FeesList.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("student_id",preferences.getString("student_id"));
                // params.put("student_id","20");
//                params.put("user_type",preferences.getString("login_type"));
                Log.i("student_id--------",preferences.getString("student_id"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FeesList.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

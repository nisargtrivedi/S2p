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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobandme.ada.Entity;
import com.s2paa.Adapters.RemarkAdapters;
import com.s2paa.Application.SMS;
import com.s2paa.Model.Annoucement;
import com.s2paa.Model.Remark;
import com.s2paa.R;
import com.s2paa.Utils.ExceptionsHelper;
import com.s2paa.Utils.Messages;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/21/2017.
 */

@EActivity(R.layout.announcement)
public class Remarks extends BaseActivity
{
    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    RecyclerView annoucement_list;

    Annoucement obj=new Annoucement();
    ArrayList<Remark> list=new ArrayList<>();
    RemarkAdapters adapters;

    @AfterViews
    protected void init(){
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Remarks");
        LoadData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void LoadData(){
        final ProgressDialog pDialog = new ProgressDialog(Remarks.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, SMS.REMARKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        list.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Remarks Response",response);

                            JSONArray array=object.getJSONArray("Response");
                            Remark remark=new Remark();
                            list=remark.getRemarks(array);
                            saveClass();
                            adapters=new RemarkAdapters(Remarks.this,dataContext.remarkObjectSet);
                            annoucement_list.setAdapter(adapters);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            annoucement_list.setLayoutManager(layoutManager);
                            annoucement_list.setItemAnimator(new DefaultItemAnimator());

                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Remarks.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Remarks.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(!TextUtils.isEmpty(preferences.getString("student_id")))
                    params.put("student_id",preferences.getString("student_id"));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Remarks.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void saveClass()
    {
        try {
            dataContext.remarkObjectSet.fill();
            if (list.size() > 0) {

                for (int i = 0; i < dataContext.remarkObjectSet.size(); i++)
                    dataContext.remarkObjectSet.get(i).setStatus(Entity.STATUS_DELETED);

                dataContext.remarkObjectSet.save();
                dataContext.remarkObjectSet.addAll(list);
                dataContext.remarkObjectSet.save();
            }
        } catch (Exception ex) {
            ExceptionsHelper.manage(Remarks.this, ex);
        }
    }
}

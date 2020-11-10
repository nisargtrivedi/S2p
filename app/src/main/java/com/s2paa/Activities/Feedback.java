package com.s2paa.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Adapters.ClassActivityAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Listener.RecycleViewItemClick;
import com.s2paa.Model.ClassActivity;
import com.s2paa.R;
import com.s2paa.Utils.AppLogger;
import com.s2paa.Utils.EEditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisarg on 12/18/2017.
 */

@EActivity(R.layout.feedback)
public class Feedback extends BaseActivity{

    @ViewById
    EEditText txt_feedback;


    @ViewById
    AppCompatButton btnsave;

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;
    @AfterViews
    public void init()
    {
        load();
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Feedback");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
    @Click
    public void btnsave()
    {
        if(!TextUtils.isEmpty(txt_feedback.getText().toString().trim())){
            _feedback();
            finish();
        }
    }
    public void _feedback()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                SMS.FEED_BACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse
                            (String response) {
                        AppLogger.info(response);
                        try {
                            JSONObject ob = new JSONObject(response);
                           // JSONArray object=ob.getJSONArray("Response");
                            Toast.makeText(Feedback.this,ob.getJSONObject("Response").getString("msg").toString(),Toast.LENGTH_LONG).show();
                            Log.i("RESPONSE----->",response);

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(Feedback.this, "error from server", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_type", preferences.getString("login_type"));
                if(preferences.getString("login_type").equalsIgnoreCase("student")) {
                    params.put("user_id", preferences.getString("student_id"));
                }
                else if(preferences.getString("login_type").equalsIgnoreCase("parent")) {
                    params.put("user_id", preferences.getString("student_id"));
                }
                else if(preferences.getString("login_type").equalsIgnoreCase("teacher")) {
                    params.put("user_id", preferences.getString("teacher_id"));
                }
                params.put("messages", txt_feedback.getText().toString());
                Log.i("PARAM------>",params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}

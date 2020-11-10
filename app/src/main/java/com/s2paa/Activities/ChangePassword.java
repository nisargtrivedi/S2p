package com.s2paa.Activities;

import android.app.ProgressDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.s2paa.R;
import com.s2paa.Utils.EEditText;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/16/2017.
 */

@EActivity(R.layout.change_password)
public class ChangePassword extends BaseActivity{

    @ViewById
    EEditText oldpassword;

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    EEditText newpassword;

    @ViewById
    EEditText confirmpassword;

    @ViewById
    TTextView btndone;

    @AfterViews
    public void init()
    {
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //showActionBack();
        txtActionTitle.setText("Change Password");
    }

    @Click
    public void btndone(){

        if(!TextUtils.isEmpty(newpassword.getText().toString()) && !TextUtils.isEmpty(oldpassword.getText().toString())) {
            if (newpassword.getText().toString().equalsIgnoreCase(confirmpassword.getText().toString()))
                LoadData();
            else
                Toast.makeText(ChangePassword.this, "Password don't match", Toast.LENGTH_LONG).show();
        }else{
            newpassword.setError("enter proper data");
            oldpassword.setError("enter proper data");
        }
    }
    public void LoadData() {

            final ProgressDialog pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS.CHANGE_PASSWORD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse
                                (String response) {
                            Log.i("response",response);

                            pDialog.hide();
                            pDialog.dismiss();
                            try {
                                JSONObject object = new JSONObject(response);
                                //  JSONArray object=new JSONArray(response);

                                Toast.makeText(getApplicationContext(), object.getJSONObject("Response").getString("status").toString(), Toast.LENGTH_LONG).show();
                                if (object.getJSONObject("Response").getString("status").toString().equalsIgnoreCase("Password Updated"))
                                    finish();


                            } catch (JSONException e) {
                                pDialog.hide();
                                pDialog.dismiss();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.hide();
                            pDialog.dismiss();
                            Toast.makeText(ChangePassword.this, "error from server", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", preferences.getString("student_id").toString());
                    params.put("user_type", preferences.getString("login_type").toString());
                    params.put("old_password", oldpassword.getText().toString().trim());
                    params.put("new_password", newpassword.getText().toString().trim());
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

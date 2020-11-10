package com.s2paa.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.s2paa.Adapters.GallaryAdapter2;
import com.s2paa.Adapters.GallaryMainAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Model.GallaryObjects;
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

@EActivity(R.layout.gallery_main)
public class GallaryMainPage extends BaseActivity {

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;
    @ViewById
    RecyclerView gallary;

    GallaryMainAdapter adapter;
    List<GallaryObjects> list=new ArrayList<>();

    @AfterViews
    public void init()
    {
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Gallary");
        LoadDAta();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }

    public void LoadDAta()
    {
        final ProgressDialog pDialog = new ProgressDialog(GallaryMainPage.this);
        pDialog.setMessage("Loading.....");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                SMS.GALLARY,
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
                            list = Arrays.asList(mGson.fromJson(object.getJSONArray("Response")+"", GallaryObjects[].class));
                            adapter = new GallaryMainAdapter(GallaryMainPage.this, list);
                            gallary.setAdapter(adapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            gallary.setLayoutManager(layoutManager);
                            gallary.setItemAnimator(new DefaultItemAnimator());

                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(GallaryMainPage.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(GallaryMainPage.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(GallaryMainPage.this);
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

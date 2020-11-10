package com.s2paa.Activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobandme.ada.Entity;
import com.s2paa.Adapters.EventAdapter;
import com.s2paa.Adapters.GallaryAdapter;
import com.s2paa.Adapters.GallaryAdapter2;
import com.s2paa.Adapters.GallaryVideoAdapter;
import com.s2paa.Adapters.GallayViewPager;
import com.s2paa.Application.SMS;
import com.s2paa.Model.EventObjects;
import com.s2paa.Model.GallaryObjects;
import com.s2paa.Model.Multiple_Gallary;
import com.s2paa.R;
import com.s2paa.Utils.ExceptionsHelper;
import com.s2paa.Utils.Messages;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@EActivity(R.layout.gallary)
public class Gallery extends BaseActivity {

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    TextView images;

    @ViewById
    TextView video;

    @ViewById
    GridView rv_event;
    @ViewById
    GridView rv_event2;

    List<GallaryObjects> list=new ArrayList<>();
    List<Multiple_Gallary> list2=new ArrayList<>();
    GallaryAdapter2 eventAdapter;
    GallaryVideoAdapter videoAdapter;
    GallayViewPager gallayViewPager;

    @AfterViews
    public void init()
    {
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Gallery");
        LoadEvents();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        images.setBackgroundColor(getResources().getColor(R.color.APP_COLOR));
        images.setTextColor(getResources().getColor(R.color.white));
    }

    @Click
    public void images()
    {
        images.setBackgroundColor(getResources().getColor(R.color.APP_COLOR));
        images.setTextColor(getResources().getColor(R.color.white));
        video.setBackgroundResource(R.drawable.border);
        video.setTextColor(getResources().getColor(R.color.black));
        rv_event2.setVisibility(View.GONE);
        rv_event.setVisibility(View.VISIBLE);
        eventAdapter = new GallaryAdapter2(Gallery.this, list);
        rv_event.setAdapter(eventAdapter);
    }
    @Click
    public void video()
    {
        video.setBackgroundColor(getResources().getColor(R.color.APP_COLOR));
        video.setTextColor(getResources().getColor(R.color.white));
        images.setBackgroundResource(R.drawable.border);
        images.setTextColor(getResources().getColor(R.color.black));
        rv_event.setVisibility(View.GONE);
        rv_event2.setVisibility(View.VISIBLE);
           list2=eventAdapter.videoGalleries;
           videoAdapter=new GallaryVideoAdapter(this,list2);
           rv_event2.setAdapter(videoAdapter);
        //Toast.makeText(this,list2.size()+"", Toast.LENGTH_LONG).show();
    }
    public void LoadEvents()
    {
        final ProgressDialog pDialog = new ProgressDialog(Gallery.this);
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
                            rv_event2.setVisibility(View.GONE);
                            rv_event.setVisibility(View.VISIBLE);
                            eventAdapter = new GallaryAdapter2(Gallery.this, list);
                            rv_event.setAdapter(eventAdapter);
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Gallery.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Gallery.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                if(preferences.getString("login_type").toString().equalsIgnoreCase("student"))
//                params.put("user_id",preferences.getString("student_id"));
//                else if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher"))
//                    params.put("user_id",preferences.getString("teacher_id"));
//                else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
//                    params.put("user_id",preferences.getString("parent_id"));
//
//                params.put("user_type",preferences.getString("login_type"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Gallery.this);
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

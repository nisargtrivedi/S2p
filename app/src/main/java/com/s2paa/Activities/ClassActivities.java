package com.s2paa.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Adapters.ClassActivityAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Listener.RecycleViewItemClick;
import com.s2paa.Model.ClassActivity;
import com.s2paa.Model.Division;
import com.s2paa.Model.Standard;
import com.s2paa.R;
import com.s2paa.Utils.AppLogger;
import com.s2paa.Utils.ExceptionsHelper;
import com.s2paa.Utils.KeyBoardHandling;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/3/2017.
 */

@EActivity(R.layout.class_activity)

public class ClassActivities extends BaseActivity {

    @ViewById
    RecyclerView class_activity;

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    TTextView itemAdd;

    @ViewById
    LinearLayout teacher_menu;

    @ViewById
            TTextView classs;
    @ViewById
            TTextView section;

    ClassActivityAdapter adapter;
    ArrayList<ClassActivity> list=new ArrayList<>();

    int class_id;
    int sec_id;


    @AfterViews
    public void init()
    {
        load();
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Class Activity");
        //itemAdd.setVisibility(View.VISIBLE);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher")){
            itemAdd.setVisibility(View.VISIBLE);
            teacher_menu.setVisibility(View.VISIBLE);
        }else{
            itemAdd.setVisibility(View.GONE);
            teacher_menu.setVisibility(View.GONE);
            LoadData();
        }
    }

    @Click
    public void section(){
        startActivityForResult(new Intent(getApplicationContext(), DivisionAct_.class).putExtra("class_id",class_id+""), 101);
    }
    @Click
    public void classs(){

        startActivityForResult(new Intent(getApplicationContext(), StandardAct_.class), 100);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        LoadData();
    }

    public void LoadData()
    {

        final ProgressDialog pDialog = new ProgressDialog(ClassActivities.this);
        pDialog.setMessage("Loading Class Activities...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                SMS.CLASS_ACTIVITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse
                            (String response) {
                        AppLogger.info(response);
                        pDialog.hide();
                        pDialog.dismiss();
                        list.clear();
                        try {
                            JSONObject ob = new JSONObject(response);
                            JSONArray object=ob.getJSONArray("Response");
                            Log.i("RESPONSE----->",response);
                            for(int i=0;i<object.length();i++) {
                                ClassActivity classActivity = new ClassActivity();
                                classActivity.class_activity_id = object.getJSONObject(i).getString("class_activity_id");
                                classActivity.title = object.getJSONObject(i).getString("title");
                                classActivity.details = object.getJSONObject(i).getString("details");
                                classActivity.class_id = object.getJSONObject(i).getString("class_id");

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                //SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                try {
                                    classActivity.create_at =  sdf.parse(object.getJSONObject(i).getString("create_at"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                classActivity.status = object.getJSONObject(i).getString("status");
                                classActivity.class_name = object.getJSONObject(i).getString("class_name");
                                list.add(classActivity);
                            }
                            saveClass();
                            try {
                                dataContext.classSet.fill("create_at desc");
                            } catch (AdaFrameworkException e) {
                                e.printStackTrace();
                            }
                            adapter=new ClassActivityAdapter(ClassActivities.this,dataContext.classSet);
                            class_activity.setAdapter(adapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            class_activity.setLayoutManager(layoutManager);
                            class_activity.setItemAnimator(new DefaultItemAnimator());
                            adapter.setOnCatClickListener(new RecycleViewItemClick() {
                                @Override
                                public void onitemClick(ClassActivity classActivity) {
                                    Intent intent=new Intent(ClassActivities.this,DetailsActivity_.class);
                                    intent.putExtra("Class", classActivity);
                                    startActivity(intent);
                                }
                            });

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

                        Toast.makeText(ClassActivities.this, "error from server", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(preferences.getString("login_type").equalsIgnoreCase("student")) {
                    params.put("section_id", preferences.getString("section_id"));
                    params.put("class_id", preferences.getString("class_id"));
                }else if(preferences.getString("login_type").equalsIgnoreCase("teacher")){
                    params.put("section_id", sec_id+"");
                    params.put("class_id", class_id+"");
                }else if( preferences.getString("login_type").equalsIgnoreCase("parent"))
                {
                    params.put("section_id", preferences.getString("section_id"));
                    params.put("class_id", preferences.getString("class_id"));
                }
                Log.i("PARAM",params.toString());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            try {
                KeyBoardHandling.hideSoftKeyboard(this);
                Standard schoolItem = null;
                if (data != null && data.getExtras() != null) {
                    schoolItem = (Standard) data.getExtras().getSerializable("STANDARD");
                }
                if (schoolItem == null) {
                    return;
                }
                ((TTextView) findViewById(R.id.classs)).setText(schoolItem.name);
                class_id=Integer.parseInt(schoolItem.class_id);
            } catch (Exception ex) {
                ExceptionsHelper.manage(ex);
            }
        }
        else if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            try {
                KeyBoardHandling.hideSoftKeyboard(this);
                Division schoolItem = null;
                if (data != null && data.getExtras() != null) {
                    schoolItem = (Division) data.getExtras().getSerializable("DIVISION");

                }
                if (schoolItem == null) {
                    return;
                }
                ((TTextView) findViewById(R.id.section)).setText(schoolItem.name);
                sec_id=Integer.parseInt(schoolItem.section_id);
                LoadData();
            } catch (Exception ex) {
                ExceptionsHelper.manage(ex);
            }
        }
        //initData();
    }
    @Click
    public void itemAdd()

    {
        startActivity(new Intent(ClassActivities.this,AddTeacherClassActivity_.class));
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//       getMenuInflater().inflate(R.menu.menu_add, menu);
//        MenuItem item=menu.findItem(R.id.itemAdd);
////        if(!TextUtils.isEmpty(preferences.getString("login_type")))
////        {
////            if(preferences.getString("login_type").trim().toString().equalsIgnoreCase("teacher")){
////                Toast.makeText(this,preferences.getString("login_type").trim().toString(),Toast.LENGTH_LONG).show();
////                item.setVisible(true);
////            }else{
////                item.setVisible(false);
////            }
////        }
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {

         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.menu_add, menu);
         return true;
         }

         @Override
        public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
         int id = item.getItemId();
         if (id == R.id.itemAdd) {
             return true;
             }
         return super.onOptionsItemSelected(item);
         }


         public void saveClass()
         {
             try {
                 dataContext.classSet.fill();
                 if (list.size() > 0) {

                     for (int i = 0; i < dataContext.classSet.size(); i++)
                         dataContext.classSet.get(i).setStatus(Entity.STATUS_DELETED);

                     dataContext.classSet.save();
                     dataContext.classSet.addAll(list);
                     dataContext.classSet.save();
                 }
             } catch (Exception ex) {
                 ExceptionsHelper.manage(ClassActivities.this, ex);
             }
         }
}


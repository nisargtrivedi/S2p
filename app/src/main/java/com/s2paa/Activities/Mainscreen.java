package com.s2paa.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
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
import com.bumptech.glide.Glide;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Application.SMS;
import com.s2paa.Fragments.Dashboard;
import com.s2paa.Fragments.Dashboard_;
import com.s2paa.Model.Division;
import com.s2paa.Model.ExamList;
import com.s2paa.Model.Exam_Schedule;
import com.s2paa.Model.Standard;
import com.s2paa.R;
import com.s2paa.Utils.AppLogger;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.ExceptionsHelper;
import com.s2paa.Utils.Messages;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.drawer_main)
public class Mainscreen extends BaseActivity {


    @ViewById
    DrawerLayout drawer_layout;

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView txtActionTitle;

    @ViewById
    TTextView name;

    @ViewById
    TTextView email;

    @ViewById
    de.hdodenhof.circleimageview.CircleImageView img;
    @ViewById
    de.hdodenhof.circleimageview.CircleImageView img_2;

    ArrayList<ExamList> list = new ArrayList<>();
    ArrayList<ExamList> examListArrayList = new ArrayList<>();
    ArrayList<Exam_Schedule> exam_scheduleArrayList = new ArrayList<>();

    //    @ViewById
//    de.hdodenhof.circleimageview.CircleImageView img_3;
    @ViewById
    LinearLayout logout;

    @ViewById
    LinearLayout leave_list;

    @ViewById
    LinearLayout change_password;

    @ViewById
    LinearLayout chat;

    @ViewById
    LinearLayout fees;

    @ViewById
    LinearLayout rating;


    AppPreferences preferences;
    boolean mSlideState = false;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ArrayList<Standard> standardArrayList = new ArrayList<>();
    ArrayList<Division> divisionArrayList = new ArrayList<>();
    int i = 1;

    @AfterViews
    public void init() {
        load();
        setSupportActionBar(toolbar);
        //Glide.with(this).load(preferences.getString("img").toString()).into(img);
        getSupportActionBar().setTitle("");
        txtActionTitle.setText("Dashboard");
        preferences = new AppPreferences(Mainscreen.this);
        if (preferences.getString("login_type").equalsIgnoreCase("parent")) {
            try {
                dataContext.parentStudentObjectSet.fill();

            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            name.setText(dataContext.parentStudentObjectSet.get(0).name + " " + dataContext.parentStudentObjectSet.get(0).last_name);
            email.setText(preferences.getString("stu_email"));

            Glide.with(this).load(dataContext.parentStudentObjectSet.get(0).profile_img).into(img);
            if (dataContext.parentStudentObjectSet.size() > 1) {
                img_2.setVisibility(View.VISIBLE);
                Glide.with(this).load(dataContext.parentStudentObjectSet.get(1).profile_img).into(img_2);
            }
            preferences.set("student_id", dataContext.parentStudentObjectSet.get(0).student_id);
            preferences.set("class_id", dataContext.parentStudentObjectSet.get(0).class_id);
            preferences.set("section_id", dataContext.parentStudentObjectSet.get(0).section_id);
            preferences.set("parent_id", dataContext.parentStudentObjectSet.get(0).parent_id);
        }
        if (preferences.getString("login_type").equalsIgnoreCase("student") || preferences.getString("login_type").equalsIgnoreCase("teacher")) {
            if (!TextUtils.isEmpty(preferences.getString("name")) && !TextUtils.isEmpty(preferences.getString("stu_email"))) {
                name.setText(preferences.getString("name"));
                email.setText(preferences.getString("stu_email"));
                Glide.with(this).load(preferences.getString("img").toString()).into(img);
            }
        }
        if (preferences.getString("login_type").equalsIgnoreCase("teacher")) {
            fees.setVisibility(View.INVISIBLE);
        }
        setDrawer();
        loadMainFragement();
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.i("DEVICE ID", tm.getDeviceId());
        Log.i("TOKEN ID", preferences.getString("regId"));
        AppLogger.info("DEVICE ID----------------"+tm.getDeviceId());
        getClassData();
        getExams();
    }

    @Click
    public void rating(){
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.s2pp");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this,"error",Toast.LENGTH_LONG).show();
            //UtilityClass.showAlertDialog(context, ERROR, "Couldn't launch the market", null, 0);
        }
    }

    @Click
    public void feedback(){
        startActivity(new Intent(Mainscreen.this, Feedback_.class));
    }
    @Click
    public void calendar_data()
    {

    }
    @Click
    public void img() {
        startActivity(new Intent(Mainscreen.this, Profile_.class));
    }
    @Click
    public void img_2() {
        i++;
        if (i == 1) {
            Glide.with(this).load(dataContext.parentStudentObjectSet.get(0).profile_img).into(img);
            img_2.setVisibility(View.VISIBLE);
            Glide.with(this).load(dataContext.parentStudentObjectSet.get(1).profile_img).into(img_2);

            preferences.set("student_id", dataContext.parentStudentObjectSet.get(0).student_id);
            preferences.set("class_id", dataContext.parentStudentObjectSet.get(0).class_id);
            preferences.set("section_id", dataContext.parentStudentObjectSet.get(0).section_id);
            preferences.set("parent_id", dataContext.parentStudentObjectSet.get(0).parent_id);
            Dashboard fragment = Dashboard.newInstance("0");
            replaceFragment(fragment,"Dashboard3");
        }
        else if(i==2){
            Glide.with(this).load(dataContext.parentStudentObjectSet.get(1).profile_img).into(img);
            img_2.setVisibility(View.VISIBLE);
            Glide.with(this).load(dataContext.parentStudentObjectSet.get(2).profile_img).into(img_2);

            preferences.set("student_id", dataContext.parentStudentObjectSet.get(1).student_id);
            preferences.set("class_id", dataContext.parentStudentObjectSet.get(1).class_id);
            preferences.set("section_id", dataContext.parentStudentObjectSet.get(1).section_id);
            preferences.set("parent_id", dataContext.parentStudentObjectSet.get(1).parent_id);
            Dashboard fragment = Dashboard.newInstance("1");
            replaceFragment(fragment,"Dashboard2");
        }
        else if(i==3){
            Glide.with(this).load(dataContext.parentStudentObjectSet.get(2).profile_img).into(img);
            img_2.setVisibility(View.VISIBLE);
            Glide.with(this).load(dataContext.parentStudentObjectSet.get(0).profile_img).into(img_2);

            preferences.set("student_id", dataContext.parentStudentObjectSet.get(2).student_id);
            preferences.set("class_id", dataContext.parentStudentObjectSet.get(2).class_id);
            preferences.set("section_id", dataContext.parentStudentObjectSet.get(2).section_id);
            preferences.set("parent_id", dataContext.parentStudentObjectSet.get(2).parent_id);
            i=0;
            Dashboard fragment = Dashboard.newInstance("2");
           replaceFragment(fragment,"Dashboard1");
        }

//    @Click
//    public void img_3()
//    {
//        Glide.with(this).load(dataContext.parentStudentObjectSet.get(2).profile_img).into(img);
//        if(dataContext.parentStudentObjectSet.size()>1) {
//            img_2.setVisibility(View.VISIBLE);
//            Glide.with(this).load(dataContext.parentStudentObjectSet.get(0).profile_img).into(img_2);
//        }
//        if(dataContext.parentStudentObjectSet.size()>2) {
//            img_3.setVisibility(View.VISIBLE);
//            Glide.with(this).load(dataContext.parentStudentObjectSet.get(1).profile_img).into(img_3);
//        }
//        preferences.set("student_id",dataContext.parentStudentObjectSet.get(2).student_id);
//        preferences.set("class_id",dataContext.parentStudentObjectSet.get(2).class_id);
//        preferences.set("section_id",dataContext.parentStudentObjectSet.get(2).section_id);
//        preferences.set("parent_id",dataContext.parentStudentObjectSet.get(2).parent_id);
//    }
    }
    void setDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSlideState = true;//is Closed

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideState = false;//is Closed
            }
        };
        drawer_layout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        drawer_layout.setDrawerShadow(android.R.color.white, GravityCompat.START);
    }

    public void loadMainFragement() {
        Fragment fragment = new Dashboard_();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack("Dashboard").commit();

    }

    public void replaceFragment(Fragment fragment, String tag) {

        try {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment, tag).commit();

        } catch (Exception e) {
            Log.d("TAG", e.toString());
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Click
    public void logout() {
        preferences.set(preferences.IS_LOGGED_IN, false);
        preferences.set("name", "");
        preferences.set("login_type","");
        preferences.set("student_id","");
        preferences.set("teacher_id","");
        preferences.set("parent_id","");
        startActivity(new Intent(Mainscreen.this, MainPage_.class));
        finish();
    }

    @Click
    public void fees()
    {
        startActivity(new Intent(Mainscreen.this,FeesList_.class));
    }
    @Click
    public void change_password() {
        startActivity(new Intent(Mainscreen.this, ChangePassword_.class));
    }

    @Click
    public void leave_list() {
        if (preferences.getString("login_type").toString().equalsIgnoreCase("student"))
            startActivity(new Intent(Mainscreen.this, LeaveList_.class));
        else if (preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
            startActivity(new Intent(Mainscreen.this, AddParentLeave_.class));
        else if (preferences.getString("login_type").toString().equalsIgnoreCase("teacher"))
            startActivity(new Intent(Mainscreen.this, LeaveList_.class));
    }

    @Click
    public void chat()
    {
        startActivity(new Intent(Mainscreen.this, ChatList_.class));
    }
    @Override
    public void onBackPressed() {

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            //  Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_LONG).show(); // write your code to switch between fragments.
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit from this App?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Mainscreen.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            getSupportFragmentManager().popBackStack();
        }
//        }else{
//            super.onBackPressed();
//        }


        drawer_layout.closeDrawers();
    }

    public void getClassData() {
        final ProgressDialog pDialog = new ProgressDialog(Mainscreen.this);
        pDialog.setMessage("Loading Class Details...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SMS.STANDARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        //list.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response Class Data---", response);
                            Standard standard = new Standard();
                            standardArrayList = standard.getStandardDetails(object.getJSONArray("Response"));
                            divisionArrayList = standard.divisionArrayList;
                                addDataToDB();
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Mainscreen.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Mainscreen.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Mainscreen.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void addDataToDB() {

        try {
            dataContext.standardObjectSet.fill();
            if (standardArrayList.size() > 0) {

                for (int i = 0; i < dataContext.standardObjectSet.size(); i++)
                    dataContext.standardObjectSet.get(i).setStatus(Entity.STATUS_DELETED);

                dataContext.standardObjectSet.save();
                dataContext.standardObjectSet.addAll(standardArrayList);
                dataContext.standardObjectSet.save();
            }
        } catch (Exception ex) {
            ExceptionsHelper.manage(Mainscreen.this, ex);
        }

        try {

            if (divisionArrayList.size() > 0) {

                dataContext.divisionObjectSet.fill();
                for (int i = 0; i < dataContext.divisionObjectSet.size(); i++)
                    dataContext.divisionObjectSet.get(i).setStatus(Entity.STATUS_DELETED);

                dataContext.divisionObjectSet.save();

                dataContext.divisionObjectSet.addAll(divisionArrayList);
                dataContext.divisionObjectSet.save();
            }
        } catch (Exception ex) {
            ExceptionsHelper.manage(Mainscreen.this, ex);
        }

    }

    public void getExams() {
        final ProgressDialog pDialog = new ProgressDialog(Mainscreen.this);
        pDialog.setMessage("Loading Details...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                SMS.EXAM_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        //list.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            ExamList standard = new ExamList();
                            examListArrayList = standard.ReponseData(object.getJSONArray("Response"));
                            exam_scheduleArrayList=standard.list;
                            Log.i("EXAM_SCHEDULE_SIZE---",exam_scheduleArrayList.size()+"");
                            addData();

                            Log.i("Response-----", object.getJSONArray("Response") + "" + examListArrayList.size());
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
//                            if (!e.getMessage().trim().equals(""))
//                                Messages.ShowMessage(Mainscreen.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Mainscreen.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Mainscreen.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Background
    public void addData() {
        try {
            dataContext.examListObjectSet.fill();
            dataContext.examScheduleObjectSet.fill();
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }

        if (examListArrayList.size() > 0) {

            try {
                dataContext.examListObjectSet.fill();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < dataContext.examListObjectSet.size(); i++)
                dataContext.examListObjectSet.get(i).setStatus(Entity.STATUS_DELETED);

            try {
                dataContext.examListObjectSet.save();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            dataContext.examListObjectSet.addAll(examListArrayList);
            try {
                dataContext.examListObjectSet.save();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
        }
        if(exam_scheduleArrayList.size()>0){
            try {
                dataContext.examScheduleObjectSet.fill();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < dataContext.examScheduleObjectSet.size(); i++)
                dataContext.examScheduleObjectSet.get(i).setStatus(Entity.STATUS_DELETED);

            try {
                dataContext.examScheduleObjectSet.save();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            dataContext.examScheduleObjectSet.addAll(exam_scheduleArrayList);
            try {
                dataContext.examScheduleObjectSet.save();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
        }
    }



}

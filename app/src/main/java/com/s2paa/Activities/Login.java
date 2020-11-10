package com.s2paa.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
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
import com.s2paa.Application.SMS;
import com.s2paa.Model.DataContext;
import com.s2paa.Model.ParentStudent;
import com.s2paa.Model.User;
import com.s2paa.Model.subUserDetails;
import com.s2paa.R;
import com.s2paa.Utils.AppLogger;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.ExceptionsHelper;
import com.s2paa.Utils.Messages;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@EActivity(R.layout.activity_main)
public class Login extends Activity {

    @ViewById
    AppCompatEditText txt_username;

    @ViewById
    AppCompatEditText txt_password;

    @ViewById
    TTextView msg;

    @ViewById
    AppCompatButton parents;

    @ViewById
    AppCompatButton teacher;

    @ViewById
    AppCompatButton student;

    @ViewById
    LinearLayout main_layout;

    @ViewById
    LinearLayout ll;

    AppPreferences preferences;
    RequestQueue requestQueue;
    ArrayList<subUserDetails> list = new ArrayList<>();
    List<ParentStudent> parentStudentList = new ArrayList<>();

    String user_type = "";
    DataContext dataContext;

    int school;
    @AfterViews
    public void init() {
        dataContext = new DataContext(Login.this);
        preferences = new AppPreferences(Login.this);
        requestQueue = Volley.newRequestQueue(Login.this);

        if (!TextUtils.isEmpty(preferences.getString("name"))) {
            startActivity(new Intent(Login.this, Mainscreen_.class));
            finish();
        }
        school=getIntent().getIntExtra("school",0);
       if(school!=1){
           ll.setVisibility(View.INVISIBLE);
       }
        //Toast.makeText(Login.this,SMS.ENDPOINT,Toast.LENGTH_LONG).show();
//        txt_username.setText("student@student.com");
//        txt_password.setText("student");
//
//        txt_username.setText("parent@parent.com");
//        txt_password.setText("parent");

//        txt_username.setText("teacher@teacher.com");
//        txt_password.setText("teacher");

        txt_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= txt_password.getRight() - txt_password.getTotalPaddingRight()) {
                        //Toast.makeText(getApplicationContext(),"Clicked...",Toast.LENGTH_LONG).show();
                        openImagePopup();
                    }
                }
                return false;
            }
        });
    }

    @Click
    protected void parents() {
        select(parents, true);
        de_select(student, false);
        de_select(teacher, false);
        msg.setText("Hello Parent!\n Please login to get start");
        if(school==1) {
            txt_username.setText("parent@parent.com");
            txt_password.setText("parent");
        }
    }

    @Click
    protected void teacher() {
        select(teacher, true);
        de_select(parents, false);
        de_select(student, false);
        msg.setText("Hello Teacher!\n Please login to get start");
        if(school==1) {
            txt_username.setText("teacher@teacher.com");
            txt_password.setText("teacher");
        }
    }

    @Click
    protected void student() {
        select(student, true);
        de_select(parents, false);
        de_select(teacher, false);
        msg.setText("Hello Student!\n Please login to get start");
        if (school == 1){
            txt_username.setText("student@student.com");
        txt_password.setText("student");
        }

    }

    public void select(AppCompatButton textView, boolean ans) {
        if (ans == true && textView == parents) {
            textView.setBackgroundResource(R.drawable.btn_selector);
            // textView.setTextColor(getResources().getColor(R.color.white));
            textView.setSelected(true);
        }

        if (ans == true && textView == teacher) {
            textView.setBackgroundResource(R.drawable.teacher_btn_selector);
            // textView.setTextColor(getResources().getColor(R.color.white));
            textView.setSelected(true);
        }

        if (ans == true && textView == student) {
            textView.setBackgroundResource(R.drawable.student_btn_selector);
            // textView.setTextColor(getResources().getColor(R.color.white));
            textView.setSelected(true);
        }

    }

    public void de_select(AppCompatButton textView, boolean ans) {
        textView.setSelected(false);
    }

    @Click
    protected void btnclick() {
        if (TextUtils.isEmpty(txt_username.getText().toString().trim()))
            txt_username.setError("please enter username");

        if (TextUtils.isEmpty(txt_password.getText().toString().trim()))
            txt_password.setError("please enter password");

        if (!TextUtils.isEmpty(txt_username.getText().toString().trim()) && !TextUtils.isEmpty(txt_password.getText().toString().trim())) {
            if (Patterns.EMAIL_ADDRESS.matcher(txt_username.getText().toString()).matches() == true) {
                // if (teacher.isSelected() == true | parents.isSelected() == true | student.isSelected() == true) {
                //LOGIN();
                if(school!=1)
                {
                    user_type = "parent";
                }else {
                    if (teacher.isSelected() == true) {
                        user_type = "teacher";
                    } else if (parents.isSelected() == true) {
                        user_type = "parent";
                    } else if (student.isSelected() == true) {
                        user_type = "student";
                    }
                }

                LOGIN_API(txt_username.getText().toString().trim(), txt_password.getText().toString().trim(), preferences.getString("regId"), user_type);
            }
            //} else
            //  Snackbar.make(main_layout, "please select login option student,teacher,parent", Snackbar.LENGTH_LONG).show();

            else
                txt_username.setError("please enter correct email address");
        }
    }

    private void LOGIN_API(final String email, final String password, final String authenticate, final String user) {
        final ProgressDialog pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            //AppLogger.info("Response--------" + response);
                            Log.i("Response--------", response);
                            User user = new User();
                            user.StoreData(object);
//                            if (user.status.equalsIgnoreCase("Wrong Username or Password")) {
//                                Toast.makeText(Login.this,user.status,Toast.LENGTH_LONG).show();
//                            } else {
                            if (user.isValid(user) == true) {

                                if (!TextUtils.isEmpty(user.name) && !TextUtils.isEmpty(user.email) && !TextUtils.isEmpty(user.profile_img) && !TextUtils.isEmpty(user.login_type)) {
                                    preferences.set("name", user.name);
                                    preferences.set("stu_email", user.email);
                                    preferences.set("img", user.profile_img);
                                    preferences.set("login_type", user.login_type);
                                    if(preferences.getString("login_type").equalsIgnoreCase("parent"))
                                    {
                                        preferences.set("mother_imge", user.mother_image);
                                        preferences.set("email", user.email);
                                        preferences.set("roll", object.getJSONObject("Response").getJSONArray("subUserDetails").getJSONObject(0).getString("roll").toString());
                                    }
                                    preferences.set("address", user.address);
                                    preferences.set("rollno", user.rollno);
                                    preferences.set("class", user.class_id);
                                    preferences.set("section", user.section_id);
                                    preferences.set("phone", user.phone);
                                    preferences.set("grno", user.gr_no);
                                    preferences.set("class_name", user.class_name);
                                    preferences.set("student_id", user.student_id);
                                    preferences.set("section_name", user.section_name);
                                    preferences.set(preferences.IS_LOGGED_IN, true);
                                    if (!TextUtils.isEmpty(user.teacher_id)) {
                                        preferences.set("teacher_id", user.teacher_id);
                                    }
                                    if (preferences.getString("login_type").equalsIgnoreCase("parent")) {
                                        GsonBuilder builder = new GsonBuilder();
                                        Gson mGson = builder.create();
                                        parentStudentList = Arrays.asList(mGson.fromJson(object.getJSONObject("Response").getJSONArray("subUserDetails") + "", ParentStudent[].class));
                                        addDataToDB(parentStudentList);
                                        Log.i("PA_STUDENT->", object.getJSONObject("Response").getJSONArray("subUserDetails").toString()+"");
                                    } else {
                                        list = user.StudentParents(object.getJSONObject("Response").getJSONArray("subUserDetails"));
                                    }
                                    if (list.size() > 0) {
                                        preferences.set("mother_name", list.get(0).mother_name);
                                        preferences.set("father_name", list.get(0).name);
                                        preferences.set("parent_email", list.get(0).email);
                                        preferences.set("house_phone", list.get(0).phone);
                                        preferences.set("mother_img", list.get(0).mother_img);
                                        preferences.set("father_img", list.get(0).profile_img);
                                        //Toast.makeText(Login.this,list.get(0).name+"",Toast.LENGTH_LONG).show();
                                        //

                                    }
                                    if (parentStudentList.size() > 0) {
                                        Log.i("in data->", object.getJSONObject("Response").getJSONArray("subUserDetails").toString()+"");
                                        preferences.set("name", parentStudentList.get(0).name + parentStudentList.get(0).last_name );
                                        preferences.set("mother_name", parentStudentList.get(0).mother_name);
                                        preferences.set("father_name", parentStudentList.get(0).father_name);
                                        preferences.set("parent_email", parentStudentList.get(0).email);
                                        preferences.set("class_name", parentStudentList.get(0).class_name);
                                        preferences.set("section_name", parentStudentList.get(0).section_name);
                                        preferences.set("house_phone", parentStudentList.get(0).phone);
                                        preferences.set("address", parentStudentList.get(0).address);
                                        preferences.set("mother_img", parentStudentList.get(0).mother_img);
                                        preferences.set("father_img", parentStudentList.get(0).profile_img);
                                        preferences.set("profile_imggg", parentStudentList.get(0).profile_img);

                                        //Toast.makeText(Login.this,list.get(0).name+"",Toast.LENGTH_LONG).show();
                                        //

                                    }


                                    startActivity(new Intent(Login.this, Mainscreen_.class));
                                    finish();

                                } else {
                                    Toast.makeText(Login.this, "please contact admin for login details", Toast.LENGTH_LONG).show();

                                }
                            } else if (user.isValid(user) == false) {
                                Toast.makeText(Login.this, user.status.toString(), Toast.LENGTH_LONG).show();
                            }

                        }
                        //}
                        catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Login.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Login.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                AppLogger.info("email--------"+email);
//                AppLogger.info("password--------"+password);
//                AppLogger.info("gcm_id--------"+authenticate);
//                AppLogger.info("user_type--------"+user_type);
                params.put("email", email);
                params.put("password", password);
                params.put("gcm_id", preferences.getString("regId"));
                params.put("user_type", user_type);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

      //  Toast.makeText(Login.this,SMS.ENDPOINT+"",Toast.LENGTH_LONG).show();
        Log.i("DATA_____",SMS.LOGIN);
    }

    private void GCM_API(final String user_id, final String user_type, final String gcm_id, final String type) {
        final ProgressDialog pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS.GCM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response--------", response);
                            User user = new User();
                            user.StoreData(object);
                        }
                        //}
                        catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Login.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Login.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("user_type", user_type);
                params.put("gcm_id", gcm_id);
                params.put("type", "android");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

        //  Toast.makeText(Login.this,SMS.ENDPOINT+"",Toast.LENGTH_LONG).show();
        Log.i("DATA_____",SMS.LOGIN);
    }


    private void openImagePopup() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.popup, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        final AppCompatEditText email = (AppCompatEditText) view.findViewById(R.id.email);
        final AppCompatButton go = (AppCompatButton) view.findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(email.getText().toString().trim())) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() == false) {
                        email.requestFocus();
                        email.setError("enter proper email address");
                        return;
                    } else {
                        FORGOT_API(email.getText().toString().trim());

                    }
                }
                alertDialog.hide();
                alertDialog.dismiss();

            }
        });
//        Picasso.with(getActivity()).load(str).transform(new RoundedCornersTransform(10, RoundedCornersTransform.Corners.ALL)).into(img);
        alertDialog.show();
    }

    private void FORGOT_API(final String email) {
        final ProgressDialog pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS.FORGOT_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            AppLogger.info("Response--------" + response);

                            String msg = object.getJSONObject("Response").getString("status").toString();
                            Messages.ShowMessage(Login.this, msg);

                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Login.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Login.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void addDataToDB(List<ParentStudent> parentStudentList) {

        try {

            if (parentStudentList.size() > 0) {

                dataContext.parentStudentObjectSet.fill();
                for (int i = 0; i < dataContext.parentStudentObjectSet.size(); i++)
                    dataContext.parentStudentObjectSet.get(i).setStatus(Entity.STATUS_DELETED);

                dataContext.parentStudentObjectSet.save();

                dataContext.parentStudentObjectSet.addAll(parentStudentList);
                dataContext.parentStudentObjectSet.save();
            }
        } catch (Exception ex) {
            ExceptionsHelper.manage(Login.this, ex);
        }

    }
}

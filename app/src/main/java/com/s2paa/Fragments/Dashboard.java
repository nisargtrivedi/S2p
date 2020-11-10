package com.s2paa.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Activities.AddRemarks_;
import com.s2paa.Activities.AnnouncementActivity_;
import com.s2paa.Activities.Attandance_;
import com.s2paa.Activities.CalenderActivity;
import com.s2paa.Activities.ClassActivities_;
import com.s2paa.Activities.EventList_;
import com.s2paa.Activities.GallaryMainPage_;
import com.s2paa.Activities.Gallery_;
import com.s2paa.Activities.HomeWork_;
import com.s2paa.Activities.Login;
import com.s2paa.Activities.Mainscreen;
import com.s2paa.Activities.Remarks_;
import com.s2paa.Activities.ResultExamSelection_;
import com.s2paa.Activities.ShowExamList_;
import com.s2paa.Activities.StudentAttendanceCalenderActivity;
import com.s2paa.Activities.Student_Attendance_;
import com.s2paa.Activities.TimeTable_;
import com.s2paa.Adapters.DashboardAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Listener.DashboardItemClickListeners;
import com.s2paa.Model.DashboardItem;
import com.s2paa.Model.DataContext;
import com.s2paa.Model.User;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.Messages;
import com.s2paa.Utils.TTextView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 7/12/2017.
 */

@EFragment(R.layout.dashboard)
public class
Dashboard extends Fragment implements DashboardItemClickListeners {

    @ViewById
    GridView grid_dashboard;
    DashboardAdapter adapter;

    ArrayList<DashboardItem> dashboardItems;

    @ViewById
    TTextView news;


    @ViewById
    CircleImageView img;

    @ViewById
    AppCompatImageView img_back;

    @ViewById
    RelativeLayout rl;

    AppPreferences preferences;

    DataContext dataContext;
    int value;

    public static Dashboard newInstance(String param1) {
        Dashboard fragment = new Dashboard_();
        Bundle args = new Bundle();
        args.putString("value", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public Dashboard() {

    }

    @AfterViews
    public void init() {

        dataContext = new DataContext(getActivity());
        news.setSelected(true);
        String test = "This is demo text to show the data to display the message";
        news.setText(test);
        preferences = new AppPreferences(getActivity());
        if (getArguments() != null) {
            value = Integer.parseInt(getArguments().getString("value"));
        }
        if (preferences.getString("login_type").equalsIgnoreCase("parent")) {
            try {
                dataContext.parentStudentObjectSet.fill();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            Glide.with(this).load(dataContext.parentStudentObjectSet.get(value).profile_img).into(img);
            Picasso.with(getActivity()).
                    load(dataContext.parentStudentObjectSet.get(value).profile_img).fit().
                    into(img_back);
        }
        else if (preferences.getString("login_type").equalsIgnoreCase("teacher") || preferences.getString("login_type").equalsIgnoreCase("student")) {
            if (!TextUtils.isEmpty(preferences.getString("name")) && !TextUtils.isEmpty(preferences.getString("img"))) {
                Glide.with(this).load(preferences.getString("img").toString()).into(img);
                Picasso.with(getActivity()).
                        load(preferences.getString("img").toString()).fit().
                        into(img_back);
            }
        }
        if(preferences.getString("login_type").equalsIgnoreCase("teacher"))
            GCM_API(preferences.getString("teacher_id"),preferences.getString("login_type"), preferences.getString("regId"), "android");
        if(preferences.getString("login_type").equalsIgnoreCase("parent"))
            GCM_API(preferences.getString("student_id"),preferences.getString("login_type"), preferences.getString("regId"), "android");
        if(preferences.getString("login_type").equalsIgnoreCase("student"))
            GCM_API(preferences.getString("student_id"),preferences.getString("login_type"), preferences.getString("regId"), "android");


        dashboardItems = new ArrayList<>();
        dashboardItems.add(new DashboardItem("Time Table", R.drawable.ic_time_table));
        dashboardItems.add(new DashboardItem("Attendance", R.drawable.ic_attandance));
        dashboardItems.add(new DashboardItem("Event", R.drawable.ic_events));
        dashboardItems.add(new DashboardItem("Announcement", R.drawable.ic_notofication));
       dashboardItems.add(new DashboardItem("Class Activity", R.drawable.ic_class_activity));
        dashboardItems.add(new DashboardItem("Home Work", R.drawable.ic_homework));
        dashboardItems.add(new DashboardItem("Calendar", R.drawable.ic_dash_calendar));
        dashboardItems.add(new DashboardItem("Gallery", R.drawable.gallery));
       dashboardItems.add(new DashboardItem("Exam Schedule", R.drawable.ic_exam_schedule));
       dashboardItems.add(new DashboardItem("Results", R.drawable.ic_results));
       dashboardItems.add(new DashboardItem("Remarks", R.drawable.ic_remarks));
        adapter = new DashboardAdapter(getActivity(), dashboardItems);
        grid_dashboard.setAdapter(adapter);
        adapter.attach(this);
    }

    @Override
    public void onItemClick(String item) {

        if (item.trim().equalsIgnoreCase("time table")) {
            Intent intent = new Intent(getActivity(), TimeTable_.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else if (item.trim().equalsIgnoreCase("Exam Schedule")) {
            Intent intent = new Intent(getActivity(), ShowExamList_.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//            Intent intent=new Intent(getActivity(), ExamSchedule_.class);
//            startActivity(intent);
        } else if (item.trim().equalsIgnoreCase("Home Work")) {
            if (preferences.getString("login_type").toString().equalsIgnoreCase("student")) {
                Intent intent = new Intent(getActivity(), HomeWork_.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            if (preferences.getString("login_type").toString().equalsIgnoreCase("parent")) {
                Intent intent = new Intent(getActivity(), HomeWork_.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            else if (preferences.getString("login_type").toString().equalsIgnoreCase("teacher")) {
                Intent intent = new Intent(getActivity(), HomeWork_.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }

        } else if (item.trim().equalsIgnoreCase("Announcement")) {
            Intent intent = new Intent(getActivity(), AnnouncementActivity_.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else if (item.trim().equalsIgnoreCase("Attendance")) {

            if (preferences.getString("login_type").toString().equalsIgnoreCase("student")) {
                //Intent intent = new Intent(getActivity(), Student_Attendance_.class);
                Intent intent = new Intent(getActivity(), StudentAttendanceCalenderActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            else if( preferences.getString("login_type").toString().equalsIgnoreCase("parent")){
                Intent intent = new Intent(getActivity(), StudentAttendanceCalenderActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            else if (preferences.getString("login_type").toString().equalsIgnoreCase("teacher")) {
                Intent intent = new Intent(getActivity(), Attandance_.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        } else if (item.trim().equalsIgnoreCase("Class Activity")) {
            Intent intent = new Intent(getActivity(), ClassActivities_.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else if (item.trim().equalsIgnoreCase("Results")) {
            startActivity(new Intent(getActivity(), ResultExamSelection_.class));
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else if (item.trim().equalsIgnoreCase("Remarks")) {
            if (preferences.getString("login_type").toString().equalsIgnoreCase("student")) {
                startActivity(new Intent(getActivity(), Remarks_.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            else if( preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
            {
                startActivity(new Intent(getActivity(), Remarks_.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            else if (preferences.getString("login_type").toString().equalsIgnoreCase("teacher")) {
                startActivity(new Intent(getActivity(), AddRemarks_.class));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        } else if (item.trim().equalsIgnoreCase("Event")) {
            startActivity(new Intent(getActivity(), EventList_.class));
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }else if(item.trim().equalsIgnoreCase("Gallery")){
            //startActivity(new Intent(getActivity(), Gallery_.class));
            startActivity(new Intent(getActivity(), GallaryMainPage_.class));
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        }
        else if(item.trim().equalsIgnoreCase("Calendar")){
            startActivity(new Intent(getActivity(), CalenderActivity.class));
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
        //Toast.makeText(getActivity(),item,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private void GCM_API(final String user_id, final String user_type, final String gcm_id, final String type) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
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
                            //AppLogger.info("Response--------" + response);
                            Log.i("Response--------", response);
//                            User user = new User();
//                            user.StoreData(object);



                        }
                        //}
                        catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(getActivity(), e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(getActivity(), "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                AppLogger.info("email--------"+email);
//                AppLogger.info("password--------"+password);
//                AppLogger.info("gcm_id--------"+authenticate);
//                AppLogger.info("user_type--------"+user_type);
                params.put("user_id", user_id);
                params.put("user_type", user_type);
                params.put("gcm_id", gcm_id);
                params.put("type", "android");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

        //  Toast.makeText(Login.this,SMS.ENDPOINT+"",Toast.LENGTH_LONG).show();
        Log.i("DATA_____",SMS.LOGIN);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}

package com.s2paa.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.s2paa.Application.SMS;
import com.s2paa.Listener.CalendarListener;
import com.s2paa.Listener.CustomCalendarView;
import com.s2paa.Listener.DayDecorator;
import com.s2paa.Listener.DayView;
import com.s2paa.Model.AttendanceModel;
import com.s2paa.R;
import com.s2paa.Utils.CalendarUtils;
import com.s2paa.Utils.Messages;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by admin on 8/23/2017.
 */

@EActivity(R.layout.student_attendance)
public class Student_Attendance extends BaseActivity {

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    CustomCalendarView calendarView;
    Date date = new Date();
    ArrayList<AttendanceModel> arrayList = new ArrayList<>();
    ArrayList<Integer> daylist = new ArrayList<>();
    private TextView selectedDateTv;

    @AfterViews
    public void init() {
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Attendance");
        getAttendance();
//        daylist.add(1);
//        daylist.add(2);
//        daylist.add(3);
//        daylist.add(4);
        txtActionTitle.setText("Attendance");
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                if (!CalendarUtils.isPastDay(date)) {
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    //selectedDateTv.setText("Selected date is " + df.format(date));
                } else {
                    //selectedDateTv.setText("Selected date is disabled!");
                }
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                //Toast.makeText(CalendarDayDecoratorActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });


        //adding calendar day decorators
        List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(new DisabledColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAttendance() {
        final ProgressDialog pDialog = new ProgressDialog(Student_Attendance.this);
        pDialog.setMessage("Loading ...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SMS.GET_ATTENDANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        arrayList.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("ResponseAttendance-->", response);
                            JSONArray array = object.getJSONArray("Response");
                            AttendanceModel model = new AttendanceModel();
                            for (int i = 0; i < array.length(); i++) {
                                model.present_absent = array.getJSONObject(i).getString("student_status").toString();
                                model.date = array.getJSONObject(i).getString("date").toString();
                                DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat df = new SimpleDateFormat("d");
                                Date startDate = null;
                                try {
                                    startDate = df2.parse(model.date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                int newDateString = Integer.parseInt(df.format(startDate));
                                daylist.add(newDateString);
                                arrayList.add(model);
                            }

                            //arrayList.
                            //String  ans=object.getString("message").toString();
                            //Toast.makeText(Student_Attendance.this,ans+"",Toast.LENGTH_LONG).show();
                            //finish();
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(Student_Attendance.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(Student_Attendance.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", preferences.getString("student_id"));
                Log.i("Parameter", params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Student_Attendance.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {

            final SimpleDateFormat df = new SimpleDateFormat("d");
            int day2 = Integer.parseInt(df.format(dayView.getDate()));
            //    arrayList.contains(dayView);
            if (daylist.contains(day2) == true) {
                int color = Color.parseColor("#123456");
                dayView.setBackgroundColor(color);
                dayView.setTextColor(Color.WHITE);
            }else{
               // int color = Color.parseColor("#123456");
                dayView.setBackgroundColor(Color.RED);
                dayView.setTextColor(Color.WHITE);
            }
//            int i=0;
//            Log.i("Array Size-->",dayView.getText()+"");
//            final SimpleDateFormat df = new SimpleDateFormat("d");
//            int day2 = Integer.parseInt(df.format(dayView.getDate()));
//            String startDateString="";
//            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
//            Date startDate = null;
//            try {
//                startDate = df2.parse(startDateString);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            int newDateString = Integer.parseInt(df.format(startDate));
//            System.out.println("DAy--" + day2);
//
//            if(arrayList.size()>0) {
//                if(i<arrayList.size()) {
//                 startDateString = arrayList.get(i).date;
//                }
//
//            }
//            if (day2 == newDateString) {
//                int color = Color.parseColor("#123456");
//                dayView.setBackgroundColor(color);
//                dayView.setTextColor(Color.WHITE);
//            }

        }
    }
}

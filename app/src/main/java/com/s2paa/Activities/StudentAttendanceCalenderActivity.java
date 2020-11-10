package com.s2paa.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.s2paa.Adapters.StudentCalendarAdapter;
import com.s2paa.Adapters.StudentCalendarAdapter;
import com.s2paa.Adapters.StudentCalendarAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Model.AttendanceModel;
import com.s2paa.R;
import com.s2paa.Utils.CalendarCollection;
import com.s2paa.Utils.Messages;

import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class StudentAttendanceCalenderActivity extends BaseActivity {
	public GregorianCalendar cal_month, cal_month_copy;
	private StudentCalendarAdapter cal_adapter;
	private TextView tv_month;
	TextView txtActionTitle;
	Toolbar toolbar;
	LinearLayout extra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calender);
		txtActionTitle=(TextView)findViewById(R.id.txtActionTitle);
		toolbar=(Toolbar)findViewById(R.id.toolbar);
		load();
		setSupportActionBar(toolbar);
		showActionBack();
		extra=(LinearLayout)findViewById(R.id.extra);
		extra.setVisibility(View.VISIBLE);
		txtActionTitle.setText("Attendance");
		cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
		cal_month_copy = (GregorianCalendar) cal_month.clone();
		cal_adapter = new StudentCalendarAdapter(this, cal_month, CalendarCollection.date_collection_arr);

		CalendarCollection.date_collection_arr=new ArrayList<CalendarCollection>();
		//CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-12-07","John Birthday"));
		//CALENDAR_API();
		get_holiday_list();
		getAttendance();

		tv_month = (TextView) findViewById(R.id.tv_month);
		tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

		ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			}
		});
		GridView gridview = (GridView) findViewById(R.id.gv_calendar);
		gridview.setAdapter(cal_adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
			
				((StudentCalendarAdapter) parent.getAdapter()).setSelected(v,position);
				String selectedGridDate = StudentCalendarAdapter.day_string
						.get(position);
				
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*","");
				int gridvalue = Integer.parseInt(gridvalueString);

				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
				}
				((StudentCalendarAdapter) parent.getAdapter()).setSelected(v,position);


				((StudentCalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, StudentAttendanceCalenderActivity.this);
			}
			
		});
	


	}

	private void getAttendance() {
		final ProgressDialog pDialog = new ProgressDialog(StudentAttendanceCalenderActivity.this);
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
						//arrayList.clear();
						try {
							JSONObject object = new JSONObject(response);
							Log.i("ResponseAttendance-->", response);
							JSONArray array = object.getJSONArray("Response");
							AttendanceModel model = new AttendanceModel();
							for (int i = 0; i < array.length(); i++) {
								model.present_absent = array.getJSONObject(i).getString("student_status").toString();
								model.date = array.getJSONObject(i).getString("date").toString();
								CalendarCollection.date_collection_arr.add(new CalendarCollection(model.date,model.present_absent));
//								DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
//								SimpleDateFormat df = new SimpleDateFormat("d");
//								Date startDate = null;
//								try {
//									startDate = df2.parse(model.date);
//								} catch (ParseException e) {
//									e.printStackTrace();
//								}
//								int newDateString = Integer.parseInt(df.format(startDate));
//								daylist.add(newDateString);
//								arrayList.add(model);
								refreshCalendar();
							}

							//arrayList.
							//String  ans=object.getString("message").toString();
							//Toast.makeText(Student_Attendance.this,ans+"",Toast.LENGTH_LONG).show();
							//finish();
						} catch (JSONException e) {
							pDialog.hide();
							pDialog.dismiss();
							if (!e.getMessage().trim().equals(""))
								Messages.ShowMessage(StudentAttendanceCalenderActivity.this, e.getMessage().toString());
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						pDialog.hide();
						pDialog.dismiss();
						Messages.ShowMessage(StudentAttendanceCalenderActivity.this, "problem on network connection");
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

		RequestQueue requestQueue = Volley.newRequestQueue(StudentAttendanceCalenderActivity.this);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				7000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		requestQueue.add(stringRequest);
	}
	private void get_holiday_list() {
		final ProgressDialog pDialog = new ProgressDialog(StudentAttendanceCalenderActivity.this);
		pDialog.setMessage("Loading ...");
		pDialog.show();

		StringRequest stringRequest = new StringRequest(
				Request.Method.POST,
				SMS.HOLIDAY_LEAVE,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						pDialog.hide();
						pDialog.dismiss();
						//arrayList.clear();
						try {
							JSONObject object = new JSONObject(response);
							Log.i("ResponseHolidayList-->", response);
							JSONArray array = object.getJSONArray("Response");
							AttendanceModel model = new AttendanceModel();
							for (int i = 0; i < array.length(); i++) {
								model.present_absent=array.getJSONObject(i).getString("type").toString();
								CalendarCollection.date_collection_arr.add(new CalendarCollection(array.getJSONObject(i).getString("holiday_leave_date").toString(),model.present_absent));
								refreshCalendar();
							}

							//arrayList.
							//String  ans=object.getString("message").toString();
							//Toast.makeText(Student_Attendance.this,ans+"",Toast.LENGTH_LONG).show();
							//finish();
						} catch (JSONException e) {
							pDialog.hide();
							pDialog.dismiss();
							if (!e.getMessage().trim().equals(""))
								Messages.ShowMessage(StudentAttendanceCalenderActivity.this, e.getMessage().toString());
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						pDialog.hide();
						pDialog.dismiss();
						Messages.ShowMessage(StudentAttendanceCalenderActivity.this, "problem on network connection");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
				params.put("student_id",preferences.getString("student_id"));
				Log.i("Parameter", params.toString());
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(StudentAttendanceCalenderActivity.this);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				7000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		requestQueue.add(stringRequest);
	}
	protected void setNextMonth() {
		if (cal_month.get(GregorianCalendar.MONTH) == cal_month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
					cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			cal_month.set(GregorianCalendar.MONTH,
					cal_month.get(GregorianCalendar.MONTH) + 1);
		}

	}

	protected void setPreviousMonth() {
		if (cal_month.get(GregorianCalendar.MONTH) == cal_month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
					cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			cal_month.set(GregorianCalendar.MONTH,
					cal_month.get(GregorianCalendar.MONTH) - 1);
		}

	}

	public void refreshCalendar() {
		cal_adapter.refreshDays();
		cal_adapter.notifyDataSetChanged();
		tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	}
}

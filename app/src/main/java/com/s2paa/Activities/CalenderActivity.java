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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.s2paa.Adapters.CalendarAdapter;
import com.s2paa.Application.SMS;
import com.s2paa.Model.User;
import com.s2paa.R;
import com.s2paa.Utils.CalendarCollection;
import com.s2paa.Utils.Messages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class CalenderActivity extends BaseActivity {
	public GregorianCalendar cal_month, cal_month_copy;
	private CalendarAdapter cal_adapter;
	private TextView tv_month;
	TextView txtActionTitle;
	Toolbar toolbar;
	TextView event_details,txt_date;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calender);
		txtActionTitle=(TextView)findViewById(R.id.txtActionTitle);
		toolbar=(Toolbar)findViewById(R.id.toolbar);
		txtActionTitle.setText("Calendar Schedule");

		load();
		setSupportActionBar(toolbar);
		showActionBack();
		cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
		cal_month_copy = (GregorianCalendar) cal_month.clone();
		event_details=(TextView)findViewById(R.id.event_details);
		txt_date=(TextView)findViewById(R.id.date);
		cal_adapter = new CalendarAdapter(this, cal_month, CalendarCollection.date_collection_arr);

		CalendarCollection.date_collection_arr=new ArrayList<CalendarCollection>();
		//CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-12-07","John Birthday"));
		CALENDAR_API();
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.fadein, R.anim.fadeout);
			}
		});
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


		GridView gridview = (GridView) findViewById(R.id.gv_calendar);
		gridview.setAdapter(cal_adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
			
				((CalendarAdapter) parent.getAdapter()).setSelected(v,position);
				String selectedGridDate = CalendarAdapter.day_string
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
				((CalendarAdapter) parent.getAdapter()).setSelected(v,position);


				((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, CalenderActivity.this,event_details,txt_date);

			}
			
		});
	


	}

	private void CALENDAR_API() {
		final ProgressDialog pDialog = new ProgressDialog(CalenderActivity.this);
		pDialog.setMessage("Loading...");
		pDialog.show();

		StringRequest stringRequest = new StringRequest(Request.Method.GET, SMS.GET_CALENDAR_DATA,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						pDialog.hide();
						pDialog.dismiss();
						try {
							JSONObject object = new JSONObject(response);
							//AppLogger.info("Response--------" + response);
							Log.i("Response--------", response);

							JSONArray array=object.getJSONArray("Response");
							for(int i=0;i<array.length();i++) {
									String date=array.getJSONObject(i).getString("calender_date");
								String description=array.getJSONObject(i).getString("description");
								CalendarCollection.date_collection_arr.add(new CalendarCollection(date, description));

							}
							refreshCalendar();

						}
						//}
						catch (JSONException e) {
							pDialog.hide();
							pDialog.dismiss();
							if (!e.getMessage().trim().equals(""))
								Messages.ShowMessage(CalenderActivity.this, e.getMessage().toString());
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						pDialog.hide();
						pDialog.dismiss();
						Messages.ShowMessage(CalenderActivity.this, "problem on network connection");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
//

				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(CalenderActivity.this);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				7000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		requestQueue.add(stringRequest);

		//  Toast.makeText(Login.this,SMS.ENDPOINT+"",Toast.LENGTH_LONG).show();
		Log.i("DATA_____",SMS.LOGIN);
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
	//		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
	}
}

package com.s2paa.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.s2paa.Application.SMS;
import com.s2paa.Model.AttendanceModel;
import com.s2paa.R;
import com.s2paa.Utils.Messages;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/18/2017.
 */

@EActivity(R.layout.standard_list)
public class StudentAct extends BaseActivity {


    @ViewById
    ListView standard_list;

    ArrayList<String> list=new ArrayList<>();

    @ViewById(R.id.search_editText)
    EditText searchEDTX;
    ArrayList<AttendanceModel> list2=new ArrayList<>();
    ArrayAdapter<String> adapter;
    @AfterViews
    public void init()
    {
        load();
        String id=getIntent().getStringExtra("class_id").toString();
        String sid=getIntent().getStringExtra("section_id").toString();
        LoadData(id,sid);
        standard_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(Activity.RESULT_OK,getIntent().putExtra("ROLLNO", adapter.getItem(position)));
                //KeyBoardHandling.hideSoftKeyboard(getApplicationContext());
                finish();
            }
        });
    }

    private void LoadData(final String class_id, final String sec_id){
        final ProgressDialog pDialog = new ProgressDialog(StudentAct.this);
        pDialog.setMessage("Loading.....");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SMS.GET_STUDENT_FOR_CLASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        list.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            AttendanceModel attendanceModel=new AttendanceModel();
                            list2=attendanceModel.getAttendanceData(object.getJSONArray("Response"));
                            //adapters=new AttendanceAdapters(AddTeacherHomeWork.this,list);
                            for (int i=0;i<list2.size();i++){
                                list.add(list2.get(i).student_id);
                            }
                           adapter =new ArrayAdapter<String>(StudentAct.this,android.R.layout.simple_list_item_1,list);
                            standard_list.setAdapter(adapter);
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(StudentAct.this,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(StudentAct.this,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id",class_id+"");
                params.put("section_id",sec_id+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(StudentAct.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}

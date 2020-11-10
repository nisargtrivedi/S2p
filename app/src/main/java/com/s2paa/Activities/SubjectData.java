package com.s2paa.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobandme.ada.Entity;
import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Adapters.SubjectList;
import com.s2paa.Application.SMS;
import com.s2paa.Model.Subject;
import com.s2paa.R;
import com.s2paa.Utils.ExceptionsHelper;
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

/**
 * Created by admin on 9/12/2017.
 */

@EActivity(R.layout.standard_list)
public class SubjectData extends BaseActivity {
    @ViewById
    ListView standard_list;

    SubjectList subjectList;

    @ViewById(R.id.search_editText)
    EditText searchEDTX;

    List<Subject> list=new ArrayList<>();

    @AfterViews
    public void init()
    {
        String id=getIntent().getStringExtra("class_id").toString();
        String sid=getIntent().getStringExtra("section_id").toString();
        load();
        getSubjectData(id,sid);


        searchEDTX.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,int arg3) {
                subjectList.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {}
        });


        standard_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(Activity.RESULT_OK,getIntent().putExtra("SUBJECT",subjectList.getItem(position)));
                //KeyBoardHandling.hideSoftKeyboard(getApplicationContext());
                finish();
            }
        });
    }

    public void getSubjectData(final String class_id, final String sec_id) {
        final ProgressDialog pDialog = new ProgressDialog(SubjectData.this);
        pDialog.setMessage("Loading Subject Details...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SMS.GET_SUBJECT_OF_CLASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        //list.clear();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("SubjectData---", response);
                            //JSONArray array=object.getJSONArray("Response");
                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();
                            list = Arrays.asList(mGson.fromJson(object.getJSONArray("Response")+"", Subject[].class));
                            try {

                                dataContext.subjectObjectSet.fill();
                                subjectList = new SubjectList(SubjectData.this, list);
                                standard_list.setAdapter(subjectList);
                            } catch (AdaFrameworkException e) {
                                e.printStackTrace();
                            }
                            addDataToDB();
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if (!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(SubjectData.this, e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(SubjectData.this, "problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id",class_id);
                params.put("section_id",sec_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SubjectData.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }




    private void addDataToDB() {

        try {
            dataContext.subjectObjectSet.fill();

                for (int i = 0; i < dataContext.subjectObjectSet.size(); i++)
                    dataContext.subjectObjectSet.get(i).setStatus(Entity.STATUS_DELETED);

                dataContext.subjectObjectSet.save();
                dataContext.subjectObjectSet.addAll(list);
                dataContext.subjectObjectSet.save();
            }
         catch (Exception ex) {
            ExceptionsHelper.manage(SubjectData.this, ex);
        }
    }
}

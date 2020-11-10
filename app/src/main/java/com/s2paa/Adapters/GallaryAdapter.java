package com.s2paa.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.s2paa.Activities.Show_Comment_Event_;
import com.s2paa.Application.SMS;
import com.s2paa.Model.DataContext;
import com.s2paa.Model.GallaryObjects;
import com.s2paa.Model.Multiple_Gallary;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;
import com.s2paa.Utils.Messages;
import com.s2paa.Utils.RoundedCornersTransform;
import com.s2paa.Utils.TTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 7/31/2017.
 */

public class GallaryAdapter extends  RecyclerView.Adapter<GallaryAdapter.ViewHolder> {


    public List<GallaryObjects> list;
    Context context;
    AppPreferences preferences;
    List<Multiple_Gallary> eventGalleries = new ArrayList<>();
    GallayViewPager homeAdapter;
    DataContext dataContext;
    int page = 0;
    public GallaryAdapter(Context context, List<GallaryObjects> list){
        this.context=context;
        preferences=new AppPreferences(context);
        this.list=list;
        dataContext=new DataContext(context);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView event_img;

        public ViewHolder(View view) {
            super(view);
            event_img=(ImageView)view.findViewById(R.id.event_img);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.gallary_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GallaryObjects annoucement = list.get(position);
        String id="";
        if(preferences.getString("login_type").toString().equalsIgnoreCase("student"))
            id=preferences.getString("student_id");
        else if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher"))
            id=preferences.getString("teacher_id");
        else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
            id=preferences.getString("parent_id");

//        holder.title.setText(annoucement.title);
//        holder.description.setVisibility(View.GONE);
//       // holder.description.setText(annoucement.description);
//        Date date = new Date(annoucement.event_date);
//        System.out.println(date);
//        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//        String msg=dateFormat.format(date);
//        holder.date.setText(msg);
//        //Glide.with(context).load(annoucement.main_img).into(holder.event_img);
//        holder.likes.setText("Like ("+annoucement.eventTotalLikes+")");
//        holder.comment.setText("Comments ("+annoucement.eventTotalComments+")");
        Picasso.with(context).load(annoucement.main_img).transform(new RoundedCornersTransform(5, RoundedCornersTransform.Corners.ALL)).into(holder.event_img);



        holder.event_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUp(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private void openPopUp(final int position)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view =  inflater.inflate(R.layout.gallery,null);
        AlertDialog.Builder builder =  new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        final ViewPager sliders = (ViewPager) view.findViewById(R.id.sliders);

        final ProgressDialog pDialog = new ProgressDialog(context);
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
                            JSONArray array=object.getJSONArray("Response").getJSONObject(position).getJSONArray("galleryImages");
                            eventGalleries = Arrays.asList(mGson.fromJson(array+"", Multiple_Gallary[].class));
                            homeAdapter=new GallayViewPager(context, eventGalleries);

                            sliders.setAdapter(homeAdapter);
                            sliders.setCurrentItem(0);

                            // finish();
                        } catch (JSONException e) {
                            pDialog.hide();
                            pDialog.dismiss();
                            if(!e.getMessage().trim().equals(""))
                                Messages.ShowMessage(context,e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        pDialog.dismiss();
                        Messages.ShowMessage(context,"problem on network connection");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                if(preferences.getString("login_type").toString().equalsIgnoreCase("student"))
//                    params.put("user_id",preferences.getString("student_id"));
//                else if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher"))
//                    params.put("user_id",preferences.getString("teacher_id"));
//                else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
//                    params.put("user_id",preferences.getString("parent_id"));
//
//                params.put("user_type",preferences.getString("login_type"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

        alertDialog.show();
    }
}

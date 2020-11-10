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
import com.s2paa.Model.EventGallery;
import com.s2paa.Model.EventObjects;
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

public class EventAdapter extends  RecyclerView.Adapter<EventAdapter.ViewHolder> {


    public List<EventObjects> list;
    Context context;
    AppPreferences preferences;
    List<EventGallery> eventGalleries = new ArrayList<EventGallery>();
    GalleryViewPager homeAdapter;
    DataContext dataContext;
    int page = 0;
    public EventAdapter(Context context, List<EventObjects> list){
        this.context=context;
        preferences=new AppPreferences(context);
        this.list=list;
        dataContext=new DataContext(context);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView event_img;
        public TTextView title,date,description,likes,comment,share;

        public ViewHolder(View view) {
            super(view);
            event_img=(ImageView)view.findViewById(R.id.event_img);
            title = (TTextView) view.findViewById(R.id.title);
            date = (TTextView) view.findViewById(R.id.date);
            description=(TTextView)view.findViewById(R.id.description);
            likes=(TTextView)view.findViewById(R.id.likes);
            comment=(TTextView)view.findViewById(R.id.comment);
            share=(TTextView)view.findViewById(R.id.share);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.event_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final EventObjects annoucement = list.get(position);
        String id="";
        if(preferences.getString("login_type").toString().equalsIgnoreCase("student"))
            id=preferences.getString("student_id");
        else if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher"))
            id=preferences.getString("teacher_id");
        else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
            id=preferences.getString("parent_id");

        holder.title.setText(annoucement.title);
        holder.description.setVisibility(View.GONE);
       // holder.description.setText(annoucement.description);
        Date date = new Date(annoucement.event_date);
        System.out.println(date);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String msg=dateFormat.format(date);
        holder.date.setText(msg);
        //Glide.with(context).load(annoucement.main_img).into(holder.event_img);
        holder.likes.setText("Like ("+annoucement.eventTotalLikes+")");
        holder.comment.setText("Comments ("+annoucement.eventTotalComments+")");
        Picasso.with(context).load(annoucement.main_img).transform(new RoundedCornersTransform(5, RoundedCornersTransform.Corners.ALL)).into(holder.event_img);


        if(annoucement.is_like==1 && annoucement.user_id.toString().equalsIgnoreCase(id)){
            holder.likes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_selected,0,0,0);
        }
        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(annoucement.is_like==0) {
                    holder.likes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_selected, 0, 0, 0);
                    annoucement.eventTotalLikes++;
                    holder.likes.setText("Like (" + annoucement.eventTotalLikes + ")");
                    Likes(annoucement.event_id);
                }else{
                    Toast.makeText(context,"You already like this event",Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.event_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUp(position);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, annoucement.description + "\n" + annoucement.main_img);
                context.startActivity(Intent.createChooser(sharingIntent,"Share using"));
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Show_Comment_Event_.class).putExtra("event_id",annoucement.event_id+""));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void Likes(final int eventid)
    {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading.....");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SMS.LIKES_EVENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        pDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.i("Response Event------>",response);
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
                if(preferences.getString("login_type").toString().equalsIgnoreCase("student"))
                    params.put("user_id",preferences.getString("student_id"));
                else if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher"))
                    params.put("user_id",preferences.getString("teacher_id"));
                else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
                    params.put("user_id",preferences.getString("parent_id"));

                params.put("user_type",preferences.getString("login_type"));
                params.put("event_id",eventid+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
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
                Request.Method.POST,
                SMS.GET_EVENT,
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
                            JSONArray array=object.getJSONArray("Response").getJSONObject(position).getJSONArray("eventImages");
                            eventGalleries = Arrays.asList(mGson.fromJson(array+"", EventGallery[].class));
                            homeAdapter=new GalleryViewPager(context, eventGalleries);

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
                if(preferences.getString("login_type").toString().equalsIgnoreCase("student"))
                    params.put("user_id",preferences.getString("student_id"));
                else if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher"))
                    params.put("user_id",preferences.getString("teacher_id"));
                else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent"))
                    params.put("user_id",preferences.getString("parent_id"));

                params.put("user_type",preferences.getString("login_type"));
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

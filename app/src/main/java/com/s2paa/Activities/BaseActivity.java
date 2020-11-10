package com.s2paa.Activities;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.s2paa.Model.DataContext;
import com.s2paa.R;
import com.s2paa.Utils.AppPreferences;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 7/7/2017.
 */

@EActivity
public class BaseActivity extends AppCompatActivity {

        @ViewById
        Toolbar toolbar;

        ProgressDialog dialog;
        DataContext dataContext;
        RequestQueue requestQueue;
        AppPreferences preferences;


    public void load()
    {
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        dataContext=new DataContext(this);
        requestQueue= Volley.newRequestQueue(this);
        preferences=new AppPreferences(this);
        dialog=new ProgressDialog(this);
    }
    public void showActionBack(){
        ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(toolbar);
        if (actionBar!=null){
            Drawable drawable = getResources().getDrawable(R.drawable.back);
            actionBar.setHomeAsUpIndicator(drawable);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }
    }
    void showDialog(String message) {
        dialog.setMessage(message);
        dialog.show();
    }

    void showDialog() {
        dialog.setMessage("Loading...");
        dialog.show();
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

}

package com.s2paa.Activities;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.s2paa.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by admin on 9/26/2017.
 */

@EActivity(R.layout.standard_list)
public class choose_result extends BaseActivity {


    @ViewById
    ListView standard_list;

    ArrayAdapter<String> adapter;

    ArrayList<String> list=new ArrayList<>();
    @AfterViews
    public void init()
    {
        load();
        try {
            list.add("STUDENT WISE");
            list.add("SUBJECT WISE");
             adapter=new ArrayAdapter<>(choose_result.this,android.R.layout.simple_list_item_1,list);
            standard_list.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        standard_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(Activity.RESULT_OK,getIntent().putExtra("SELECT", adapter.getItem(position)));
                //KeyBoardHandling.hideSoftKeyboard(getApplicationContext());
                finish();
            }
        });
    }

}

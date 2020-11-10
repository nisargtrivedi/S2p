package com.s2paa.Activities;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.s2paa.Model.ClassActivity;
import com.s2paa.Model.Home_Work;
import com.s2paa.Model.Leave;
import com.s2paa.R;
import com.s2paa.Utils.DateUtils;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 8/3/2017.
 */

@EActivity(R.layout.details_activity)
public class DetailsActivity extends BaseActivity {

    @ViewById
    TTextView details_date;

    @ViewById
    TTextView details_title;

    @ViewById
    TTextView activity_details;


    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    ClassActivity classActivity;
    Home_Work home_work;
    Leave leave;

    @AfterViews
    public void init()
    {
        load();
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Details");
        classActivity=new ClassActivity();
        home_work=new Home_Work();
        leave=new Leave();
        if(getIntent().hasExtra("Class")) {
            classActivity = (ClassActivity) getIntent().getSerializableExtra("Class");
            details_title.setText(classActivity.title);
            details_date.setText(DateUtils.GETLOCALE_DATE(classActivity.create_at));
            activity_details.setText(classActivity.details);
        }
        else if(getIntent().hasExtra("Home")) {
            home_work = (Home_Work) getIntent().getSerializableExtra("Home");
            details_title.setText(home_work.subject_name);
            details_date.setText(DateUtils.GETLOCALE_DATE(home_work.date));
            activity_details.setText(home_work.subject_details);
        }

        else if(getIntent().hasExtra("Leave")) {
            leave = (Leave) getIntent().getSerializableExtra("Leave");
            details_title.setText(leave.title);
            details_date.setText(leave.leave_date.toString());
            activity_details.setText(leave.details);
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}

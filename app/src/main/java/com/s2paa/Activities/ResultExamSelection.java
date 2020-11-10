package com.s2paa.Activities;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Adapters.ResultExamListAdapter;
import com.s2paa.Model.ExamList;
import com.s2paa.Model.Exam_Schedule;
import com.s2paa.R;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by admin on 9/12/2017.
 */

@EActivity(R.layout.exam)
public class ResultExamSelection extends BaseActivity {
    @ViewById
    RecyclerView rv_exam;
    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @ViewById
    TTextView total;


    ArrayList<ExamList> list=new ArrayList<>();
    ArrayList<ExamList> examListArrayList = new ArrayList<>();
    ArrayList<Exam_Schedule> exam_scheduleArrayList=new ArrayList<>();

    ResultExamListAdapter adapter;
    @AfterViews
    public void init(){
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Exam List");
        //tExams();
        try {
            dataContext.examListObjectSet.fill();
            adapter=new ResultExamListAdapter(ResultExamSelection.this,dataContext.examListObjectSet);
            rv_exam.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            rv_exam.setLayoutManager(layoutManager);
            rv_exam.setItemAnimator(new DefaultItemAnimator());

            total.setText("Total Exam "+dataContext.examListObjectSet.size()+"");
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

}

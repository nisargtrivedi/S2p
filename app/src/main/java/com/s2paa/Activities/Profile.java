package com.s2paa.Activities;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Adapters.ParentAdapter;
import com.s2paa.Model.subUserDetails;
import com.s2paa.R;
import com.s2paa.Utils.TTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by admin on 8/10/2017.
 */

@EActivity(R.layout.profile)
public class Profile extends BaseActivity {



    @ViewById
    de.hdodenhof.circleimageview.CircleImageView img;

    @ViewById
    TTextView name;

    @ViewById
    TTextView phno;

    @ViewById
    TTextView email;

    @ViewById
    TTextView standard;

    @ViewById
    TTextView division;

    @ViewById
    TTextView rollno;

    @ViewById
    TTextView address;

    @ViewById
    RecyclerView parents;

    @ViewById
    LinearLayout  ll_one;

    @ViewById
    LinearLayout  ll_two;
    @ViewById
    TTextView  ll_three;

    ParentAdapter adapter;
    ArrayList<subUserDetails> list=new ArrayList<>();

    @AfterViews
    public void init()
    {
        load();
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);
//        showActionBack();
//        txtActionTitle.setText("Profile");
        if(preferences.getString("login_type").toString().equalsIgnoreCase("student")) {
            ll_one.setVisibility(View.VISIBLE);
            ll_two.setVisibility(View.VISIBLE);
            ll_three.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(preferences.getString("name")))
                name.setText(preferences.getString("name"));
            if (!TextUtils.isEmpty(preferences.getString("stu_email")))
                email.setText(preferences.getString("stu_email"));
            if (!TextUtils.isEmpty(preferences.getString("address")))
                address.setText(preferences.getString("address"));
            if (!TextUtils.isEmpty(preferences.getString("img")))
                Glide.with(this).load(preferences.getString("img").toString()).into(img);
            if (!TextUtils.isEmpty(preferences.getString("class_name")))
                standard.setText(preferences.getString("class_name").toString());
            if (!TextUtils.isEmpty(preferences.getString("section_name")))
                division.setText(preferences.getString("section_name").toString());
            if (!TextUtils.isEmpty(preferences.getString("rollno")))
                rollno.setText(preferences.getString("rollno").toString());
            if (!TextUtils.isEmpty(preferences.getString("phone")))
                phno.setText(preferences.getString("phone").toString());
            subUserDetails subUserDetails=new subUserDetails();
            subUserDetails.name=preferences.getString("father_name");
            subUserDetails.phone=preferences.getString("house_phone");
            subUserDetails.profile_img=preferences.getString("father_img");
            subUserDetails.email=preferences.getString("parent_email");
            subUserDetails subUserDetails2=new subUserDetails();
            subUserDetails2.name=preferences.getString("mother_name");
            subUserDetails2.phone="";
            subUserDetails2.profile_img=preferences.getString("mother_img");
            subUserDetails2.email=preferences.getString("parent_email");
            list.clear();
            list.add(subUserDetails);
            list.add(subUserDetails2);
            adapter=new ParentAdapter(Profile.this,list);
            parents.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            parents.setLayoutManager(layoutManager);
            parents.setItemAnimator(new DefaultItemAnimator());
        }else if(preferences.getString("login_type").toString().equalsIgnoreCase("teacher")){
            if (!TextUtils.isEmpty(preferences.getString("name")))
                name.setText(preferences.getString("name"));
            if (!TextUtils.isEmpty(preferences.getString("stu_email")))
                email.setText(preferences.getString("stu_email"));
            if (!TextUtils.isEmpty(preferences.getString("address")))
                address.setText(preferences.getString("address"));
            if (!TextUtils.isEmpty(preferences.getString("img")))
                Glide.with(this).load(preferences.getString("img").toString()).into(img);
            if (!TextUtils.isEmpty(preferences.getString("phone")))
                phno.setText(preferences.getString("phone").toString());
            ll_one.setVisibility(View.GONE);
            ll_two.setVisibility(View.GONE);
            ll_three.setVisibility(View.GONE);
        }
        else if(preferences.getString("login_type").toString().equalsIgnoreCase("parent")){
            rollno.setText(preferences.getString("roll"));
            try {
                dataContext.parentStudentObjectSet.fill();
            } catch (AdaFrameworkException e) {
                e.printStackTrace();
            }
            subUserDetails subUserDetails=new subUserDetails();
            subUserDetails.name=preferences.getString("father_name");
            subUserDetails.phone=preferences.getString("house_phone");
            subUserDetails.profile_img=preferences.getString("img");
            subUserDetails.email=preferences.getString("parent_email");
            subUserDetails subUserDetails2=new subUserDetails();
            subUserDetails2.name=preferences.getString("mother_name");
            subUserDetails2.phone="";
            subUserDetails2.profile_img=preferences.getString("mother_imge");
            subUserDetails2.email=preferences.getString("parent_email");
            list.clear();
            list.add(subUserDetails);
            list.add(subUserDetails2);
            adapter=new ParentAdapter(Profile.this,list);
            parents.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            parents.setLayoutManager(layoutManager);
            parents.setItemAnimator(new DefaultItemAnimator());
            name.setText(dataContext.parentStudentObjectSet.get(0).name + dataContext.parentStudentObjectSet.get(0).last_name);
            email.setText(preferences.getString("stu_email"));
            if (!TextUtils.isEmpty(preferences.getString("roll")))
                rollno.setText(preferences.getString("roll").toString());

            address.setText(dataContext.parentStudentObjectSet.get(0).address);
            Glide.with(this).load(dataContext.parentStudentObjectSet.get(0).profile_img).into(img);
            phno.setText(dataContext.parentStudentObjectSet.get(0).phone);
            if (!TextUtils.isEmpty(preferences.getString("class_name")))
                standard.setText(preferences.getString("class_name").toString());
            ll_one.setVisibility(View.VISIBLE);
            ll_two.setVisibility(View.VISIBLE);
            ll_three.setVisibility(View.VISIBLE);
        }




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

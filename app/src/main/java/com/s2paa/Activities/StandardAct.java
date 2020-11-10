package com.s2paa.Activities;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Adapters.StandardList;
import com.s2paa.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 8/18/2017.
 */

@EActivity(R.layout.standard_list)
public class StandardAct extends BaseActivity {


    @ViewById
    ListView standard_list;

    StandardList standardList;

    @ViewById(R.id.search_editText)
    EditText searchEDTX;

    @AfterViews
    public void init()
    {
        load();
        try {
            dataContext.standardObjectSet.fill();
            standardList=new StandardList(StandardAct.this,dataContext.standardObjectSet);
           // Toast.makeText(getApplicationContext(),dataContext.standardObjectSet.size()+"",Toast.LENGTH_LONG).show();
            standard_list.setAdapter(standardList);
        } catch (AdaFrameworkException e) {
            e.printStackTrace();
        }
        searchEDTX.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,int arg3) {
                standardList.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {}
        });


        standard_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(Activity.RESULT_OK,getIntent().putExtra("STANDARD", standardList.getItem(position)));
                //KeyBoardHandling.hideSoftKeyboard(getApplicationContext());
                finish();
            }
        });
    }
}

package com.s2paa.Activities;

import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.s2paa.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 9/21/2017.
 */

@EActivity(R.layout.chat_list)
public class ChatList extends BaseActivity {

    @ViewById
    WebView wb_show_chat;

    @ViewById
    Toolbar toolbar;
    @ViewById
    TextView txtActionTitle;

    @AfterViews
    public void init()
    {
        load();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showActionBack();
        txtActionTitle.setText("Chat");
        wb_show_chat.setWebViewClient(new WebViewClient());
        wb_show_chat.getSettings().setJavaScriptEnabled(true);
        wb_show_chat.loadUrl("http://school2parent.com/superadmin/index.php?mobiletheme/getMessageIframe/teacher/1");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

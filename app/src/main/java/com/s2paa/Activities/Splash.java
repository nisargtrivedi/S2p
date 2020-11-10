package com.s2paa.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.s2paa.R;
import com.s2paa.Utils.AppLogger;
import com.s2paa.Utils.AppPreferences;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by admin on 7/7/2017.
 */

@EActivity(R.layout.splash)
public class Splash extends Activity {

    AppPreferences preferences;
    private static int SPLASH_TIME_OUT = 3000;
    private static final int REQUEST_WRITE_STORAGE = 1001;
    private static final int INTERNET_REQUEST = 1002;
    private static final int READ_PHONE_REQUEST = 1003;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    @AfterViews
    protected void init() {
        preferences=new AppPreferences(Splash.this);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    // Toast.makeText(getApplicationContext(), "Push notification:-------------->>> " + message, Toast.LENGTH_LONG).show();
                    AppLogger.info("Push notification:-------------->>> " + message);

                }
            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            AppLogger.info("check :: ask for permission");

            Log.e("debug", "splash oncreate() asking permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE}, REQUEST_WRITE_STORAGE);
        } else {
            AppLogger.info("check :: permission granted already");
            Log.e("debug", "splash oncreate() permission granted");
            permission();
        }
        Log.i("TOKEN ID", preferences.getString("regId"));
        AppLogger.info("TOKEN-------------"+preferences.getString("regId"));
        //Toast.makeText(getApplicationContext(),preferences.getString("regId"),Toast.LENGTH_LONG).show();

    }
    private void displayFirebaseRegId() {

        //regId = preferences.getString("regId");

        AppLogger.info("NOTIFICATION TOKEN ----------------------------------------- " + preferences.getString("regId"));

//        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
    }
    public void permission()
    {

        boolean hasPermission2 = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        boolean hasPermission3 = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED);
        boolean hasPermission4 = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission2) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
            return;
        }
        if (!hasPermission3) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    INTERNET_REQUEST);
            return;
        }
        if (!hasPermission4) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    READ_PHONE_REQUEST);
            return;
        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splash.this,MainPage_.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AppLogger.info("requestCode::"+requestCode);
        Log.e("debug","splash onRequestPermissionsResult requestCode::"+requestCode);
        switch (requestCode){
            case REQUEST_WRITE_STORAGE:
                AppLogger.info("in write request");
                permission();
                break;
            case INTERNET_REQUEST:
                AppLogger.info("in write request");
                permission();
                break;
            case READ_PHONE_REQUEST:
                AppLogger.info("in write request");
                permission();
                break;
        }
    }

}

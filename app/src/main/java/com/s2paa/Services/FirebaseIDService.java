package com.s2paa.Services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.s2paa.Activities.Config;
import com.s2paa.Utils.AppLogger;
import com.s2paa.Utils.AppPreferences;

/**
 * Created by nisarg on 12/7/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = FirebaseIDService.class.getSimpleName();

    AppPreferences apppreferences;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        AppLogger.info("Token------------------------------------------------------------------>"+token);
//        AppPreferences preferences=new AppPreferences(getApplicationContext());
//        preferences.set("regId",token);
//        apppreferences.set("regId",token);
    }

    private void storeRegIdInPref(String token) {

        AppPreferences preferences=new AppPreferences(getApplicationContext());
        preferences.set("regId",token);
    }
}
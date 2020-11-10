package com.s2paa.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class AppPreferences {

    public static String PREF_NAME = "SMS_APP_PREFERENCES";
    public static String IS_LOGGED_IN = "SMS_IS_USER_LOGGED_IN";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public AppPreferences(Context context){
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean getBoolean(String key, boolean defFlag) {
        return pref.getBoolean(key,defFlag);
    }

    public String getString(String key) {
        return pref.getString(key,"");
    }

    public int getInteger(String key) {
        return pref.getInt(key,0);
    }

    public int getInteger(String key,int defaultVal) {
        return pref.getInt(key,defaultVal);
    }

    public long getLong(String key){
        return pref.getLong(key,0);
    }

    public void removeValue(String key){
        editor=pref.edit();
        editor.remove(key);
        editor.commit();
        editor=null;
    }

    public void set(String key, boolean flag) {
        editor=pref.edit();

        editor.putBoolean(key,flag);
        editor.commit();
        editor=null;
    }

    public void set(String key, long value) {
        editor=pref.edit();
        editor.putLong(key,value);
        editor.commit();
        editor=null;
    }

    public void set(String key, String val){
        editor=pref.edit();
        editor.putString(key,val);
        editor.commit();
        editor=null;
    }

    public void set(String key, int val) {
        editor=pref.edit();
        editor.putInt(key,val);
        editor.commit();
        editor=null;
    }
}

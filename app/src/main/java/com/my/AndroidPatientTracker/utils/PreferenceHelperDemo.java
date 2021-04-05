package com.my.AndroidPatientTracker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelperDemo {

    private final SharedPreferences mPrefs;

    public PreferenceHelperDemo(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String PREF_Key= "Key";

    public String getKey(String key) {
        String str = mPrefs.getString(key, "");
        return str;
    }

    public void setKey(String key,String value) {
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }

}

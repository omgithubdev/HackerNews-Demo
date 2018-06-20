package com.omagrahari.hackernews.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    private Context context;

    // Shared pref name
    final String PREF_NAME = "pref";

    // Shared pref mode
    final int PRIVATE_MODE = 0;

    public PreferenceManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void resetPreferences() {
        editor.clear();
        editor.commit();
    }

    public void setBooleanPref(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoooleanPref(String key) {
        return pref.getBoolean(key, false);
    }

    public void setStringPref(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringPref(String key) {
        return pref.getString(key, "");
    }

}

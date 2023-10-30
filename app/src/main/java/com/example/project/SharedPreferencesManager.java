package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SharedPreferencesManager {

    private static final String PREFERENCES_NAME = "my_preferences";

    public static SharedPreferences getPreferences(Context mContext){
        return mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void clearPreferences(Context context){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public static void setLoginInfo(Context context, String id, String password){
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id", id);
        editor.putString("password", password);

        editor.commit();
    }
    public static void setBoolean(Context context, String key, boolean value) {

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        boolean value = prefs.getBoolean(key, false);
        return value;
    }
    public static Map<String, String> getLoginInfo(Context context){
        SharedPreferences prefs = getPreferences(context);
        Map<String, String> LoginInfo = new HashMap<>();
        String id = prefs.getString("id", "");
        String password = prefs.getString("password", "");

        LoginInfo.put("id", id);
        LoginInfo.put("password", password);

        return LoginInfo;
    }

}
package com.thetechroot.vision.CommonCl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedCommon {

   public static final String key1 = "key1";

    public static final String keycard = "keycard";
    public static final String keycard6 = "keycard6";
    public static final String keycard32 = "keycard32";
    public static final String keycard600 = "keycard600";

    public static final String keynotification = "keynotification";

    public static final String txtsix = "txt600";



   public static final String key2 = "key2";


    public void putPreferencesInt(Context context, String key, int value){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=preferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getPreferencesInt(Context context, String key, int _default){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, _default);
    }

    public static void putSharedPreferencesString(Context context, String key, String val) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, val);
        edit.commit();

    }

    public static String getSharedPreferencesString(Context context, String key, String _default){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, _default);
    }

}

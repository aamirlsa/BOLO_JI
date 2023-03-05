package com.boloji.videocallchat.TemporaryData;

import android.content.Context;
import android.content.SharedPreferences;

public class TempSharedpref {
    private static final String APP_SHARED_PREFERENCE = "app_preferences";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String APIURL = "APIURL";
    public static final String PACKAGE = "PACKAGE";
    public static final String VERSIONKEY = "VERSIONKEY";
    public static final String APPNAME = "APPNAME";
    public static final String Server2Baseurl = "Server2Baseurl";
    public static final String Server2SocketURL = "Server2SocketURL";
    public static void setString(Context ctx, String Key, String Value) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, 0).edit();
        editor.putString(Key, Value);
        editor.apply();
    }

    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, 0).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setInt(Context ctx, String key, int value) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, 0).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getString(Context ctx, String Key) {
        SharedPreferences pref = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, 0);
        if (pref.contains(Key)) {
            return pref.getString(Key, "");
        }
        return "";
    }
    public static String getDefault(Context ctx, String Key,String deflt) {
        SharedPreferences pref = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, 0);
        if (pref.contains(Key)) {
            return pref.getString(Key, deflt);
        }
        return deflt;
    }
    public static boolean getBoolean(Context ctx, String Key, Boolean defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, 0);
        if (pref.contains(Key)) {
            return pref.getBoolean(Key, defaultValue);
        }
        return defaultValue;
    }

    public static int getInt(Context ctx, String Key, int defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(APP_SHARED_PREFERENCE, 0);
        if (pref.contains(Key)) {
            return pref.getInt(Key, defaultValue);
        }
        return defaultValue;
    }
}

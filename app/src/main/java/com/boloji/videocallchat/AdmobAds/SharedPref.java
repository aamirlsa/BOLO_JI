package com.boloji.videocallchat.AdmobAds;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static final String APP_SHARED_PREFERENCE = "app_preferences";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String ID = "id";
    public static final String GENDER = "gender";
    public static final String COUNTRY = "country";
    public static final String DEVICE_ID = "device_id";
    public static final String LAST_VIDEO_TIMESTAMP = "last_video_timestamp";
    public static final String SHOW_VIDEO_1 = "show_video_1";
    public static final String SHOW_VIDEO_2 = "show_video_2";
    public static final String COUNT = "count";

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

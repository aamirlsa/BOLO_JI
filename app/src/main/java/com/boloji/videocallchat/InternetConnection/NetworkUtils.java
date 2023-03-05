package com.boloji.videocallchat.InternetConnection;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NetworkUtils {

    public static boolean isConnectedToInternet(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    static boolean hasActiveInternetConnection(Context context) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://www.google.com").openConnection();
            httpURLConnection.setRequestProperty("User-Agent", "Test");
            httpURLConnection.setRequestProperty("Connection", "close");
            httpURLConnection.setConnectTimeout(1500);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean isConnectedToWifi(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(1).isConnected();
    }

    public static boolean isConnectedToMobileNetwork(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(0).isConnected();
    }

    public static void turnOn3g(Context context) {
        context.startActivity(new Intent("android.settings.SETTINGS"));
    }

    public static void turnOnWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null || isConnectedToWifi(context)) {
            context.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
        } else {
            wifiManager.setWifiEnabled(true);
        }
    }

    public static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    public static void turnOffAirplaneMode(Context context) {
        context.startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }
}

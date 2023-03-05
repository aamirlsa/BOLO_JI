package com.boloji.videocallchat.WiFiConnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.boloji.videocallchat.InternetConnection.ConnectListener;


public class WiFiRec extends BroadcastReceiver {
    private ConnectListener connectionListener;

    public void onReceive(Context context, Intent intent) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo == null || activeNetworkInfo.getType() != 1) {
            ConnectListener connectListener = this.connectionListener;
            if (connectListener != null) {
                connectListener.onWifiTurnedOff();
                return;
            }
            return;
        }
        ConnectListener connectListener2 = this.connectionListener;
        if (connectListener2 != null) {
            connectListener2.onWifiTurnedOn();
        }
    }

    public void setConnectionListener(ConnectListener connectListener) {
        this.connectionListener = connectListener;
    }
}

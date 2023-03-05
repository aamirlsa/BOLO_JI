package com.boloji.videocallchat.WiFiConnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.util.Log;

import com.boloji.videocallchat.InternetConnection.NetworkCallBackListner;

public class NetwrokRec extends BroadcastReceiver {
    private NetworkCallBackListner connectionCallback;

    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo;
        if (!(intent.getExtras() == null || (networkInfo = (NetworkInfo) intent.getExtras().get("networkInfo")) == null || networkInfo.getState() != NetworkInfo.State.CONNECTED)) {
            Log.i("app", "Network " + networkInfo.getTypeName() + " connected");
            NetworkCallBackListner connectCallBackListener = this.connectionCallback;
            if (connectCallBackListener != null) {
                connectCallBackListener.hasActiveConnection(true);
            }
        }
        if (intent.getExtras().getBoolean("noConnectivity", false)) {
            Log.d("app", "There's no network connectivity");
            NetworkCallBackListner connectCallBackListener2 = this.connectionCallback;
            if (connectCallBackListener2 != null) {
                connectCallBackListener2.hasActiveConnection(false);
            }
        }
    }

    public void setConnectionCallback(NetworkCallBackListner connectCallBackListener) {
        this.connectionCallback = connectCallBackListener;
    }
}

package com.boloji.videocallchat.NetworkConnection;

import android.util.Log;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

public class CustomObserver implements SdpObserver {
    private String tag = getClass().getCanonicalName();

    public CustomObserver(String str) {
        this.tag += " " + str;
    }

    @Override 
    public void onCreateSuccess(SessionDescription sessionDescription) {
        String str = this.tag;
        Log.d(str, "onCreateSuccess() called with: sessionDescription = [" + sessionDescription + "]");
    }

    @Override 
    public void onSetSuccess() {
        Log.d(this.tag, "onSetSuccess() called");
    }

    @Override 
    public void onCreateFailure(String str) {
        String str2 = this.tag;
        Log.d(str2, "onCreateFailure() called with: s = [" + str + "]");
    }

    @Override 
    public void onSetFailure(String str) {
        String str2 = this.tag;
        Log.d(str2, "onSetFailure() called with: s = [" + str + "]");
    }
}

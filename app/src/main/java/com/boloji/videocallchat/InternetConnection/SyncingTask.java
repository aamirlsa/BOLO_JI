package com.boloji.videocallchat.InternetConnection;

import android.content.Context;
import android.os.AsyncTask;

public class SyncingTask extends AsyncTask<Context, Void, Boolean> {
    private NetworkCallBackListner connectionCallback;

    
    public Boolean doInBackground(Context... contextArr) {
        boolean z = false;
        if (NetworkUtils.isConnectedToInternet(contextArr[0]) && NetworkUtils.hasActiveInternetConnection(contextArr[0])) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    
    public void onPostExecute(Boolean bool) {
        super.onPostExecute(bool);
        NetworkCallBackListner connectCallBackListener = this.connectionCallback;
        if (connectCallBackListener != null) {
            connectCallBackListener.hasActiveConnection(bool.booleanValue());
        }
    }

    public void setConnectionCallback(NetworkCallBackListner connectCallBackListener) {
        this.connectionCallback = connectCallBackListener;
    }
}

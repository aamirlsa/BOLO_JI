package com.boloji.videocallchat.AdmobAds;

import android.content.Context;
import android.content.SharedPreferences;

public class Sharedpreferenceconfig {


    private SharedPreferences sharedpreference;
    private Context context;

    public boolean readpolicyacceptstatus() {
        boolean policyacceptstatus = sharedpreference.getBoolean("policyacceptstatus", false);
        return policyacceptstatus;
    }

    public Sharedpreferenceconfig(Context context) {
        this.context = context;
        sharedpreference = context.getSharedPreferences("policyacceptstatus", Context.MODE_PRIVATE);
    }

    public void writepolicyacceptstatus(boolean policyacceptstatus) {
        SharedPreferences.Editor editor = sharedpreference.edit();
        editor.putBoolean("policyacceptstatus", policyacceptstatus);
        editor.commit();
    }


}

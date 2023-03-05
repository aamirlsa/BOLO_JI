package com.boloji.videocallchat.NetworkConnection;


import android.content.Context;

import com.boloji.videocallchat.TemporaryData.TempSharedpref;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApi {
    private static Retrofit retrofit;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(TempSharedpref.getString(context,TempSharedpref.Server2Baseurl)).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}

package com.boloji.videocallchat.OtherActivities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.webkit.WebView;
import com.firebase.client.Firebase;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;
import com.nhancv.webrtcpeer.rtc_comm.ws.SocketService;
import com.boloji.videocallchat.NetworkConnection.SocketConfig;
import com.boloji.videocallchat.TemporaryData.TempSharedpref;
import com.boloji.videocallchat.TempStorage.AppPref;
import com.boloji.videocallchat.AdmobAds.AppOpenManager;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class AppCheck extends Application {

    public static Context appContext = null;

    private static AppOpenManager appOpenManager;
    public static final String TAG = "ApplicationClass";
    private static AppCheck mInstance;
    private static Socket mSocket;


    public Activity activity;
    public AppPref appPrefs;
    public int isVoice = 0;
    public int random = 2000;
    public SocketService socket;
    public int width = 720;
    public static Socket GetSocket() {
        if (mSocket == null) {
            try {
                mSocket = IO.socket(getInstance().appPrefs.getSocket_url());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        return mSocket;
    }
    public void onTerminate() {
        super.onTerminate();
        SocketConfig.getInstance().DisconnectScoket();
    }
    public static synchronized AppCheck getInstance() {
        AppCheck applicationClass;
        synchronized (AppCheck.class) {
            applicationClass = mInstance;
        }
        return applicationClass;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String process = getProcessName();
            if (getPackageName() != process) WebView.setDataDirectorySuffix(process);
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        Firebase.setAndroidContext(getApplicationContext());

        FirebaseApp.initializeApp(this);

        mInstance = this;
        this.appPrefs = new AppPref(this);
        appOpenManager = new AppOpenManager(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void BindService(Context context) {
        SocketConfig.getInstance().initSocket();
        SocketConfig.getInstance().registerClient(context);
        SocketConfig.getInstance().SocketConnect();
    }
    public void RegisterUser() {
        try {
            String string = Settings.Secure.getString(getContentResolver(), "android_id");
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("DeviceId", string);
            jSONObject.put("app_name", TempSharedpref.getString(getApplicationContext(), TempSharedpref.APPNAME));
            jSONObject.put("package", TempSharedpref.getString(getApplicationContext(), TempSharedpref.PACKAGE));

            jSONObject.put("version", TempSharedpref.getString(getApplicationContext(), TempSharedpref.VERSIONKEY));
            jSONObject.put("d_type", "Android");
            jSONObject.put("kurento", "google1");
            jSONObject.put("user_name", this.appPrefs.getUser());
            jSONObject.put("pp", this.appPrefs.getpp());
            jSONObject.put("login_type", "NoLogin");
            jSONObject.put("social_unique_id", this.appPrefs.getSocialID());
            jSONObject.put("phone_no", "");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("en", "video_call_Mini_App_register");
            jSONObject2.put("data", jSONObject);
            SocketConfig.getInstance().emitCall(jSONObject2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
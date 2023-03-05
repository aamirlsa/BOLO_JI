package com.boloji.videocallchat.Activity;

import static android.Manifest.permission.CAMERA;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nhancv.webrtcpeer.rtc_comm.ws.SocketService;
import com.boloji.videocallchat.NetworkConnection.ApiConstant;
import com.boloji.videocallchat.NetworkConnection.SocketConfig;
import com.boloji.videocallchat.NetworkConnection.SocketListner;
import com.boloji.videocallchat.OtherActivities.AppCheck;
import com.boloji.videocallchat.OtherData.ReSchedualCall;
import com.boloji.videocallchat.OtherData.VariableData;
import com.boloji.videocallchat.R;
import com.boloji.videocallchat.TemporaryData.TempSharedpref;
import com.boloji.videocallchat.TempStorage.AppPref;
import com.boloji.videocallchat.AdmobAds.DataHelprt;
import com.boloji.videocallchat.Configuration.WebConfigData;
import com.boloji.videocallchat.Configuration.WebConstantData;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import io.socket.client.Socket;
import rx.functions.Action1;

public class CallStrangerActivity extends AppCompatActivity implements SocketListner, WebConfigData.WebSocketConnectListener {

    RelativeLayout imgBtnMakeCall, imgBtnMakeCall2,RLMainContainer, viewNew1;
    TextView txtConnect, txtConnect2,privacypolicy;
    ImageView centerImage;
    KProgressHUD hud;
    int MAKECALL = 100;
    RelativeLayout addContact, imgBtnMakeCal2;
    String category = "";
    DataHelprt databaseHelper;
    public static final int PERMISSION_REQUEST_CODE = 200;
     String[] PERMISSIONS_START_CALL = {"android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.READ_PHONE_STATE"};


    String name = "";
    AppPref applicationPreference;
    String userdata = "";
    public static final String[] BLUETOOTH_PERMISSIONS_S = {  Manifest.permission.BLUETOOTH_CONNECT} ;

    public ArrayList<String> getEmojisList() throws IOException {
        String[] list = getAssets().list("emojis");
        ApiConstant.EMOJI_LIST = new ArrayList<>();
        for (String str : list) {
            ApiConstant.EMOJI_LIST.add(str);
        }
        return ApiConstant.EMOJI_LIST;
    }

    private NativeAd mobNativeView;

    private void NativeBinding(NativeAd nativeAd, NativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }

    public void NativeShow(final FrameLayout frameLayout) {
        AdLoader.Builder builder = new AdLoader.Builder(getApplication(), getString(R.string.AdMob_Native));

        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                centerImage.setVisibility(View.GONE);
                boolean isDestroyed = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isDestroyed = isDestroyed();
                }
                if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                    nativeAd.destroy();
                    return;
                }
                if (CallStrangerActivity.this.mobNativeView != null) {
                    CallStrangerActivity.this.mobNativeView.destroy();
                }
                CallStrangerActivity.this.mobNativeView = nativeAd;
                NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.mobnative, null);
                NativeBinding(nativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });
        VideoOptions videoOptions = new VideoOptions.Builder().build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {


            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());


    }

    public void NativeLoad() {
        NativeShow((FrameLayout) findViewById(R.id.mobadslayout));

    }

    KProgressHUD hudads;
    InterstitialAd mMobInterstitialAds;
    public void InterstitialLoad() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("1ADAD30F02CD84CDE72190C2ABE5EB5E")).build();
        MobileAds.setRequestConfiguration(configuration);
        InterstitialAd.load(getApplicationContext(), getString(R.string.AdMob_Interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                CallStrangerActivity.this.mMobInterstitialAds = interstitialAd;
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                            }
                        });
                if (hudads != null) {
                    hudads.dismiss();
                }
                CallStrangerActivity.this.mMobInterstitialAds.show(CallStrangerActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                if (hudads != null) {
                    hudads.dismiss();
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call_stranger);
        centerImage = findViewById(R.id.centerImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PERMISSIONS_START_CALL = new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.READ_PHONE_STATE","android.permission.BLUETOOTH_CONNECT"};
        }
        NativeLoad();
        hudads = KProgressHUD.create(CallStrangerActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
                .setLabel("Ads Loading...")
                .setDetailsLabel("Please wait")
                .setCancellable(true)
                .setDimAmount(0.5f)
                .show();
        InterstitialLoad();


        try {
            getEmojisList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        privacypolicy = findViewById(R.id.privacypolicy);
        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CallStrangerActivity.this, PrivacyPolicy.class));
                finish();
            }
        });

        this.name = userdata;
        Log.d("datauser", "onCreate: " + name);
        txtConnect = findViewById(R.id.txtConnect);
        RLMainContainer = findViewById(R.id.RLMainContainer);
        viewNew1 = findViewById(R.id.viewNew1);
        txtConnect2 = findViewById(R.id.txtConnect2);
        addContact = findViewById(R.id.addContact);
        imgBtnMakeCal2 = findViewById(R.id.imgBtnMakeCal2);
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("cat")) {
                txtConnect.setText("You are join in " + extras.getString("cat") + " Room");
                category = extras.getString("cat");
            }
        } else {
            txtConnect.setText("Click To Make Call");
        }

        if (category.isEmpty()) {
            category = "General";
        }

        databaseHelper = new DataHelprt(CallStrangerActivity.this);

        boolean exits = databaseHelper.checkExistance(category);
        if (!exits) {
            databaseHelper.adddata(category, 0);
        }

        hud = KProgressHUD.create(CallStrangerActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
                .setLabel("Find User...")
                .setDetailsLabel("Please wait")
                .setCancellable(true)
                .setDimAmount(0.5f);


        centerImage = findViewById(R.id.centerImage);
        imgBtnMakeCall = findViewById(R.id.imgBtnMakeCall);
        imgBtnMakeCall2 = findViewById(R.id.imgBtnMakeCall2);

        imgBtnMakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    hud.show();
                    req();
                } else {
                    requestPermission();
                }
            }
        });
        imgBtnMakeCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    hud.show();
                    startActivity(new Intent(CallStrangerActivity.this, RandomVideoCallActivity.class));
                } else {
                    requestPermission();
                }
            }
        });


        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        WebConstantData.data(this);
        CallService();
        this.applicationPreference = new AppPref(this);
        imgBtnMakeCal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    hud.show();
                    req();
                } else {
                    requestPermission();
                }
            }
        });
    }


    private boolean checkPermission() {
        int result4;
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.RECORD_AUDIO");
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_PHONE_STATE");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            result4 = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.BLUETOOTH_CONNECT");
            return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED;

        }
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED ;

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, PERMISSIONS_START_CALL, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAKECALL) {
            if (resultCode == RESULT_OK) {
                RLMainContainer.setVisibility(View.GONE);
                viewNew1.setVisibility(View.VISIBLE);

            }
        }
    }


    @Override
    public void onBackPressed() {

        if (viewNew1.getVisibility() == View.VISIBLE) {
            RLMainContainer.setVisibility(View.VISIBLE);
            viewNew1.setVisibility(View.GONE);
        } else {
            startActivity(new Intent(CallStrangerActivity.this, Start_Activity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WebConfigData.getInstance().SocketConnect(this);
    }

    private void findUser() {
        try {
            hud.dismiss();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("from", this.applicationPreference.getNewRegId());
            jSONObject.put("package", TempSharedpref.getString(getApplicationContext(), TempSharedpref.PACKAGE));
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("en", "video_call_mini_app_video_find_user");
            jSONObject2.put("data", jSONObject);
            SocketConfig.getInstance().emitCall(jSONObject2);
        } catch (Exception e) {
            try {
                e.printStackTrace();
            } catch (Exception e2) {

                Toast.makeText(this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                e2.printStackTrace();
            }
        }
    }

    @Override
    public void call(Object... objArr) {
        JSONObject jSONObject = (JSONObject) objArr[0];
        try {
            if (jSONObject.getString("en").equals("video_call_mini_app_video_find_user")) {
                VariableData.videocounter = 0;
                JSONObject jSONObject2 = jSONObject.getJSONObject("data");
                Log.i("app_video_find_user", VariableData.SocketId);
                if (VariableData.SocketId.equals(jSONObject2.getString("socketid"))) {
                    Log.i("app_video_find_user", jSONObject2.getString("socketid"));
                    ReSchedualCall.runOnUi(new Action1() {


                        @Override
                        public final void call(Object obj) {
                            try {
                                VariableData.SocketId = jSONObject2.getString("socketid");
                                String string = jSONObject2.getString("call_status");
                                if (string.equalsIgnoreCase("0")) {
                                    String newRegId = applicationPreference.getNewRegId();
                                    Intent intent = new Intent(CallStrangerActivity.this, PeerToPeerActivity.class);
                                    intent.putExtra("from", newRegId);
                                    intent.putExtra("to", "");
                                    startActivity(intent);

                                } else if (string.equalsIgnoreCase("1")) {
                                    String newRegId2 = applicationPreference.getNewRegId();
                                    String string2 = jSONObject2.getString("to");
                                    Intent intent2 = new Intent(CallStrangerActivity.this, PeerToPeerActivity.class);
                                    intent2.putExtra("from", newRegId2);
                                    intent2.putExtra("to", string2);
                                    startActivity(intent2);

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            removeCallService();
                            Toast.makeText(getApplicationContext(), "Server problem please restart app!!", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(CallStrangerActivity.this, Start_Activity.class);

                            startActivity(intent2);

                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallService() {
        SocketConfig.getInstance().registerClient(this);
        SocketConfig.getInstance().SocketConnect();
    }

    @Override
    public void GetChat(Socket socket) {
        if (!VariableData.SocketId.equals(socket.id())) {
            AppCheck.getInstance().RegisterUser();
        }
    }

    @Override
    public void WebSocketConnected(SocketService socketService) {
        AppCheck.getInstance().socket = socketService;
    }


    public void removeCallService() {
        SocketConfig.getInstance().UnregisterClient(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removeCallService();
    }

    public void req() {
        findUser();
    }


}
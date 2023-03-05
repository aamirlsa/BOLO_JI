package com.boloji.videocallchat.Activity;

import static android.Manifest.permission.CAMERA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.boloji.videocallchat.R;
import com.boloji.videocallchat.AdmobAds.SharedPref;
import com.boloji.videocallchat.AdmobAds.helper;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;
import java.util.Random;

public class Second_Start_Activity extends AppCompatActivity {
    RelativeLayout videocall,groupchat;

    public static final int PERMISSION_REQUEST_CODE = 200;
    String device_id = "";
    TelephonyManager telephonyManager;
    int[] imageIdsmorning = {R.drawable.un1, R.drawable.un2, R.drawable.un3,R.drawable.un4,R.drawable.un5,R.drawable.un6};
    int[] imageIdsnight ={R.drawable.un1, R.drawable.un2, R.drawable.un3,R.drawable.un4,R.drawable.un5,R.drawable.un6};
    int[] imageIdafternoon = {R.drawable.un1, R.drawable.un2, R.drawable.un3,R.drawable.un4,R.drawable.un5,R.drawable.un6};
    int[] imageIdnoon = {R.drawable.un1, R.drawable.un2, R.drawable.un3,R.drawable.un4,R.drawable.un5,R.drawable.un6};

    TextView titlewelcome;
    private RoundedImageView iv_materia;

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                }
                break;
            case 300:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    device_id = telephonyManager.getImei();
                    if (!device_id.isEmpty()) {
                        checkExistance(device_id);
                    }
                }
                break;
            default:
                break;
        }
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

                boolean isDestroyed = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isDestroyed = isDestroyed();
                }
                if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                    nativeAd.destroy();
                    return;
                }
                if (Second_Start_Activity.this.mobNativeView != null) {
                    Second_Start_Activity.this.mobNativeView.destroy();
                }
                Second_Start_Activity.this.mobNativeView = nativeAd;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second_start);

        NativeLoad();
        titlewelcome = findViewById(R.id.title_welcome);
        iv_materia = findViewById(R.id.iv_materiala);


        videocall = findViewById(R.id.videocall);
        groupchat = findViewById(R.id.groupchat);

        titlewelcome.setText(getTimeOfTheDay());

        videocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Second_Start_Activity.this, SelectGenderActivity.class));
                finish();
            }
        });

        groupchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    if (SharedPref.getString(Second_Start_Activity.this, SharedPref.DEVICE_ID).isEmpty()) {
                        telephonyManager = (TelephonyManager) Second_Start_Activity.this.getSystemService(TELEPHONY_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                            } else {
                                if (helper.checkPermissionForIMEI(Second_Start_Activity.this)) {
                                    device_id = telephonyManager.getImei();
                                } else {
                                    helper.requestPermissionForIMEI(Second_Start_Activity.this, 300);
                                }
                            }
                            if (!device_id.isEmpty()) {
                                checkExistance(device_id);
                            }
                        } else {
                            device_id = telephonyManager.getDeviceId();
                            checkExistance(device_id);
                        }
                    } else {
                        goChat();
                    }
                } else {
                    requestPermission();
                }
            }
        });


    }

    private void goChat() {
        if (SharedPref.getString(Second_Start_Activity.this, SharedPref.ID).isEmpty()) {
            startActivity(new Intent(Second_Start_Activity.this, ChatLoginActivity.class));
        } else {
            startActivity(new Intent(Second_Start_Activity.this, PeopleListActivity.class));

        }
    }

    public void checkExistance(String id) {
        Log.v(":::device_id", id);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = rootRef.collection("users").document(id);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                        DocumentReference docIdRef = rootRef.collection("users")
                                .document(id);
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();

                                    SharedPref.setString(Second_Start_Activity.this, SharedPref.NAME, String.valueOf(document.get("name")));
                                    SharedPref.setString(Second_Start_Activity.this, SharedPref.AGE, String.valueOf(document.get("age")));
                                    SharedPref.setString(Second_Start_Activity.this, SharedPref.ID, String.valueOf(document.get("id")));
                                    SharedPref.setString(Second_Start_Activity.this, SharedPref.GENDER, String.valueOf(document.get("gender")));
                                    SharedPref.setString(Second_Start_Activity.this, SharedPref.COUNTRY, String.valueOf(document.get("country")));
                                    SharedPref.setString(Second_Start_Activity.this, SharedPref.DEVICE_ID, String.valueOf(document.get("device_id")));
                                    SharedPref.setString(Second_Start_Activity.this, SharedPref.LAST_VIDEO_TIMESTAMP, String.valueOf(document.get("last_video_timestamp")));

                                    startActivity(new Intent(Second_Start_Activity.this, PeopleListActivity.class));
                                } else {
                                    Log.e("fail", "onComplete: fail task");
                                }
                            }
                        });
                    } else {
                        SharedPref.setString(Second_Start_Activity.this, SharedPref.DEVICE_ID, id);
                        goChat();
                    }
                } else {
                    Log.e("fail", "onComplete: fail task");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Second_Start_Activity.this, Start_Activity.class));
        finish();
    }

    private String getTimeOfTheDay() {
        String message = getString(R.string.title_good_day);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        int[] images = new int[]{};
        if (timeOfDay >= 0 && timeOfDay < 6) {
            message = getString(R.string.title_good_night);
            images=imageIdsnight;



        } else if (timeOfDay >= 6 && timeOfDay < 12) {
            message = getString(R.string.title_good_morning);
            images=imageIdsmorning;


        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            message = getString(R.string.title_good_afternoon);
            images = imageIdnoon;


        } else if (timeOfDay >= 16 && timeOfDay < 20) {
            message = getString(R.string.title_good_evening);
            images=imageIdafternoon;



        } else if (timeOfDay >= 20 && timeOfDay < 24) {
            message = getString(R.string.title_good_night);
            images=imageIdsnight;
        }
        int day = images[new Random().nextInt(images.length)];
        loadTimeImage(day);
        return message;
    }

    private void loadTimeImage(int day) {
        Glide.with(this)
                .load(day)
                .placeholder(R.drawable.aamdefault)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(iv_materia);
    }
}
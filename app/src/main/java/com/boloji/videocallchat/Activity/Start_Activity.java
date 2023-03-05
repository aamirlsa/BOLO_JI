package com.boloji.videocallchat.Activity;



import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.boloji.videocallchat.NetworkConnection.ApiConstant;
import com.boloji.videocallchat.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.boloji.videocallchat.TemporaryData.TempSharedpref;
import com.boloji.videocallchat.AdmobAds.Sharedpreferenceconfig;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;
import java.util.Random;


public class Start_Activity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    int[] imageIdsmorning = {R.drawable.un1, R.drawable.un2, R.drawable.un3,R.drawable.un4,R.drawable.un5,R.drawable.un6};
    int[] imageIdsnight = {R.drawable.un1, R.drawable.un2, R.drawable.un3,R.drawable.un4,R.drawable.un5,R.drawable.un6};
    int[] imageIdafternoon = {R.drawable.un1, R.drawable.un2, R.drawable.un3,R.drawable.un4,R.drawable.un5,R.drawable.un6};
    int[] imageIdnoon = {R.drawable.un1, R.drawable.un2, R.drawable.un3,R.drawable.un4,R.drawable.un5,R.drawable.un6};

    TextView titlewelcome;
    private RoundedImageView iv_materia;
    CardView aboutuss, privacypolicy, rateus,settings;

    Button start;
    RelativeLayout mainlayout;
    Sharedpreferenceconfig sharedpreferenceconfig;
    Dialog dialog;


    public void policy() {
        TextView textView, textView2, txtlink, txttitle, txt_info;
        View inflate = ((LayoutInflater) Start_Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.policyrules, null);

        textView = inflate.findViewById(R.id.txtcancel);
        textView2 = inflate.findViewById(R.id.txtaccept);
        txtlink = inflate.findViewById(R.id.txtlink);
        txttitle = inflate.findViewById(R.id.txttitle);
        txt_info = inflate.findViewById(R.id.txt_info);

        txt_info.setText(Html.fromHtml(
                "<h1>Privacy Policy of Followers Assistant Mass unfolow team</h1>\n" +
                        "\n" +
                        "<p>Followers Assistant Mass unfolow team operates the https://sites.google.com/view/bolojilivevideochat/home website, which provides the SERVICE.</p>\n" +
                        "\n" +
                        "<p>This page is used to inform website visitors regarding our policies with the collection, use, and disclosure of Personal Information if anyone decided to use our Service, the BOLO JI LIVE VIDEO CHAT website.</p>\n" +
                        "\n" +
                        "<p>If you choose to use our Service, then you agree to the collection and use of information in relation with this policy. The Personal Information that we collect are used for providing and improving the Service. We will not use or share your information with anyone except as described in this Privacy Policy.</p>\n" +
                        "\n" +
                        "<p>The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which is accessible at https://sites.google.com/view/bolojilivevideochat/home, unless otherwise defined in this Privacy Policy.</p>\n" +
                        "\n" +
                        "<h2>Information Collection and Use</h2>\n" +
                        "\n" +
                        "<p>For a better experience while using our Service, we may require you to provide us with certain personally identifiable information, including but not limited to your name, phone number, and postal address. The information that we collect will be used to contact or identify you.</p>\n" +
                        "\n" +
                        "<h2>Log Data</h2>\n" +
                        "\n" +
                        "<p>We want to inform you that whenever you visit our Service, we collect information that your browser sends to us that is called Log Data. This Log Data may include information such as your computer’s Internet Protocol (\"IP\") address, browser version, pages of our Service that you visit, the time and date of your visit, the time spent on those pages, and other statistics.</p>\n" +
                        "\n" +
                        "<h2>Cookies</h2>\n" +
                        "\n" +
                        "<p>Cookies are files with small amount of data that is commonly used an anonymous unique identifier. These are sent to your browser from the website that you visit and are stored on your computer’s hard drive.</p>\n" +
                        "\n" +
                        "<p>Our website uses these \"cookies\" to collection information and to improve our Service. You have the option to either accept or refuse these cookies, and know when a cookie is being sent to your computer. If you choose to refuse our cookies, you may not be able to use some portions of our Service.</p>\n" +
                        "\n" +
                        "<h2>Service Providers</h2>\n" +
                        "\n" +
                        "<p>We may employ third-party companies and individuals due to the following reasons:</p>\n" +
                        "\n" +
                        "<ul>\n" +
                        "    <li>To facilitate our Service;</li>\n" +
                        "    <li>To provide the Service on our behalf;</li>\n" +
                        "    <li>To perform Service-related services; or</li>\n" +
                        "    <li>To assist us in analyzing how our Service is used.</li>\n" +
                        "</ul>\n" +
                        "\n" +
                        "<p>We want to inform our Service users that these third parties have access to your Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose.</p>\n" +
                        "\n" +
                        "<h2>Security</h2>\n" +
                        "\n" +
                        "<p>We value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and we cannot guarantee its absolute security.</p>\n" +
                        "\n" +
                        "<h2>Links to Other Sites</h2>\n" +
                        "\n" +
                        "<p>Our Service may contain links to other sites. If you click on a third-party link, you will be directed to that site. Note that these external sites are not operated by us. Therefore, we strongly advise you to review the Privacy Policy of these websites. We have no control over, and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.</p>\n" +
                        "\n" +
                        "<p>Children's Privacy</p>\n" +
                        "\n" +
                        "<p>Our Services do not address anyone under the age of 13. We do not knowingly collect personal identifiable information from children under 13. In the case we discover that a child under 13 has provided us with personal information, we immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact us so that we will be able to do necessary actions.</p>\n" +
                        "\n" +
                        "<h2>Changes to This Privacy Policy</h2>\n" +
                        "\n" +
                        "<p>We may update our Privacy Policy from time to time. Thus, we advise you to review this page periodically for any changes. We will notify you of any changes by posting the new Privacy Policy on this page. These changes are effective immediately, after they are posted on this page.</p>\n" +
                        "\n" +
                        "<p>Our Privacy Policy was created with the help of the <a href=\"https://www.termsfeed.com/blog/sample-privacy-policy-template/\">TermsFeed Privacy Policy Template</a>.</p>\n" +
                        "\n" +
                        "<h2>Contact Us</h2>\n" +
                        "\n" +
                        "<p>If you have any questions or suggestions about our Privacy Policy, do not hesitate to contact us.</p>"));
        txttitle.setText(Html.fromHtml("<u>Privacy Policy<u/>"));
        txtlink.setText(Html.fromHtml("If you click on Accept, you acknowledge that it makes the content present and all the content of out Terms of Service and implies that you have read our"));
        txtlink.setMovementMethod(LinkMovementMethod.getInstance());


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Start_Activity.this);
        builder.setView(inflate);
        builder.setCancelable(false);
        final android.app.AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sharedpreferenceconfig.writepolicyacceptstatus(true);
                mainlayout.setVisibility(View.VISIBLE);
                create.dismiss();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Start_Activity.this.finish();
                create.dismiss();
            }
        });
        create.show();
    }

    public void checkads() {
        try {


        TempSharedpref.setString(getApplicationContext(), TempSharedpref.APIURL, ApiConstant.decryptMsg(ApiConstant.parseHexStr2Byte(ApiConstant.s1url), ApiConstant.appkey));
        TempSharedpref.setString(getApplicationContext(), TempSharedpref.PACKAGE, ApiConstant.decryptMsg(ApiConstant.parseHexStr2Byte(ApiConstant.pname), ApiConstant.appkey));
          TempSharedpref.setString(getApplicationContext(), TempSharedpref.VERSIONKEY, ApiConstant.decryptMsg(ApiConstant.parseHexStr2Byte(ApiConstant.vkey), ApiConstant.appkey));
         TempSharedpref.setString(getApplicationContext(), TempSharedpref.APPNAME, ApiConstant.decryptMsg(ApiConstant.parseHexStr2Byte(ApiConstant.aname), ApiConstant.appkey));
         TempSharedpref.setString(getApplicationContext(), TempSharedpref.Server2Baseurl, ApiConstant.decryptMsg(ApiConstant.parseHexStr2Byte(ApiConstant.s2base), ApiConstant.appkey));
         TempSharedpref.setString(getApplicationContext(), TempSharedpref.Server2SocketURL, ApiConstant.decryptMsg(ApiConstant.parseHexStr2Byte(ApiConstant.s2socket), ApiConstant.appkey));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    KProgressHUD hudads;
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
                if (Start_Activity.this.mobNativeView != null) {
                    Start_Activity.this.mobNativeView.destroy();
                }
                Start_Activity.this.mobNativeView = nativeAd;
                NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.mobnative, null);
                NativeBinding(nativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });
        VideoOptions videoOptions = new VideoOptions.Builder().build();
        com.google.android.gms.ads.nativead.NativeAdOptions adOptions = new com.google.android.gms.ads.nativead.NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_start);
        titlewelcome = findViewById(R.id.title_welcom);
        iv_materia = findViewById(R.id.iv_materia);

        hudads = KProgressHUD.create(Start_Activity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
                .setLabel("Checking Connection...")
                .setDetailsLabel("Please wait")
                .setCancellable(true)
                .setDimAmount(0.5f);

        checkads();

        NativeLoad();
        titlewelcome.setText(getTimeOfTheDay());

        settings=findViewById(R.id.setting);

        rateus = findViewById(R.id.rateus);
        aboutuss=findViewById(R.id.aboutus);

        privacypolicy = findViewById(R.id.privacypolicy);

        aboutuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAboutDialog();

            }
        });


        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRateDialogForRate();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Start_Activity.this, SettingActivity.class));
            }
        });


        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Start_Activity.this, PrivacyPolicy.class));
                finish();
            }
        });
        start = findViewById(R.id.start);
        mainlayout = findViewById(R.id.mainlayout);
        sharedpreferenceconfig = new Sharedpreferenceconfig(this);
        if (!sharedpreferenceconfig.readpolicyacceptstatus()) {
            policy();
        } else {
            mainlayout.setVisibility(View.VISIBLE);
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "double tap to exit!", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

    }


    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(Start_Activity.this, "android.permission.CAMERA") + ContextCompat.checkSelfPermission(Start_Activity.this, "android.permission.READ_EXTERNAL_STORAGE") + ContextCompat.checkSelfPermission(Start_Activity.this, "android.permission.WRITE_EXTERNAL_STORAGE") + ContextCompat.checkSelfPermission(Start_Activity.this, "android.permission.READ_PHONE_STATE") + ContextCompat.checkSelfPermission(Start_Activity.this, "android.permission.MODIFY_AUDIO_SETTINGS") + ContextCompat.checkSelfPermission(Start_Activity.this, "android.permission.RECORD_AUDIO")  == 0) {

            startActivity(new Intent(Start_Activity.this, Second_Start_Activity.class));
            Start_Activity.this.finish();
            Log.e("Permission", "Permissions already granted");
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(Start_Activity.this, "android.permission.CAMERA") || ActivityCompat.shouldShowRequestPermissionRationale(Start_Activity.this, "android.permission.READ_EXTERNAL_STORAGE") || ActivityCompat.shouldShowRequestPermissionRationale(Start_Activity.this, "android.permission.WRITE_EXTERNAL_STORAGE") || ActivityCompat.shouldShowRequestPermissionRationale(Start_Activity.this, "android.permission.READ_PHONE_STATE") || ActivityCompat.shouldShowRequestPermissionRationale(Start_Activity.this, "android.permission.MODIFY_AUDIO_SETTINGS") || ActivityCompat.shouldShowRequestPermissionRationale(Start_Activity.this, "android.permission.RECORD_AUDIO") ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Start_Activity.this);
            builder.setMessage("Camera, Read and Write External,Audio Storage permissions are required to do the task.");
            builder.setTitle("Please grant those permissions");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(Start_Activity.this, new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.MODIFY_PHONE_STATE", "android.permission.RECORD_AUDIO"}, 123);
                }
            });
            builder.setNeutralButton("Cancel", (DialogInterface.OnClickListener) null);
            builder.create().show();
        } else {
            ActivityCompat.requestPermissions(Start_Activity.this, new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.MODIFY_PHONE_STATE", "android.permission.RECORD_AUDIO"}, 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 123) {
            if (iArr.length <= 0 || iArr[0] + iArr[1] + iArr[2] + iArr[3] + iArr[4] + iArr[5] != 0) {
                Log.e("Permission", "Permissions denied");
                return;
            }
            startActivity(new Intent(Start_Activity.this, Second_Start_Activity.class));

            Start_Activity.this.finish();
        }
    }

    public void showRateDialogForRate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Start_Activity.this)
                .setTitle("Rate application")
                .setMessage("Please, rate the app at PlayMarket")
                .setPositiveButton("RATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                        }


                    }
                })
                .setNegativeButton("CANCEL", null);
        builder.show();
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

    private void showAboutDialog() {
        final Dialog dialog = new Dialog(Start_Activity.this, R.style.DialogCustomTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_about);

        Button dialog_btn=dialog.findViewById(R.id.btn_done);
        dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
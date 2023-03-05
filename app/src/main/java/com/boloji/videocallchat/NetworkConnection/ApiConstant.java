package com.boloji.videocallchat.NetworkConnection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.boloji.videocallchat.OtherActivities.AppCheck;

import org.webrtc.PeerConnection;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ApiConstant {

    static {
        System.loadLibrary("hello-jni");
    }
    public static String AUDIO_MODE = "audio_mode";
    public static String EMOJI_COUNTER = "emji_count";
    public static ArrayList<String> EMOJI_LIST = new ArrayList<>();
    public static String GENDER_PREF_KEY = "gender";
    public static String ISLOADED_PREF_KEY = "isLoaded";
    public static String IS_FIRST_PREF_KEY = "is_first";
    public static String NAME_PREF_KEY = AppMeasurementSdk.ConditionalUserProperty.NAME;
    public static String PREF_NAME = "PREF_VIDEO";
    public static String TAG = "LiveTalk";
    public static LinkedList<PeerConnection.IceServer> iceServers_Static;
    public static boolean isBlockingAllow = true;
    public static String local_video_url = "";
    public static String opponet_device_id;

    public static native String aname();
    public static String aname=aname();

    public static native String pname();
    public static String pname=pname();

    public static native String s2base();
    public static String s2base=s2base();

    public static native String s2socket();
    public static String s2socket=s2socket();

    public static native String vkey();
    public static String vkey=vkey();

    public static native String s1url();
    public static String s1url=s1url();

    public static native String appkey();
    public static String appkey=appkey();

    public static native String ivkey();
    public static String ivkey=ivkey();

        public static byte[] parseHexStr2Byte(String hexStr) {
            if (hexStr.length() < 1)
                return null;
            byte[] result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; i++) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1),
                        16);
                int low = Integer.parseInt(
                        hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        }

    public static String decryptMsg(byte[] cipherText, String  secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {

        try {
            IvParameterSpec iv = new IvParameterSpec(ivkey.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(secret.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(cipherText);

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
    public static void privacyPolicy(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Privacy Policy");
        builder.setCancelable(false);
        WebView webView = new WebView(context);

        webView.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                webView.loadUrl(str);
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                webView.loadUrl(webResourceRequest.getUrl().toString());
                return false;
            }
        });
        builder.setView(webView);
        builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static String getDeviceId() {
        return Settings.Secure.getString(AppCheck.getInstance().getContentResolver(), "android_id");
    }



    public static int getRandomNoBetweenTwoNo(int i, int i2) {
        return new Random().nextInt(i2 - i) + i;
    }

    public static boolean isFirstRunApp(Context context) {
        if (context == null) {
            context = AppCheck.getInstance();
        }
        return context.getSharedPreferences(PREF_NAME, 0).getBoolean(IS_FIRST_PREF_KEY, true);
    }

    public static void isFirstRunAppUpdate(Context context, boolean z) {
        if (context == null) {
            context = AppCheck.getInstance();
        }
        context.getSharedPreferences(PREF_NAME, 0).edit().putBoolean(IS_FIRST_PREF_KEY, z).apply();
    }

    public static void updateNameAndGender(Context context, int i, String str) {
        if (context == null) {
            context = AppCheck.getInstance();
        }
        context.getSharedPreferences(PREF_NAME, 0).edit().putInt(GENDER_PREF_KEY, i).putString(NAME_PREF_KEY, str).apply();
    }

    public static String getUserName(Context context) {
        if (context == null) {
            context = AppCheck.getInstance();
        }
        return context.getSharedPreferences(PREF_NAME, 0).getString(NAME_PREF_KEY, "");
    }

    public static int getGender(Context context) {
        if (context == null) {
            context = AppCheck.getInstance();
        }
        return context.getSharedPreferences(PREF_NAME, 0).getInt(GENDER_PREF_KEY, 1);
    }

    public static boolean isLoaded(Context context) {
        if (context == null) {
            context = AppCheck.getInstance();
        }
        return context.getSharedPreferences(PREF_NAME, 0).getBoolean(ISLOADED_PREF_KEY, false);
    }

    public static void isLoadedUpdate(Context context, boolean z) {
        if (context == null) {
            context = AppCheck.getInstance();
        }
        context.getSharedPreferences(PREF_NAME, 0).edit().putBoolean(ISLOADED_PREF_KEY, z).apply();
    }

    public static void updateEmojiCounter(Context context, int i) {
        if (context == null) {
            context = AppCheck.getInstance();
        }
        context.getSharedPreferences(PREF_NAME, 0).edit().putInt(EMOJI_COUNTER, i).apply();
    }

    public static int getEmojiCounter(Context context) {
        if (context == null) {
            context = AppCheck.getInstance();
        }
        return context.getSharedPreferences(PREF_NAME, 0).getInt(EMOJI_COUNTER, 5);
    }

    public static boolean isAvailable(Intent intent, Context context) {
        return context.getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }

    public static boolean isSpeakerEnable(Context context) {
        if (context == null) {
            context = AppCheck.getInstance();
        }
        return context.getSharedPreferences(PREF_NAME, 0).getBoolean(AUDIO_MODE, true);
    }

    public static void updateAudioMode(Context context, boolean z) {
        if (context == null) {
            context = AppCheck.getInstance();
        }
        context.getSharedPreferences(PREF_NAME, 0).edit().putBoolean(AUDIO_MODE, z).apply();
    }
}

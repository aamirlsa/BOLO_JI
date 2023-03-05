package com.boloji.videocallchat.AdmobAds;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class helper {

    public static boolean checkPermissionForIMEI(Context context) {
        int result1 = ContextCompat.checkSelfPermission(context, READ_PHONE_STATE);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissionForIMEI(Activity context, int code) {
        ActivityCompat.requestPermissions(context, new String[]{READ_PHONE_STATE}, code);
    }

    public static String getTimeforChat(String timeStamp) {
        long timestampString = Long.parseLong(timeStamp);
        String value = new java.text.SimpleDateFormat("h:mm a").format(new java.util.Date(timestampString * 1000));
        return value;
    }

    public static String getDate(String timeStamp) {
        long timestampString = Long.parseLong(timeStamp);
        String value = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(timestampString * 1000));
        return value;
    }

    public static int getRandomChatNumber() {
        int min = 2000;
        int max = 5000;

        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        return i1;
    }
    public static boolean checkPermission(Context context) {
        int result1 = ContextCompat.checkSelfPermission(context, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;

    }

    public static void requestPermission(Activity context, int code) {
        ActivityCompat.requestPermissions(context, new String[]{CAMERA}, code);
    }
}

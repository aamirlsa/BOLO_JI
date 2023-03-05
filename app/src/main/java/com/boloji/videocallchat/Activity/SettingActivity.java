package com.boloji.videocallchat.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.boloji.videocallchat.R;

import java.io.File;
import java.text.DecimalFormat;

public class SettingActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    TextView textView, textViewversion,textcontact,textmoreapp,textshareapp,textrate;
    long size = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);


        init();
        initializeCache();


        textcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contactUS();


            }
        });

        textmoreapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_more_apps))));
            }
        });

        textshareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareUS();
            }
        });

        textrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionRateUs();
            }
        });




        textView.setText(" "  + readableFileSize(size));


        initaction();

        setValues();

    }

    private void shareUS() {


        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", "http://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(intent);
    }

    private void actionRateUs() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("market://details?id=" + getPackageName()));
        startActivity(intent);
    }


    private void initaction() {

        this.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    deleteCache(getApplicationContext());

                    initializeCache();
                }catch (Exception ex){

                }

            }
        });

    }

    public void init(){

        linearLayout=findViewById(R.id.linear_layout_clea_cache);
        textView=findViewById(R.id.text_view_cache_value);
        textViewversion=findViewById(R.id.tv_version);
        textcontact=findViewById(R.id.tv_contact);
        textmoreapp=findViewById(R.id.tv_moreapp);
        textshareapp=findViewById(R.id.tv_shareapp);
        textrate=findViewById(R.id.tv_rateapp);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
            Toast.makeText(context, "Cache is cleaned", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {}
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    private void initializeCache() {
        size = 0;
        size += getDirSize(this.getCacheDir());
        size += getDirSize(this.getExternalCacheDir());
        textView.setText(" "  + readableFileSize(size));


    }

    public long getDirSize(File dir){
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0 Bytes";
        final String[] units = new String[]{"Bytes", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    private void setValues() {

        try {
            PackageInfo pInfo =getPackageManager().getPackageInfo(getPackageName(),0);
            String version = pInfo.versionName;
            textViewversion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void contactUS() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, getResources().getString(R.string.contact_us_email));
        intent.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.extra_subject) );
        intent.putExtra(Intent.EXTRA_TEXT,getResources().getString(R.string.extra_text)) ;
        try {
            startActivity(Intent.createChooser(intent, "Send mail"));
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
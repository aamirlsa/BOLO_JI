package com.boloji.videocallchat.Configuration;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.boloji.videocallchat.BuildConfig;
import com.boloji.videocallchat.OtherActivities.AppCheck;
import com.boloji.videocallchat.TempStorage.AppPref;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class WebConstantData {

    public static String[] PERMISSIONS = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    public static String currentPath = CommonClassPath("/.crtFile");

    private static String CommonClassPath(String str) {
        if (Build.VERSION.SDK_INT >= 28) {
            return AppCheck.getInstance().getExternalFilesDir(str).getAbsolutePath();
        }
        return Environment.getExternalStorageDirectory() + "/Android/data/" + BuildConfig.APPLICATION_ID  + str;
    }

    public static boolean hasPermissions(Context context, String... strArr) {
        if (Build.VERSION.SDK_INT < 23 || context == null || strArr == null) {
            return true;
        }
        for (String str : strArr) {
            if (ActivityCompat.checkSelfPermission(context, str) != 0) {
                return false;
            }
        }
        return true;
    }

    public static void data(Activity activity) {
        Log.i("WebSocketConstData","data");
        AppPref applicationPreference = new AppPref(activity);
        String substring = applicationPreference.getCertificateFile().substring(applicationPreference.getCertificateFile().lastIndexOf(47) + 1);
        File file = new File(currentPath + "/" + substring);
        if (!applicationPreference.getCertificateFlagOld().equals(applicationPreference.getCertificateFlagNew()) || !file.exists()) {
            DashboardCustomizeImagesLanding(activity, applicationPreference.getCertificateFile());
        }

    }


    private static void DashboardCustomizeImagesLanding(Activity r4, String r5) {
        boolean z;
        Log.i("WebSocketConstData","DashboardCustomizeImagesLanding");
        if (hasPermissions(r4, PERMISSIONS)) {
            Log.i("WebSocketConstData","PERMISSIONS");
            File file = new File(currentPath);
            try {
                        if(!file.exists())
                            file.mkdirs();
                  //  z = file.mkdirs();

                        String substring = r5.substring(r5.lastIndexOf(47) + 1);
                        String str2 = currentPath + "/" + substring;
                        Log.e("TAG-fileName", substring);
                        Log.e("TAG-zipFile", str2);
                        new DashboardZipDownload(r4, str2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, r5);
                        return;



            } catch (Exception e) {
                Log.e("TAG-Test", "doInBackground: CReated not=== : " + e.getMessage());
            }
            z = true;
            if (!z) {
            }
        }
    }


    public static class DashboardZipDownload extends AsyncTask<String, String, String> {
        private boolean flag = false;
        private String zipFile;

        DashboardZipDownload(Activity activity, String str) {
            this.zipFile = str;
        }


        public void onPreExecute() {
            super.onPreExecute();
        }


        public String doInBackground(String... strArr) {
            Log.i("WebSocketConstData","doInBackground");
            try {
                URL url = new URL(strArr[0]);
                URLConnection openConnection = url.openConnection();
                openConnection.connect();
                int contentLength = openConnection.getContentLength();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(this.zipFile);
                byte[] bArr = new byte[1024];
                long j = 0;
                while (true) {
                    int read = bufferedInputStream.read(bArr);
                    if (read != -1) {
                        j += (long) read;
                        publishProgress("" + ((int) ((100 * j) / ((long) contentLength))));
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        fileOutputStream.close();
                        bufferedInputStream.close();
                        this.flag = true;
                        return null;
                    }
                }
            } catch (Exception e) {
                this.flag = false;
                Log.e("TAG-Test", "doInBackground:11111 " + e.getMessage());
                return null;
            }
        }


        public void onProgressUpdate(String... strArr) {
            Log.e("onProgressUpdate", "" + strArr[0] + " %");
        }


        public void onPostExecute(String str) {
            Log.i("WebSocketConstData","onPostExecute");
            if (this.flag) {
                AppCheck.getInstance().appPrefs.setCertificateFlagOld(AppCheck.getInstance().appPrefs.getCertificateFlagNew());
            }
        }
    }
}

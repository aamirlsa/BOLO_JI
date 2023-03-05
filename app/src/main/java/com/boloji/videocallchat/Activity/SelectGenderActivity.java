package com.boloji.videocallchat.Activity;


import static android.Manifest.permission.CAMERA;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.boloji.videocallchat.NetworkConnection.SocketConfig;
import com.boloji.videocallchat.NetworkConnection.SocketListner;
import com.boloji.videocallchat.OtherActivities.AppCheck;
import com.boloji.videocallchat.OtherData.ReSchedualCall;
import com.boloji.videocallchat.OtherData.VariableData;
import com.boloji.videocallchat.R;
import com.boloji.videocallchat.TemporaryData.TempSharedpref;
import com.boloji.videocallchat.TempStorage.AppPref;
import com.boloji.videocallchat.Configuration.WebConstantData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.socket.client.Socket;
import rx.functions.Action1;


public class SelectGenderActivity extends AppCompatActivity implements SocketListner {

    RelativeLayout menborder, menbg, womenborder, womenbg, mainmen, mainwomen;
    Button next;
    AppPref applicationPreference;
    String requestBody;
    private boolean isFirstTimeFlag = false;
    Socket socket;
    public static final int PERMISSION_REQUEST_CODE = 200;
    String device_id = "";
    TelephonyManager telephonyManager;
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

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_gender);

        next = findViewById(R.id.next);
        menborder = findViewById(R.id.menborder);
        menbg = findViewById(R.id.menbg);
        womenborder = findViewById(R.id.womenborder);
        womenbg = findViewById(R.id.womenbg);
        mainmen = findViewById(R.id.mainmen);
        mainwomen = findViewById(R.id.mainwomen);

        mainmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menbg.getVisibility() == View.VISIBLE) {

                    menbg.setVisibility(View.GONE);
                    menborder.setVisibility(View.VISIBLE);
                    womenbg.setVisibility(View.VISIBLE);
                    womenborder.setVisibility(View.GONE);

                } else {
                    womenbg.setVisibility(View.VISIBLE);
                    womenborder.setVisibility(View.GONE);
                    menbg.setVisibility(View.GONE);
                    menborder.setVisibility(View.VISIBLE);

                }

            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mainwomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (womenbg.getVisibility() == View.VISIBLE) {
                    menbg.setVisibility(View.VISIBLE);
                    menborder.setVisibility(View.GONE);
                    womenbg.setVisibility(View.GONE);
                    womenborder.setVisibility(View.VISIBLE);
                } else {
                    menbg.setVisibility(View.VISIBLE);
                    menborder.setVisibility(View.GONE);
                    womenbg.setVisibility(View.GONE);
                    womenborder.setVisibility(View.VISIBLE);
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    startActivity(new Intent(SelectGenderActivity.this, VideoCallLoginActivity.class));

                    finish();
                    requestPermission();
                }

            }
        });
        AppPref applicationPreference2 = new AppPref(this);
        this.applicationPreference = applicationPreference2;
        if (!this.isFirstTimeFlag) {
            getData(TempSharedpref.getString(getApplicationContext(), TempSharedpref.APIURL));
        }
    }

    private void getData(String str) {
        Log.i("getData", str.toString());
        this.isFirstTimeFlag = true;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("package", TempSharedpref.getString(getApplicationContext(), TempSharedpref.PACKAGE));
            this.requestBody = jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Uri.Builder buildUpon = Uri.parse(str).buildUpon();
        Log.e("URL", buildUpon.toString());
        JsonObjectRequest r1 = new JsonObjectRequest(1, buildUpon.toString(), null, new Response.Listener<JSONObject>() {


            public void onResponse(JSONObject jSONObject) {
                try {
                    Log.e("res", jSONObject + "");
                    VariableData.CallSocketInt = 0;
                    Log.e("URL", "===" + jSONObject.getString("URL") + ":" + jSONObject.getString("PORT"));
                    AppPref applicationPreference = SelectGenderActivity.this.applicationPreference;
                    applicationPreference.setSocket_url(jSONObject.getString("URL") + ":" + jSONObject.getString("PORT"));
                    SelectGenderActivity.this.CallService();
                } catch (Exception e) {
                    VariableData.CallSocketInt = 1;
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError volleyError) {
                SelectGenderActivity.this.CallService();
                volleyError.printStackTrace();
                NetworkResponse networkResponse = volleyError.networkResponse;
                boolean z = volleyError instanceof ServerError;
                if (z && networkResponse != null) {
                    try {
                        new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                        Log.e("", "");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
                if (!(volleyError instanceof NetworkError) && !z && !(volleyError instanceof AuthFailureError) && !(volleyError instanceof ParseError) && !(volleyError instanceof NoConnectionError)) {
                    boolean z2 = volleyError instanceof TimeoutError;
                }
            }
        }) {


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    if (SelectGenderActivity.this.requestBody == null) {
                        return null;
                    }
                    return SelectGenderActivity.this.requestBody.getBytes("utf-8");
                } catch (Exception unused) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", SelectGenderActivity.this.requestBody, "utf-8");
                    return null;
                }
            }
        };
        r1.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1.0f));
        r1.setTag("Request");
        Volley.newRequestQueue(this).add(r1);
    }

    private void CallService() {
        SocketConfig.getInstance().initSocket();
        SocketConfig.getInstance().registerClient(this);
        SocketConfig.getInstance().SocketConnect();
    }

    @Override
    public void call(Object... objArr) {

        JSONObject jSONObject = (JSONObject) objArr[0];
        try {
            String string = jSONObject.getString("en");
            char c = 65535;
            int hashCode = string.hashCode();
            if (hashCode != -923905396) {
                if (hashCode == 741051755) {
                    if (string.equals("video_call_Mini_App_config")) {
                        c = 1;
                    }
                }
            } else if (string.equals("video_call_Mini_App_register")) {
                c = 0;
            }
            if (c == 0) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("data");
                VariableData.SocketId = jSONObject2.getString("socketid");
                if (AppCheck.getInstance().isVoice == 0) {
                    this.applicationPreference.setNewRegId(jSONObject2.getString("Id"));
                    this.applicationPreference.setChatNickName(jSONObject2.getString("nickname"));
                    this.applicationPreference.setLoginType(jSONObject2.getString("login_type"));
                    this.applicationPreference.setpp(jSONObject2.getString("pp"));
                    v2_Mini_App_config();
                }
            } else if (c == 1) {

                JSONObject jSONObject3 = jSONObject.getJSONObject("data");
                Log.e("mini_app_config", jSONObject3.toString());
                AppCheck.getInstance().isVoice = 0;
                try {
                    JSONObject jSONObject4 = jSONObject3.getJSONObject("config_data");
                    this.applicationPreference.setfource_download(jSONObject4.getString("force_download"));
                    this.applicationPreference.setMainURL(jSONObject4.getString("s3_web_url"));
                    this.applicationPreference.setGoogleRTC_server_url(jSONObject4.getString("GoogleRTC_server_url"));
                    this.applicationPreference.setWebSocket(jSONObject4.getString("web_socket_url"));
                    this.applicationPreference.setCertificateFlagNew(jSONObject4.getString("certificate_download_flag"));
                    this.applicationPreference.setCertificateFile(jSONObject4.getString("certificate_download_path"));
                    ReSchedualCall.runOnUi(new Action1() {


                        @Override // rx.functions.Action1
                        public final void call(Object obj) {
                            SelectGenderActivity.this.lambda$call$0$MainActivity(obj);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void lambda$call$0$MainActivity(Object obj) {
        WebConstantData.data(this);
    }

    public void v2_Mini_App_config() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("package", TempSharedpref.getString(getApplicationContext(), TempSharedpref.PACKAGE));
            jSONObject.put("Id", this.applicationPreference.getNewRegId());
            jSONObject.put("app_name", TempSharedpref.getString(getApplicationContext(), TempSharedpref.APPNAME));
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("en", "video_call_Mini_App_config");
            jSONObject2.put("data", jSONObject);
            SocketConfig.getInstance().emitCall(jSONObject2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void GetChat(Socket socket2) {
        if (socket2.connected()) {
            this.socket = socket2;
            AppCheck.getInstance().isVoice = 0;
            AppCheck.getInstance().RegisterUser();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCheck.getInstance().activity = SelectGenderActivity.this;
        VariableData.Strfakeopen = "faslee";
    }


    public void onBackPressed() {
        startActivity(new Intent(SelectGenderActivity.this, Second_Start_Activity.class));
        finish();
    }

}
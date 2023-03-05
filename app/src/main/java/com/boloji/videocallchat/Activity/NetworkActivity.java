package com.boloji.videocallchat.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.boloji.videocallchat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class NetworkActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS_START_CALL = {"android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.READ_PHONE_STATE"};
    private static final String TAG = "ConnectActivity";
    private static boolean commandLineRun = false;
    String URL1 = "";
    private int countofReq = 0;
    LottieAnimationView gif;
    RequestQueue queue;
    private String roomid;
    private Intent startCallIntent;
    String success;

    private static boolean hasPermissions(Context context, String... strArr) {
        if (context == null || strArr == null) {
            return true;
        }
        for (String str : strArr) {
            if (ActivityCompat.checkSelfPermission(context, str) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_network);

        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.gif_view);
        this.gif = lottieAnimationView;
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);
        this.gif.playAnimation();
        this.queue = Volley.newRequestQueue(this);

        new Handler().postDelayed(new Runnable() {

            public void run() {
                ActivityCompat.requestPermissions(NetworkActivity.this, PERMISSIONS_START_CALL, 102);
            }
        }, 2000);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Log.d("taaaaaaaaaaaa", "onActivityResult: ");
        if (i == 1 && commandLineRun) {
            Log.d(TAG, "Return: " + i2);
            setResult(i2);
            commandLineRun = false;
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 101) {
            if (i == 102) {
                if (hasPermissions(this, PERMISSIONS_START_CALL)) {
                    req();
                } else {
                    Toast.makeText(this, "Required permissions denied.", Toast.LENGTH_LONG).show();
                }
            }
        } else if (hasPermissions(this, PERMISSIONS_START_CALL)) {
            Intent intent = this.startCallIntent;
            if (intent != null) {
                startActivityForResult(intent, 1);
            }
        } else {
            Toast.makeText(this, "Required permissions denied.", Toast.LENGTH_LONG).show();
        }
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.startCallIntent = (Intent) bundle.getParcelable("startCallIntent");
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable("startCallIntent", this.startCallIntent);
    }

    public void getcount() {
        String category = "";

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        Query citiesQuery = databaseRef.child(category);
        citiesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println(":::size " + dataSnapshot.getChildrenCount());
                Intent intent = new Intent(NetworkActivity.this, CallRandomPeopleActivity.class);
                intent.putExtra("total", String.valueOf(dataSnapshot.getChildrenCount()));
                intent.putExtra("category", category);
                startActivity(intent);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NetworkActivity.this, "Failed to connect.", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void req() {
        Log.d("volleyonRequest", "req");
        if (new Random().nextInt(4) == 3) {
            getcount();

            return;
        }
        int i = this.countofReq + 1;
        this.countofReq = i;
        if (i > 2) {
            finish();
        }
        this.queue.add(new StringRequest(0, this.URL1, new Response.Listener<String>() {


            public void onResponse(String str) {
                NetworkActivity.this.countofReq = 0;
                Log.d("volleyonResponse", str.toString());
                try {
                    JSONObject jSONObject = new JSONObject(str);

                    JSONArray jsonArray = jSONObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            roomid = String.valueOf(object.getInt("id"));
                            Log.i("roomid", roomid);
                            success = "true";
                        }
                    }
                    Toast.makeText(NetworkActivity.this, " Connecting ...", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    NetworkActivity.this.req();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkActivity.this.req();
                Log.d("volleyError", volleyError.toString());
            }
        }) {


            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("gender", "female");
                hashMap.put("deviceId", Settings.Secure.getString(NetworkActivity.this.getContentResolver(), "android_id"));
                hashMap.put("emailId", "");
                return hashMap;
            }
        });
    }
}

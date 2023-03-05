package com.boloji.videocallchat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.boloji.videocallchat.NetworkConnection.ApiConstant;
import com.boloji.videocallchat.R;
import com.boloji.videocallchat.TemporaryData.TempSharedpref;

public class VideoCallLoginActivity extends AppCompatActivity {

    EditText name, age;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_call_login);

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        next = findViewById(R.id.next);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        name.setText(TempSharedpref.getString(getApplicationContext(), TempSharedpref.NAME) + "");
        age.setText(TempSharedpref.getString(getApplicationContext(), TempSharedpref.AGE) + "");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String namestring = name.getText().toString();
                String agestring = age.getText().toString();
                if (!namestring.isEmpty() && !agestring.isEmpty()) {
                    try {
                        int number = Integer.parseInt(agestring);
                        if (number >= 18) {
                            int i = 1;
                            ApiConstant.updateNameAndGender(VideoCallLoginActivity.this, i, name.getText().toString().trim());

                            Log.i("UserDataActivity", namestring + agestring);
                            TempSharedpref.setString(getApplicationContext(), namestring, TempSharedpref.NAME);
                            TempSharedpref.setString(getApplicationContext(), agestring, TempSharedpref.AGE);
                            Intent intent = new Intent(VideoCallLoginActivity.this, CallStrangerActivity.class);
                            intent.putExtra("userdeatils", namestring);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(VideoCallLoginActivity.this, "This app only for above age of 17 years", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {

                        TempSharedpref.setString(getApplicationContext(), namestring, TempSharedpref.NAME);
                        TempSharedpref.setString(getApplicationContext(), agestring, TempSharedpref.AGE);
                        Intent intent = new Intent(VideoCallLoginActivity.this, CallStrangerActivity.class);
                        intent.putExtra("userdeatils", namestring);
                        startActivity(intent);

                        finish();
                    }
                } else {
                    Toast.makeText(VideoCallLoginActivity.this, "Please fill details..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(VideoCallLoginActivity.this, SelectGenderActivity.class));
        super.onBackPressed();
    }
}
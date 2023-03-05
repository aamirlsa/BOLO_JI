package com.boloji.videocallchat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.boloji.videocallchat.R;
import com.boloji.videocallchat.AdmobAds.SharedPref;
import com.yesterselga.countrypicker.CountryPicker;
import com.yesterselga.countrypicker.CountryPickerListener;
import com.yesterselga.countrypicker.Theme;
import java.util.HashMap;
import java.util.Map;

public class ChatLoginActivity extends AppCompatActivity {

    EditText name, age;
    TextView country;
    LinearLayout submit;
    FirebaseFirestore mFirestore;
    ImageView male, female, other, back;
    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_login);

        mFirestore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        country = findViewById(R.id.country);
        submit = findViewById(R.id.btnEnterChatRoom);
        male = findViewById(R.id.imgMale);
        female = findViewById(R.id.imgFemale);
        other = findViewById(R.id.imgSomeone);
        CountryPicker picker = CountryPicker.newInstance("Select Country", Theme.LIGHT);  // dialog title and theme
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {

                country.setText(name);

                picker.dismiss();
            }
        });
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });


        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            onBackPressed();
        });

        male.setOnClickListener(v -> {
            gender = "Man";
            male.setImageResource(R.drawable.icon_radio_sel);
            female.setImageResource(R.drawable.icon_radio_unsel);
            other.setImageResource(R.drawable.icon_radio_unsel);
        });

        female.setOnClickListener(v -> {
            gender = "Woman";
            male.setImageResource(R.drawable.icon_radio_unsel);
            female.setImageResource(R.drawable.icon_radio_sel);
            other.setImageResource(R.drawable.icon_radio_unsel);
        });

        other.setOnClickListener(v -> {
            gender = "Someone";
            male.setImageResource(R.drawable.icon_radio_unsel);
            female.setImageResource(R.drawable.icon_radio_unsel);
            other.setImageResource(R.drawable.icon_radio_sel);
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ageString = age.getText().toString();
                String nameString = name.getText().toString();
                String countryStr = country.getText().toString();
                String id;

                if (!nameString.isEmpty()) {
                    if (!ageString.isEmpty()) {
                        if (!gender.isEmpty()) {
                            if (!countryStr.isEmpty()) {


                                id = mFirestore.collection("users").document().getId();


                                DocumentReference restaurants = mFirestore.collection("users")
                                        .document(SharedPref.getString(ChatLoginActivity.this, SharedPref.DEVICE_ID));

                                Map<String, Object> map = new HashMap<>();
                                map.put("name", nameString);
                                map.put("age", ageString);
                                map.put("id", id);
                                map.put("last_video_timestamp", "");
                                map.put("gender", gender);
                                map.put("country", countryStr);
                                map.put("device_id", SharedPref.getString(ChatLoginActivity.this, SharedPref.DEVICE_ID));

                                restaurants.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        SharedPref.setString(ChatLoginActivity.this, SharedPref.NAME, nameString);
                                        SharedPref.setString(ChatLoginActivity.this, SharedPref.AGE, ageString);
                                        SharedPref.setString(ChatLoginActivity.this, SharedPref.ID, id);
                                        SharedPref.setString(ChatLoginActivity.this, SharedPref.GENDER, gender);
                                        SharedPref.setString(ChatLoginActivity.this, SharedPref.COUNTRY, countryStr);

                                        startActivity(new Intent(ChatLoginActivity.this, PeopleListActivity.class));
                                        finish();
                                    }
                                });
                            }

                            else {
                                Toast.makeText(ChatLoginActivity.this, "Enter Country", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ChatLoginActivity.this, "Select Gender", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ChatLoginActivity.this, "Enter age", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChatLoginActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
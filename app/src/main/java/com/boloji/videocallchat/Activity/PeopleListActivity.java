package com.boloji.videocallchat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.boloji.videocallchat.R;
import com.boloji.videocallchat.AdmobAds.SharedPref;
import com.boloji.videocallchat.AdmobAds.helper;

public class PeopleListActivity extends AppCompatActivity {

    int random1, random2, random3, random4;
    TextView randomView1, randomView2, randomView3, randomView4;
    int count = 0;
    ImageView back, img3, img4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chat_list);

        random1 = helper.getRandomChatNumber();
        random2 = helper.getRandomChatNumber();
        random3 = helper.getRandomChatNumber();
        random4 = helper.getRandomChatNumber();

        randomView1 = findViewById(R.id.randomView1);
        randomView2 = findViewById(R.id.randomView2);
        randomView3 = findViewById(R.id.randomView3);
        randomView4 = findViewById(R.id.randomView4);
        randomView1.setText("" + random1);
        randomView2.setText("" + random2);
        randomView3.setText("" + random3);
        randomView4.setText("" + random4);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            onBackPressed();
        });

        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);


        count = SharedPref.getInt(this, SharedPref.COUNT, 0);
    }

    public void chatPre(String grpId) {
        Intent intent = new Intent(PeopleListActivity.this, ChatPeopleActivity.class);
        intent.putExtra("group", grpId);
        startActivity(intent);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.group1:
                chatPre("Hot Room");
                break;
            case R.id.group2:
                chatPre("Romance");
                break;
            case R.id.group3:

                chatPre("Funny");
                break;
            case R.id.group4:
                chatPre("18 + Friends");
                break;

        }
    }

    @Override
    public void onBackPressed() {
            startActivity(new Intent(PeopleListActivity.this, Second_Start_Activity.class));
            finish();
    }

}
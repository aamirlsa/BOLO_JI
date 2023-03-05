package com.boloji.videocallchat.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.boloji.videocallchat.R;
import com.boloji.videocallchat.AdmobAds.DataHelprt;

import org.webrtc.Camera1Enumerator;
import org.webrtc.VideoCapturer;

import java.util.Timer;
import java.util.TimerTask;


public class CallRandomPeopleActivity extends AppCompatActivity implements Player.EventListener {

    private SimpleExoPlayer player;
    PlayerView playerView;
    RelativeLayout endcall, microphoneunmuted, microphonemuted, speackermuted, speackerunmuted;
    int currentvolume;
    String total, id, category;
    KProgressHUD hudsearching, hudconnecting;
    int time;
    Timer T;
    DataHelprt databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call_random_people);

        databaseHelper = new DataHelprt(CallRandomPeopleActivity.this);
        T = new Timer();

        hudsearching = KProgressHUD.create(CallRandomPeopleActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
                .setLabel("Searching...")
                .setDetailsLabel("Please wait")
                .setCancellable(true)
                .setDimAmount(0.5f).show();

        hudconnecting = KProgressHUD.create(CallRandomPeopleActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
                .setLabel("Connecting...")
                .setDetailsLabel("Please wait")
                .setCancellable(true)
                .setDimAmount(0.5f);

        endcall = findViewById(R.id.endcall);
        microphoneunmuted = findViewById(R.id.microphoneunmuted);
        microphonemuted = findViewById(R.id.microphonemuted);
        speackermuted = findViewById(R.id.speackermuted);
        speackerunmuted = findViewById(R.id.speackerunmuted);

        Intent i = getIntent();
        total = i.getStringExtra("total");
        category = i.getStringExtra("category");


        id = String.valueOf(databaseHelper.getCategorycount(category));
        id = String.valueOf(Integer.parseInt(id) + 1);
        if (id.isEmpty()) {
            id = "1";
        }
        if (Integer.parseInt(id) > Integer.parseInt(total)) {
            id = "1";
        }
        System.out.println(":::turn " + id);
        databaseHelper.addCategorycount(category, Integer.parseInt(id));


        playerView = findViewById(R.id.video_view);
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);


        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case ExoPlayer.STATE_BUFFERING:
                        hudconnecting.show();
                        break;
                    case ExoPlayer.STATE_ENDED:
                        T.cancel();
                        finishcall();
                        break;
                    case ExoPlayer.STATE_IDLE:
                        break;
                    case ExoPlayer.STATE_READY:
                        starttimer();
                        hudconnecting.dismiss();
                        hudsearching.dismiss();
                        break;
                    default:
                        break;
                }
            }

        });


        endcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.cancel();
                finishcall();
            }
        });
        microphoneunmuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                microphoneunmuted.setVisibility(View.GONE);
                microphonemuted.setVisibility(View.VISIBLE);
            }
        });

        microphonemuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                microphoneunmuted.setVisibility(View.VISIBLE);
                microphonemuted.setVisibility(View.GONE);
            }
        });

        speackerunmuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speackerunmuted.setVisibility(View.GONE);
                speackermuted.setVisibility(View.VISIBLE);

                currentvolume = (int) player.getVolume();
                player.setVolume(0f);

            }
        });

        speackermuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speackerunmuted.setVisibility(View.VISIBLE);
                speackermuted.setVisibility(View.GONE);

                player.setVolume(currentvolume);

            }
        });

        getvideo();
    }

    private VideoCapturer createCameraCapturer() {
        Camera1Enumerator enumerator = new Camera1Enumerator(false);
        final String[] deviceNames = enumerator.getDeviceNames();

        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    public void starttimer() {
        T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                    }
                });
            }
        }, 1000, 1000);
    }

    public void finishcall() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("time", time + "");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void getvideo() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        Query citiesQuery = databaseRef.child(category).child(id);
        citiesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(CallRandomPeopleActivity.this, "exoplayer-codelab");

                try {
                    MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(dataSnapshot.child("video_url").getValue().toString()));
                    player.setPlayWhenReady(true);
                    player.prepare(mediaSource, false, false);
                } catch (Exception e) {
                    Log.v("exception", e.getMessage());
                    finishcall();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onPause() {
        super.onPause();
        pauseplayer();
    }

    public void pauseplayer() {
        this.player.setPlayWhenReady(false);
        this.player.getPlaybackState();
    }

    public void resumeplayer() {
        this.player.setPlayWhenReady(true);
        this.player.getPlaybackState();
    }

    public void onRestart() {
        super.onRestart();
        resumeplayer();
    }

}
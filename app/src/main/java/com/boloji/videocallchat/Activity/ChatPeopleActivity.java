package com.boloji.videocallchat.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.boloji.videocallchat.HelperClass.CallModel;
import com.boloji.videocallchat.R;
import com.boloji.videocallchat.AdmobAds.SharedPref;
import com.boloji.videocallchat.AdmobAds.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatPeopleActivity extends AppCompatActivity {
    ImageView back;
    EditText message;
    TextView btn_send;
    String group_id, message_id;
    FirebaseFirestore mFirestore;
    RecyclerView recyclerView;
    ArrayList<CallModel> list = new ArrayList<CallModel>();
    LinearLayoutManager manager;
    ChatAdapter chatAdapter;
    TextView heading;
    KProgressHUD hudads;

    InterstitialAd mMobInterstitialAds;
    public void InterstitialLoad() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("1ADAD30F02CD84CDE72190C2ABE5EB5E")).build();
        MobileAds.setRequestConfiguration(configuration);
        InterstitialAd.load(getApplicationContext(), getString(R.string.AdMob_Interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                ChatPeopleActivity.this.mMobInterstitialAds = interstitialAd;
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                            }
                        });
                if (hudads != null) {
                    hudads.dismiss();
                }
                ChatPeopleActivity.this.mMobInterstitialAds.show(ChatPeopleActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                if (hudads != null) {
                    hudads.dismiss();
                }

            }
        });

    }
    RadioGroup radioGroup;
    View sheetViewblkrep;
    BottomSheetDialog mBottomSheetDialogblkrep;
    ImageView imgflag;

    private void showblkrep() {
        this.mBottomSheetDialogblkrep = new BottomSheetDialog(this, R.style.TransparentDialog);
        View inflate = getLayoutInflater().inflate(R.layout.bottom_vidclflgin, (ViewGroup) null);
        this.sheetViewblkrep = inflate;
        this.mBottomSheetDialogblkrep.setContentView(inflate);
        RadioGroup radioGroup2 =  this.sheetViewblkrep.findViewById(R.id.radioGroup);
        this.radioGroup = radioGroup2;
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton =  sheetViewblkrep.findViewById(i);
            }
        });
        EditText editText = this.sheetViewblkrep.findViewById(R.id.editText);
        (this.sheetViewblkrep.findViewById(R.id.txtcn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialogblkrep.dismiss();
            }
        });
        ((TextView) this.sheetViewblkrep.findViewById(R.id.txtok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Toast.makeText(ChatPeopleActivity.this, "Please Select Report", Toast.LENGTH_SHORT).show();
                    return;
                }
                radioGroup.clearCheck();
                RadioButton radioButton = radioGroup.findViewById(checkedRadioButtonId);
                mBottomSheetDialogblkrep.dismiss();
                Toast.makeText(ChatPeopleActivity.this, "User is Flagged! Our Team will Take Appropriate Action!", Toast.LENGTH_SHORT).show();
                mBottomSheetDialogblkrep.dismiss();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_people);


        showblkrep();
        imgflag = findViewById(R.id.imgflag);
        imgflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialogblkrep.show();
            }
        });

        hudads = KProgressHUD.create(ChatPeopleActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
                .setLabel("Ads Loading...")
                .setDetailsLabel("Please wait")
                .setCancellable(true)
                .setDimAmount(0.5f)
                .show();
        InterstitialLoad();

        Intent i = getIntent();
        group_id = i.getStringExtra("group");

        mFirestore = FirebaseFirestore.getInstance();

        back = findViewById(R.id.back);
        message = findViewById(R.id.message);
        btn_send = findViewById(R.id.btn_send);
        recyclerView = findViewById(R.id.recyclerView);
        heading = findViewById(R.id.heading);

        heading.setText(group_id);

        manager = new LinearLayoutManager(ChatPeopleActivity.this);
        manager.setStackFromEnd(true);
        recyclerView.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] msg = {message.getText().toString()};
                if (!msg[0].isEmpty()) {

                    message_id = mFirestore.collection("chat").document("group_" + group_id)
                            .collection("message").document().getId();

                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();

                    Map<String, Object> map = new HashMap<>();
                    map.put("message_id", message_id);
                    map.put("message", msg[0]);
                    map.put("sender_id", SharedPref.getString(ChatPeopleActivity.this, SharedPref.DEVICE_ID));
                    map.put("sender_name", SharedPref.getString(ChatPeopleActivity.this, SharedPref.NAME));
                    map.put("timestamp", ts);
                    map.put("group_id", group_id);

                    DocumentReference restaurants = mFirestore.collection("chat").document("group_" + group_id)
                            .collection("message").document(message_id);

                    restaurants.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            msg[0] = "";
                            message.setText(msg[0]);
                        }
                    });

                } else {
                    Toast.makeText(ChatPeopleActivity.this, "Type Something..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getChats();
    }

    public void getChats() {
        Query collectionReference = mFirestore.collection("chat")
                .document("group_" + group_id)
                .collection("message")
                .orderBy("timestamp", Query.Direction.ASCENDING);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.v("::", "onSuccess: LIST EMPTY");
                    return;
                } else {
                    list.clear();
                    List<CallModel> types = queryDocumentSnapshots.toObjects(CallModel.class);
                    list.addAll(types);

                    chatAdapter = new ChatAdapter(ChatPeopleActivity.this, list);
                    recyclerView.setAdapter(chatAdapter);

                }
            }
        });
    }


    public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

        Context context;
        ArrayList<CallModel> list;

        public ChatAdapter(Context context, ArrayList<CallModel> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            CallModel messageModel = list.get(position);

            if (position == 0) {
                holder.date.setVisibility(View.VISIBLE);
                holder.date.setText(helper.getDate(messageModel.getTimestamp()));
            } else {
                if (!helper.getDate(list.get(position - 1).getTimestamp()).equals(helper.getDate(messageModel.getTimestamp()))) {
                    holder.date.setVisibility(View.VISIBLE);
                    holder.date.setText(helper.getDate(messageModel.getTimestamp()));
                } else {
                    holder.date.setVisibility(View.GONE);
                }
            }

            if (messageModel.getSender_id().equals(SharedPref.getString(context, SharedPref.DEVICE_ID))) {
                holder.receivedLayout.setVisibility(View.GONE);
                holder.sentLayout.setVisibility(View.VISIBLE);

                holder.sent.setText(messageModel.getMessage());
                holder.sentDate.setText(messageModel.getSender_name() + " (" + helper.getTimeforChat(messageModel.getTimestamp()) + ")");
            } else {
                holder.receivedLayout.setVisibility(View.VISIBLE);
                holder.sentLayout.setVisibility(View.GONE);

                holder.received.setText(messageModel.getMessage());
                holder.receivedDate.setText(messageModel.getSender_name() + " (" + helper.getTimeforChat(messageModel.getTimestamp()) + ")");
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView sent, received, sentDate, receivedDate, date;
            LinearLayout sentLayout, receivedLayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                sent = itemView.findViewById(R.id.sent);
                sentDate = itemView.findViewById(R.id.sentDate);
                received = itemView.findViewById(R.id.received);
                sentLayout = itemView.findViewById(R.id.sentLayout);
                receivedLayout = itemView.findViewById(R.id.receivedLayout);
                receivedDate = itemView.findViewById(R.id.receivedDate);
                date = itemView.findViewById(R.id.date);
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChatPeopleActivity.this, PeopleListActivity.class));

        finish();
    }
}
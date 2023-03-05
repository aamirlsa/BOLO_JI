package com.boloji.videocallchat.OtherData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.boloji.videocallchat.Activity.RandomVideoCallActivity;
import com.boloji.videocallchat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;
    AppCompatActivity videoCallActivity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgEmoji;

        public MyViewHolder(View view) {
            super(view);
            this.imgEmoji = (ImageView) view.findViewById(R.id.imgEmoji);
        }
    }

    public EmojiAdapter(AppCompatActivity appCompatActivity, ArrayList<String> arrayList) {
        this.mDataset = arrayList;
        this.videoCallActivity = appCompatActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emoji_list_item, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        Picasso.get().load("file:///android_asset/emojis/" + this.mDataset.get(i)).into(myViewHolder.imgEmoji);
        myViewHolder.imgEmoji.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                if (EmojiAdapter.this.videoCallActivity instanceof RandomVideoCallActivity) {
                    ((RandomVideoCallActivity) EmojiAdapter.this.videoCallActivity).emojiSelect(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mDataset.size();
    }
}

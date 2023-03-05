package com.boloji.videocallchat.HelperClass;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveState {
    Context context;
    String saveName;
    SharedPreferences sp;

    public SaveState(Context context, String saveName) {
        this.context = context;
        this.saveName = saveName;
        sp=context.getSharedPreferences(saveName,context.MODE_PRIVATE);
    }


    public  void  setState(int key){
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("Key",key);
        editor.commit();

    }

    public int getState(){
        return sp.getInt("Key",0);

    }
}

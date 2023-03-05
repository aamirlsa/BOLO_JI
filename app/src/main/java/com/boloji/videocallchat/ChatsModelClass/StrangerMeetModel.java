package com.boloji.videocallchat.ChatsModelClass;

import java.util.ArrayList;

public class StrangerMeetModel {
    private String room_id;
    private ArrayList<ServerModel> stun_server;
    private String user_id;
    private int video_count;
    private String video_link;

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String str) {
        this.user_id = str;
    }

    public String getRoom_id() {
        return this.room_id;
    }

    public String getVideo_link() {
        return this.video_link;
    }

    public void setVideo_link(String str) {
        this.video_link = str;
    }

    public int getVideo_count() {
        return this.video_count;
    }

    public void setVideo_count(int i) {
        this.video_count = i;
    }

    public void setRoom_id(String str) {
        this.room_id = str;
    }

    public ArrayList<ServerModel> getStun_server() {
        return this.stun_server;
    }

    public void setStun_server(ArrayList<ServerModel> arrayList) {
        this.stun_server = arrayList;
    }
}

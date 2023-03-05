package com.boloji.videocallchat.HelperClass;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CandidateModel implements Serializable {
    @SerializedName("candidate")
    private String sdp;
    @SerializedName("sdpMLineIndex")
    private int sdpMLineIndex;
    @SerializedName("sdpMid")
    private String sdpMid;

    public CandidateModel(String str, int i, String str2) {
        this.sdpMid = str;
        this.sdpMLineIndex = i;
        this.sdp = str2;
    }

    public String getSdpMid() {
        return this.sdpMid;
    }

    public int getSdpMLineIndex() {
        return this.sdpMLineIndex;
    }

    public String getSdp() {
        return this.sdp;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}

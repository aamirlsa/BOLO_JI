package com.boloji.videocallchat.HelperClass.Connection;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.boloji.videocallchat.HelperClass.CandidateModel;
import com.boloji.videocallchat.HelperClass.CandidateID;

import java.io.Serializable;

import io.socket.engineio.client.transports.PollingXHR;

public class NetworkServer extends CandidateID implements Serializable {
    @SerializedName("candidate")
    private CandidateModel candidate;
    @SerializedName("from")
    private String from;
    @SerializedName("message")
    private String message;
    @SerializedName("response")
    private String response;
    @SerializedName("sdpAnswer")
    private String sdpAnswer;
    @SerializedName(PollingXHR.Request.EVENT_SUCCESS)
    private boolean success;

    public NetworkID getIdRes() {
        return NetworkID.getIdRes(getId());
    }

    public NetworkType getTypeRes() {
        return NetworkType.getType(getResponse());
    }

    public String getResponse() {
        return this.response;
    }

    public String getSdpAnswer() {
        return this.sdpAnswer;
    }

    public CandidateModel getCandidate() {
        return this.candidate;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getFrom() {
        return this.from;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

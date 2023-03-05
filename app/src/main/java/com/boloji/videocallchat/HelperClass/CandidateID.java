package com.boloji.videocallchat.HelperClass;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CandidateID implements Serializable {
    @SerializedName("id")
    protected String id;

    public static CandidateID create(String str) {
        CandidateID idModel = new CandidateID();
        idModel.setId(str);
        return idModel;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}

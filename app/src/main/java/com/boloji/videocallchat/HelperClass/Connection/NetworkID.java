package com.boloji.videocallchat.HelperClass.Connection;

import androidx.core.os.EnvironmentCompat;

public enum NetworkID {
    REGISTER_RESPONSE("registerResponse"),
    PRESENTER_RESPONSE("presenterResponse"),
    ICE_CANDIDATE("iceCandidate"),
    VIEWER_RESPONSE("viewerResponse"),
    STOP_COMMUNICATION("stopCommunication"),
    CLOSE_ROOM_RESPONSE("closeRoomResponse"),
    INCOMING_CALL("incomingCall"),
    START_COMMUNICATION("startCommunication"),
    CALL_RESPONSE("callResponse"),
    UN_KNOWN(EnvironmentCompat.MEDIA_UNKNOWN);
    
    private String id;

    private NetworkID(String str) {
        this.id = str;
    }

    public static NetworkID getIdRes(String str) {
        NetworkID[] values = values();
        for (NetworkID idResponse : values) {
            if (str.equals(idResponse.getId())) {
                return idResponse;
            }
        }
        return UN_KNOWN;
    }

    public String getId() {
        return this.id;
    }
}

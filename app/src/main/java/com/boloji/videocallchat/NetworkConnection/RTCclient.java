package com.boloji.videocallchat.NetworkConnection;

import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.nhancv.webrtcpeer.rtc_comm.ws.BaseSocketCallback;
import com.nhancv.webrtcpeer.rtc_comm.ws.SocketService;
import com.nhancv.webrtcpeer.rtc_peer.RTCClient;

import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

public class RTCclient implements RTCClient {
    private static final String TAG = "KurentoOne2OneRTCClient";
    private String fromPeer;
    private boolean isHost;
    private SocketService socketService;
    private String toPeer;

    @Override
    public void sendAnswerSdp(SessionDescription sessionDescription) {
    }

    public RTCclient(SocketService socketService2, String str, String str2, boolean z) {
        this.socketService = socketService2;
        this.fromPeer = str;
        this.toPeer = str2;
        this.isHost = z;
    }

    public void connectToRoom(String str, BaseSocketCallback baseSocketCallback) {
        this.socketService.connect(str, baseSocketCallback);
    }

    @Override
    public void sendOfferSdp(SessionDescription sessionDescription) {
        try {
            if (this.isHost) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("id", NotificationCompat.CATEGORY_CALL);
                jSONObject.put("from", this.fromPeer);
                jSONObject.put("to", this.toPeer);
                jSONObject.put("sdpOffer", sessionDescription.description);
                Log.e(TAG, NotificationCompat.CATEGORY_CALL);
                this.socketService.sendMessage(jSONObject.toString());
                return;
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("id", "incomingCallResponse");
            jSONObject2.put("from", this.fromPeer);
            jSONObject2.put("callResponse", "accept");
            jSONObject2.put("sdpOffer", sessionDescription.description);
            Log.e(TAG, "incomingCallResponse");
            this.socketService.sendMessage(jSONObject2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendLocalIceCandidate(IceCandidate iceCandidate) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", "onIceCandidate");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("candidate", iceCandidate.sdp);
            jSONObject2.put("sdpMid", iceCandidate.sdpMid);
            jSONObject2.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
            jSONObject.put("candidate", jSONObject2);
            Log.e(TAG, "onIceCandidate");
            this.socketService.sendMessage(jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendLocalIceCandidateRemovals(IceCandidate[] iceCandidateArr) {
        Log.e(TAG, "sendLocalIceCandidateRemovals: ");
    }
}

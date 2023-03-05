package com.boloji.videocallchat.NetworkConnection;

import android.util.Log;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;import org.webrtc.RtpReceiver;

public class PeersConnection implements org.webrtc.PeerConnection.Observer {
    private String logTag = getClass().getCanonicalName();


    public PeersConnection(String str) {
        this.logTag += " " + str;
    }

    @Override
    public void onSignalingChange(org.webrtc.PeerConnection.SignalingState signalingState) {
        String str = this.logTag;
        Log.d(str, "onSignalingChange() called with: signalingState = [" + signalingState + "]");
    }

    @Override
    public void onIceConnectionChange(org.webrtc.PeerConnection.IceConnectionState iceConnectionState) {
        String str = this.logTag;
        Log.d(str, "onIceConnectionChange() called with: iceConnectionState = [" + iceConnectionState + "]");
    }

    @Override
    public void onIceConnectionReceivingChange(boolean z) {
        String str = this.logTag;
        Log.d(str, "onIceConnectionReceivingChange() called with: b = [" + z + "]");
    }

    @Override
    public void onIceGatheringChange(org.webrtc.PeerConnection.IceGatheringState iceGatheringState) {
        String str = this.logTag;
        Log.d(str, "onIceGatheringChange() called with: iceGatheringState = [" + iceGatheringState + "]");
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        String str = this.logTag;
        Log.d(str, "onIceCandidate() called with: iceCandidate = [" + iceCandidate + "]");
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidateArr) {
        String str = this.logTag;
        Log.d(str, "onIceCandidatesRemoved() called with: iceCandidates = [" + iceCandidateArr + "]");
    }

    @Override
    public void onAddStream(MediaStream mediaStream) {
        String str = this.logTag;
        Log.d(str, "onAddStream() called with: mediaStream = [" + mediaStream + "]");
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        String str = this.logTag;
        Log.d(str, "onRemoveStream() called with: mediaStream = [" + mediaStream + "]");
    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        String str = this.logTag;
        Log.d(str, "onDataChannel() called with: dataChannel = [" + dataChannel + "]");
    }

    @Override
    public void onRenegotiationNeeded() {
        Log.d(this.logTag, "onRenegotiationNeeded() called");
    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreamArr) {
        String str = this.logTag;
        Log.d(str, "onAddTrack() called with: rtpReceiver = [" + rtpReceiver + "], mediaStreams = [" + mediaStreamArr + "]");
    }
}

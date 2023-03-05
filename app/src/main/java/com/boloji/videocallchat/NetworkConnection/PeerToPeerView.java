package com.boloji.videocallchat.NetworkConnection;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.webrtc.EglBase;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;

public interface PeerToPeerView extends MvpView {
    VideoCapturer createVideoCapturer();

    void disconnect();

    EglBase.Context getEglBaseContext();

    VideoRenderer.Callbacks getLocalProxyRenderer();

    VideoRenderer.Callbacks getRemoteProxyRenderer();

    void incomingCalling(String str);

    void logAndToast(String str);

    void registerStatus(boolean z);

    void setSwappedFeeds(boolean z);

    void socketConnect(boolean z);

    void startCallIng();

    void stopCalling();

    void transactionToCalling(String str, String str2, boolean z);
}

package com.boloji.videocallchat.NetworkConnection;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.nhancv.webrtcpeer.rtc_comm.ws.SocketCallBack;
import com.nhancv.webrtcpeer.rtc_comm.ws.SocketService;
import com.nhancv.webrtcpeer.rtc_peer.PeerConnectionClient;
import com.nhancv.webrtcpeer.rtc_peer.PeerConnectionParameters;
import com.nhancv.webrtcpeer.rtc_peer.SignalingEvents;
import com.nhancv.webrtcpeer.rtc_peer.SignalingParameters;
import com.nhancv.webrtcpeer.rtc_peer.config.DefaultConfig;
import com.nhancv.webrtcpeer.rtc_plugins.RTCAudioManager;
import com.boloji.videocallchat.OtherActivities.AppCheck;
import com.boloji.videocallchat.OtherData.ReSchedualCall;
import com.boloji.videocallchat.HelperClass.Connection.NetworkServer;
import com.boloji.videocallchat.HelperClass.Connection.NetworkID;
import com.boloji.videocallchat.HelperClass.Connection.NetworkType;
import com.boloji.videocallchat.HelperClass.CandidateModel;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;
import org.webrtc.VideoCapturer;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

import rx.functions.Action1;

public class PeerToPeerConnection extends MvpBasePresenter<PeerToPeerView> implements SignalingEvents, PeerConnectionClient.PeerConnectionEvents {
    private static final String TAG = "FaceToFacePresenter";
    private AppCheck application;
    private RTCAudioManager audioManager;
    private DefaultConfig defaultConfig;
    private Gson gson = new Gson();
    private boolean iceConnected;
    public PeerConnectionClient peerConnectionClient;
    private PeerConnectionParameters peerConnectionParameters;
    private RTCclient rtcClient;
    private SignalingParameters signalingParameters;
    private SocketService socketService = AppCheck.getInstance().socket;
    private int[] idresponse;
    @Override
    public void onPeerConnectionClosed() {
    }

    public PeerToPeerConnection(Application application2) {
        this.application = (AppCheck) application2;
    }

    public void initPeerConfig(String str, String str2, boolean z) {
        this.rtcClient = new RTCclient(this.socketService, str, str2, z);
        DefaultConfig defaultConfig2 = new DefaultConfig();
        this.defaultConfig = defaultConfig2;
        this.peerConnectionParameters = defaultConfig2.createPeerConnectionParams();
        PeerConnectionClient instance = PeerConnectionClient.getInstance();
        this.peerConnectionClient = instance;
        instance.createPeerConnectionFactory(this.application.getApplicationContext(), this.peerConnectionParameters, this);
    }

    public void disconnect() {
        if (this.rtcClient != null) {
            this.rtcClient = null;
        }
        PeerConnectionClient peerConnectionClient2 = this.peerConnectionClient;
        if (peerConnectionClient2 != null) {
            peerConnectionClient2.close();
            this.peerConnectionClient = null;
        }
        RTCAudioManager rTCAudioManager = this.audioManager;
        if (rTCAudioManager != null) {
            rTCAudioManager.stop();
            this.audioManager = null;
        }
        SocketService socketService2 = this.socketService;
        if (socketService2 != null) {
            socketService2.close();
        }
        if (isViewAttached()) {
            ((PeerToPeerView) getView()).disconnect();
        }
    }

    public void connectServer() {
        if (this.socketService.isConnected()) {
            ReSchedualCall.runOnUi(new Action1() {


                @Override
                public final void call(Object obj) {
                    PeerToPeerConnection.this.lambda$connectServer$0$FaceToFacePresenter(obj);
                }
            });
        }
        this.socketService.setCallBack(new SocketCallBack() {


            @Override
            public void onOpen(ServerHandshake serverHandshake) {
            }

            @Override
            public void onMessage(String str) {
                try {
                    NetworkServer serverResponse = (NetworkServer) PeerToPeerConnection.this.gson.fromJson(str, NetworkServer.class);
                    ((PeerToPeerView) PeerToPeerConnection.this.getView()).logAndToast(serverResponse.getMessage());
                    switch (Class3.$SwitchMap$com$freetalk$freetalk$Model$response$IdResponse[serverResponse.getIdRes().ordinal()]) {
                        case 1:
                            if (serverResponse.getTypeRes() == NetworkType.REJECTED) {
                                ReSchedualCall.runOnUi(new Action1() {


                                    @Override
                                    public final void call(Object obj) {
                                        if (PeerToPeerConnection.this.isViewAttached()) {
                                            ((PeerToPeerView) PeerToPeerConnection.this.getView()).registerStatus(false);
                                        }
                                    }
                                });
                                return;
                            } else {
                                ReSchedualCall.runOnUi(new Action1() {


                                    @Override // rx.functions.Action1
                                    public final void call(Object obj) {
                                        if (PeerToPeerConnection.this.isViewAttached()) {
                                            ((PeerToPeerView) PeerToPeerConnection.this.getView()).registerStatus(true);
                                        }
                                    }
                                });
                                return;
                            }
                        case 2:
                            ReSchedualCall.runOnUi(new Action1() {

                                @Override
                                public final void call(Object obj) {

                                    if (PeerToPeerConnection.this.isViewAttached()) {
                                        ((PeerToPeerView) PeerToPeerConnection.this.getView()).incomingCalling(serverResponse.getFrom());
                                    }
                                }
                            });
                            return;
                        case 3:
                            if (serverResponse.getTypeRes() == NetworkType.REJECTED) {
                                ReSchedualCall.runOnUi(new Action1() {

                                    @Override
                                    public final void call(Object obj) {
                                        if (PeerToPeerConnection.this.isViewAttached()) {
                                            ((PeerToPeerView) PeerToPeerConnection.this.getView()).logAndToast(serverResponse.getMessage());
                                        }
                                    }
                                });
                                return;
                            }
                            PeerToPeerConnection.this.onRemoteDescription(new SessionDescription(SessionDescription.Type.ANSWER, serverResponse.getSdpAnswer()));
                            ReSchedualCall.runOnUi(new Action1() {


                                @Override
                                public final void call(Object obj) {
                                    if (PeerToPeerConnection.this.isViewAttached()) {
                                        ((PeerToPeerView) PeerToPeerConnection.this.getView()).startCallIng();
                                    }
                                }
                            });
                            return;
                        case 4:
                            CandidateModel candidate = serverResponse.getCandidate();
                            PeerToPeerConnection.this.onRemoteIceCandidate(new IceCandidate(candidate.getSdpMid(), candidate.getSdpMLineIndex(), candidate.getSdp()));
                            return;
                        case 5:
                           PeerToPeerConnection.this.onRemoteDescription(new SessionDescription(SessionDescription.Type.ANSWER, serverResponse.getSdpAnswer()));
                            ReSchedualCall.runOnUi(new Action1() {


                                @Override
                                public final void call(Object obj) {
                                    if (PeerToPeerConnection.this.isViewAttached()) {
                                        ((PeerToPeerView) PeerToPeerConnection.this.getView()).startCallIng();
                                    }
                                }
                            });
                            return;
                        case 6:
                            ReSchedualCall.runOnUi(new Action1() {


                                @Override
                                public final void call(Object obj) {
                                    if (PeerToPeerConnection.this.isViewAttached()) {
                                        ((PeerToPeerView) PeerToPeerConnection.this.getView()).stopCalling();
                                    }
                                }
                            });
                            return;
                        default:
                            return;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onClose(int i, String str, boolean z) {
                ReSchedualCall.runOnUi(new Action1() {


                    @Override
                    public final void call(Object obj) {
                        ((PeerToPeerView) PeerToPeerConnection.this.getView()).logAndToast("Closed");
                        ((PeerToPeerView) PeerToPeerConnection.this.getView()).socketConnect(false);
                        PeerToPeerConnection.this.disconnect();
                    }
                });
            }


            @Override
            public void onError(Exception exc) {
                ReSchedualCall.runOnUi(new Action1() {


                    @Override
                    public final void call(Object obj) {
                        ((PeerToPeerView) PeerToPeerConnection.this.getView()).socketConnect(false);
                        PeerToPeerConnection.this.disconnect();
                    }
                });
            }


        });
    }

    public void lambda$connectServer$0$FaceToFacePresenter(Object obj) {
        PeerToPeerView faceToFaceView = (PeerToPeerView) getView();
        Objects.requireNonNull(faceToFaceView);
        faceToFaceView.socketConnect(true);
    }


    static class Class3 {
        static final int[] $SwitchMap$com$freetalk$freetalk$Model$response$IdResponse;

        static {
            int[] iArr = new int[NetworkID.values().length];
            $SwitchMap$com$freetalk$freetalk$Model$response$IdResponse = iArr;
            iArr[NetworkID.REGISTER_RESPONSE.ordinal()] = 1;
            $SwitchMap$com$freetalk$freetalk$Model$response$IdResponse[NetworkID.INCOMING_CALL.ordinal()] = 2;
            $SwitchMap$com$freetalk$freetalk$Model$response$IdResponse[NetworkID.CALL_RESPONSE.ordinal()] = 3;
            $SwitchMap$com$freetalk$freetalk$Model$response$IdResponse[NetworkID.ICE_CANDIDATE.ordinal()] = 4;
            $SwitchMap$com$freetalk$freetalk$Model$response$IdResponse[NetworkID.START_COMMUNICATION.ordinal()] = 5;
            try {
                $SwitchMap$com$freetalk$freetalk$Model$response$IdResponse[NetworkID.STOP_COMMUNICATION.ordinal()] = 6;
            } catch (NoSuchFieldError unused) {
            }
        }
    }


    public void register(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", "register");
            jSONObject.put("name", str);
            this.socketService.sendMessage(jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startCall() {
        if (this.rtcClient == null) {
            Log.e(TAG, "AppRTC client is not allocated for a call.");
            return;
        }
        onSignalConnected(new SignalingParameters(new LinkedList<PeerConnection.IceServer>() {


            {
                add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
            }
        }, true, null, null, null, null, null));
        this.audioManager = RTCAudioManager.create(this.application.getApplicationContext());
        Log.d(TAG, "Starting the audio manager...");
        this.audioManager.start(new RTCAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(RTCAudioManager.AudioDevice audioDevice, Set<RTCAudioManager.AudioDevice> set) {
                PeerToPeerConnection.lambda$startCall$1(audioDevice, set);
            }
        });
    }

    static  void lambda$startCall$1(RTCAudioManager.AudioDevice audioDevice, Set set) {
        String str = TAG;
        Log.d(str, "onAudioManagerDevicesChanged: " + set + ", selected: " + audioDevice);
    }

    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    private void callConnected() {
        PeerConnectionClient peerConnectionClient2 = this.peerConnectionClient;
        if (peerConnectionClient2 == null) {
            Log.w(TAG, "Call is connected in closed or error state");
            return;
        }
        peerConnectionClient2.enableStatsEvents(true, 1000);
        ((PeerToPeerView) getView()).setSwappedFeeds(false);
    }

    @Override
    public void onSignalConnected(SignalingParameters signalingParameters2) {
        ReSchedualCall.runOnUi(new Action1() {


            @Override
            public final void call(Object obj) {
                if (isViewAttached()) {
                    signalingParameters = signalingParameters2;
                    VideoCapturer videoCapturer = null;
                    if (peerConnectionParameters.videoCallEnabled) {
                        videoCapturer = ((PeerToPeerView) getView()).createVideoCapturer();
                    }
                    peerConnectionClient.createPeerConnection(((PeerToPeerView) getView()).getEglBaseContext(), ((PeerToPeerView) getView()).getLocalProxyRenderer(), ((PeerToPeerView) getView()).getRemoteProxyRenderer(), videoCapturer, signalingParameters);
                    if (isViewAttached()) {
                        ((PeerToPeerView) getView()).logAndToast("Creating OFFER...");
                    }
                    peerConnectionClient.createOffer();
                }
            }
        });
    }



    @Override
    public void onRemoteDescription(SessionDescription sessionDescription) {
        ReSchedualCall.runOnUi(new Action1() {

            @Override
            public final void call(Object obj) {
                PeerToPeerConnection.this.lambda$onRemoteDescription$3$FaceToFacePresenter(sessionDescription, obj);
            }
        });
    }

    public void lambda$onRemoteDescription$3$FaceToFacePresenter(SessionDescription sessionDescription, Object obj) {
        PeerConnectionClient peerConnectionClient2 = this.peerConnectionClient;
        if (peerConnectionClient2 == null) {
            Log.e(TAG, "Received remote SDP for non-initilized peer connection.");
            return;
        }
        peerConnectionClient2.setRemoteDescription(sessionDescription);
        if (!this.signalingParameters.initiator) {
            if (isViewAttached()) {
                ((PeerToPeerView) getView()).logAndToast("Creating ANSWER...");
            }
            this.peerConnectionClient.createAnswer();
        }
    }

    @Override
    public void onRemoteIceCandidate(IceCandidate iceCandidate) {
        ReSchedualCall.runOnUi(new Action1() {

            @Override
            public final void call(Object obj) {
                PeerToPeerConnection.this.lambda$onRemoteIceCandidate$4$FaceToFacePresenter(iceCandidate, obj);
            }
        });
    }

    public void lambda$onRemoteIceCandidate$4$FaceToFacePresenter(IceCandidate iceCandidate, Object obj) {
        PeerConnectionClient peerConnectionClient2 = this.peerConnectionClient;
        if (peerConnectionClient2 == null) {
            Log.e(TAG, "Received ICE candidate for a non-initialized peer connection.");
        } else {
            peerConnectionClient2.addRemoteIceCandidate(iceCandidate);
        }
    }

    @Override
    public void onRemoteIceCandidatesRemoved(IceCandidate[] iceCandidateArr) {
        ReSchedualCall.runOnUi(new Action1() {


            @Override
            public final void call(Object obj) {
                PeerConnectionClient peerConnectionClient2 =peerConnectionClient;
                if (peerConnectionClient2 == null) {
                    Log.e(TAG, "Received ICE candidate removals for a non-initialized peer connection.");
                } else {
                    peerConnectionClient2.removeRemoteIceCandidates(iceCandidateArr);
                }
            }
        });
    }



    @Override
    public void onChannelClose() {
        ReSchedualCall.runOnUi(new Action1() {

            public final void call(Object obj) {
                if (isViewAttached()) {
                    ((PeerToPeerView) getView()).logAndToast("Remote end hung up; dropping PeerConnection");
                }
                disconnect();
            }
        });
    }

    @Override
    public void onChannelError(String str) {
        String str2 = TAG;
        Log.e(str2, "onChannelError: " + str);
    }

    @Override
    public void onLocalDescription(SessionDescription sessionDescription) {
        ReSchedualCall.runOnUi(new Action1() {

            @Override
            public final void call(Object obj) {
                PeerToPeerConnection.this.lambda$onLocalDescription$7$FaceToFacePresenter(sessionDescription, obj);
            }
        });
    }

    public void lambda$onLocalDescription$7$FaceToFacePresenter(SessionDescription sessionDescription, Object obj) {
        if (this.rtcClient != null) {
            if (this.signalingParameters.initiator) {
                this.rtcClient.sendOfferSdp(sessionDescription);
            } else {
                this.rtcClient.sendAnswerSdp(sessionDescription);
            }
        }
        if (this.peerConnectionParameters.videoMaxBitrate > 0) {
            String str = TAG;
            Log.d(str, "Set video maximum bitrate: " + this.peerConnectionParameters.videoMaxBitrate);
            this.peerConnectionClient.setVideoMaxBitrate(Integer.valueOf(this.peerConnectionParameters.videoMaxBitrate));
        }
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        ReSchedualCall.runOnUi(new Action1() {

            @Override
            public final void call(Object obj) {
                PeerToPeerConnection.this.lambda$onIceCandidate$8$FaceToFacePresenter(iceCandidate, obj);
            }
        });
    }

    public  void lambda$onIceCandidate$8$FaceToFacePresenter(IceCandidate iceCandidate, Object obj) {
        RTCclient kurentoOne2OneRTCClient = this.rtcClient;
        if (kurentoOne2OneRTCClient != null) {
            kurentoOne2OneRTCClient.sendLocalIceCandidate(iceCandidate);
        }
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidateArr) {
        ReSchedualCall.runOnUi(new Action1() {


            @Override
            public final void call(Object obj) {
                RTCclient kurentoOne2OneRTCClient = rtcClient;
                if (kurentoOne2OneRTCClient != null) {
                    kurentoOne2OneRTCClient.sendLocalIceCandidateRemovals(iceCandidateArr);
                }
            }
        });
    }



    @Override
    public void onIceConnected() {
        ReSchedualCall.runOnUi(new Action1() {


            @Override
            public final void call(Object obj) {
              iceConnected = true;
                callConnected();
            }
        });
    }



    @Override
    public void onIceDisconnected() {
        ReSchedualCall.runOnUi(new Action1() {


            @Override
            public final void call(Object obj) {
                if (isViewAttached()) {
                    ((PeerToPeerView) getView()).logAndToast("ICE disconnected");
                }
          iceConnected = false;
                disconnect();
            }
        });
    }



    @Override
    public void onPeerConnectionStatsReady(StatsReport[] statsReportArr) {
        ReSchedualCall.runOnUi(new Action1() {


            @Override
            public final void call(Object obj) {
                if (iceConnected) {
                    String str = TAG;
                    Log.e(str, "run: " + statsReportArr);
                }
            }
        });
    }


    @Override
    public void onPeerConnectionError(String str) {
        String str2 = TAG;
        Log.e(str2, "onPeerConnectionError: " + str);
    }
}

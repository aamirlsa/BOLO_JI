package com.boloji.videocallchat.NetworkConnection;

import android.content.Context;
import android.util.Log;

import com.boloji.videocallchat.TemporaryData.TempSharedpref;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class SignallingClient {
    private SignalingInterface callback;
    public boolean isChannelReady = false;
    public boolean isInitiator = false;
    public boolean isStarted = false;
    public String roomName = null;
    private Socket socket;
    private final TrustManager[] trustAllCerts = {new X509TrustManager() {


        @Override
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }};

    public interface SignalingInterface {
        void onAnswerReceived(JSONObject jSONObject);

        void onCameraSwitchNode(JSONObject jSONObject);

        void onCreatedRoom();

        void onIceCandidateReceived(JSONObject jSONObject);

        void onJoinedRoom();

        void onNewEmoji(JSONObject jSONObject);

        void onNewMessage(JSONObject jSONObject);

        void onNewPeerJoined();

        void onOfferReceived(JSONObject jSONObject);

        void onRemoteHangUp(String str);

        void onTryToStart();
    }

    public void init(SignalingInterface signalingInterface,Context context) {
        this.callback = signalingInterface;
        try {
            SSLContext.getInstance("TLS").init(null, this.trustAllCerts, null);
            Socket socket2 = IO.socket(TempSharedpref.getString(context,TempSharedpref.Server2SocketURL));
            this.socket = socket2;
            socket2.connect();
            Log.d("SignallingClient", "init() called");
            if (!this.roomName.isEmpty()) {
                emitInitStatement(this.roomName);
            }
            this.socket.on("created", new Emitter.Listener() {

                @Override
                public final void call(Object[] objArr) {
                    lambda$init$0$SignallingClient(objArr);
                }
            });
            this.socket.on("full", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                }
            });
            this.socket.on("join", new Emitter.Listener() {

                @Override
                public final void call(Object[] objArr) {
                   lambda$init$2$SignallingClient(objArr);
                }
            });
            this.socket.on(this.roomName, new Emitter.Listener() {

                @Override
                public final void call(Object[] objArr) {
                    lambda$init$3$SignallingClient(objArr);
                }
            });
            this.socket.on("log", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                }
            });
            this.socket.on("bye", new Emitter.Listener() {

                @Override
                public final void call(Object[] objArr) {
                    lambda$init$5$SignallingClient(objArr);
                }
            });
            this.socket.on("message", new Emitter.Listener() {

                @Override
                public final void call(Object[] objArr) {
                    lambda$init$6$SignallingClient(objArr);
                }
            });
            this.socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {

                @Override
                public final void call(Object[] objArr) {
                    Log.e("EVENT_CONNECT_ERROR", "join EVENT_CONNECT_ERROR() called with: args = [" + Arrays.toString(objArr) + "]");
                }
            });
        } catch (URISyntaxException | KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void lambda$init$0$SignallingClient(Object[] objArr) {
        Log.e("testing", "created call() called with: args = [" + Arrays.toString(objArr) + "]");
        this.isInitiator = true;
        this.callback.onCreatedRoom();
    }

    public void lambda$init$2$SignallingClient(Object[] objArr) {
        Log.e("testing", "join call() called with: args = [" + Arrays.toString(objArr) + "]");
        this.isChannelReady = true;
        this.callback.onNewPeerJoined();
    }

    public  void lambda$init$3$SignallingClient(Object[] objArr) {
        Log.e("testing", "joined call() called with: args = [" + Arrays.toString(objArr) + "]");
        this.isChannelReady = true;
        this.callback.onJoinedRoom();
    }

    public  void lambda$init$5$SignallingClient(Object[] objArr) {
        this.callback.onRemoteHangUp((String) objArr[0]);
    }

    public  void lambda$init$6$SignallingClient(Object[] objArr) {
        Log.e("testing", "message call() called with: args = [" + Arrays.toString(objArr) + "]");
        if (objArr[0] instanceof String) {
            Log.e("testing", "String received :: " + objArr[0]);
            String str = (String) objArr[0];
            if (str.equalsIgnoreCase("got user media")) {
                this.callback.onTryToStart();
            }
            if (str.equalsIgnoreCase("bye")) {
                this.callback.onRemoteHangUp(str);
            }
        } else if (objArr[0] instanceof JSONObject) {
            try {
                JSONObject jSONObject = (JSONObject) objArr[0];
                Log.e("testing_json", "Json Received :: " + jSONObject.toString());
                String string = jSONObject.getString("type");
                if (string.equalsIgnoreCase("offer")) {
                    this.callback.onOfferReceived(jSONObject);
                } else if (string.equalsIgnoreCase("answer") && this.isStarted) {
                    this.callback.onAnswerReceived(jSONObject);
                } else if (string.equalsIgnoreCase("candidate")) {
                    this.callback.onIceCandidateReceived(jSONObject);
                } else if (string.equalsIgnoreCase("chat_msg")) {
                    this.callback.onNewMessage(jSONObject);
                } else if (string.equalsIgnoreCase("emoji")) {
                    this.callback.onNewEmoji(jSONObject);
                } else if (string.equalsIgnoreCase("camera")) {
                    this.callback.onCameraSwitchNode(jSONObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void emitInitStatement(String str) {
        Log.e("testing", "emitInitStatement Room Created or joined: " + str);
        this.socket.emit("create or join", str);
    }

    public void emitMessage(String str) {
        Log.d("SignallingClient", "emitMessage() called with: message = [" + str + "]");
        this.socket.emit("message", str);
    }

    public void emitMessage(SessionDescription sessionDescription) {
        try {
            Log.d("SignallingClient", "emitMessage() called with: message = [" + sessionDescription + "]");
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", sessionDescription.type.canonicalForm());
            jSONObject.put("sdp", sessionDescription.description);
            jSONObject.put("is_video", false);
            Log.d("emitMessage", jSONObject.toString());
            this.socket.emit("message", jSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void emitChatMessage(String str) {
        Log.d("SignallingClient", "ChatMessage() called with: message = [" + str + "]");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", "chat_msg");
            jSONObject.put("msg_str", str);
            this.socket.emit("message", jSONObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void emitChatEmoji(String str, int i) {
        Log.d("SignallingClient", "ChatMessage() called with: message = [" + str + "]");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", "emoji");
            jSONObject.put("emoji_name", str);
            jSONObject.put("emoji_count", i);
            this.socket.emit("message", jSONObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void emitChatSwitchCameramsg(boolean z) {
        Log.d("SignallingClient", "ChatMessage() called with: message = [" + z + "]");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", "camera");
            jSONObject.put("cameraFlag", z);
            this.socket.emit("message", jSONObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void emitIceCandidate(IceCandidate iceCandidate) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", "candidate");
            jSONObject.put("label", iceCandidate.sdpMLineIndex);
            jSONObject.put("id", iceCandidate.sdpMid);
            jSONObject.put("candidate", iceCandidate.sdp);
            jSONObject.put("nikename", ApiConstant.getUserName(null));
            jSONObject.put("device_id", ApiConstant.getDeviceId());
            this.socket.emit("message", jSONObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.socket.emit("bye", this.roomName);
        this.socket.disconnect();
        this.socket.close();
    }
}
package com.boloji.videocallchat.Configuration;

import android.util.Log;

import com.nhancv.webrtcpeer.rtc_comm.ws.BaseSocketCallback;
import com.nhancv.webrtcpeer.rtc_comm.ws.DefaultSocketService;
import com.nhancv.webrtcpeer.rtc_comm.ws.SocketService;
import com.boloji.videocallchat.OtherActivities.AppCheck;

import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.io.FileInputStream;

public class WebConfigData {

    static String TAG = "WebSocket";
    static WebConfigData socketHandler;
    private SocketService socket;
    WebSocketConnectListener webSocketConnectListener;

    public interface WebSocketConnectListener {
        void WebSocketConnected(SocketService socketService);
    }

    public static WebConfigData getInstance() {
        if (socketHandler == null) {
            socketHandler = new WebConfigData();
        }
        return socketHandler;
    }

    public void DisconnectSocket() {
        SocketService socketService = this.socket;
        if (socketService != null && socketService.isConnected()) {
            this.socket.close();
            Log.e(TAG, "Socket disconnected!!");
        }
    }

    public void SocketConnect(WebSocketConnectListener webSocketConnectListener2) {
        this.webSocketConnectListener = webSocketConnectListener2;
        SocketService socketService = this.socket;
        if (socketService == null || !socketService.isConnected()) {
            initSocket();
        } else {
            this.webSocketConnectListener.WebSocketConnected(this.socket);
        }
    }

    public void initSocket() {
        try {
            Log.e("settCir", "==");
            this.socket = new DefaultSocketService();
            String substring = AppCheck.getInstance().appPrefs.getCertificateFile().substring(AppCheck.getInstance().appPrefs.getCertificateFile().lastIndexOf(47) + 1);
            File file = new File(WebConstantData.currentPath + "/" + substring);
            StringBuilder sb = new StringBuilder();
            sb.append("==");
            sb.append(file.getAbsolutePath());
            Log.e("settCir", sb.toString());
            this.socket.setCertificateSSLFile(new FileInputStream(file));

            this.socket.connect(AppCheck.getInstance().appPrefs.getWebSocket(), new BaseSocketCallback() {
                /* class com.gtu.newlivevideocall.WebSocketConfiguration.WebsocketConfigurations.AnonymousClass1 */

                @Override
                // com.nhancv.webrtcpeer.rtc_comm.ws.BaseSocketCallback, com.nhancv.webrtcpeer.rtc_comm.ws.SocketCallBack
                public void onOpen(ServerHandshake serverHandshake) {
                    try {
                        Log.e("settCir", "==COnnct");
                        if (WebConfigData.this.webSocketConnectListener != null) {
                            WebConfigData.this.webSocketConnectListener.WebSocketConnected(WebConfigData.this.socket);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

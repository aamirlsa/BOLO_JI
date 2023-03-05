package com.boloji.videocallchat.NetworkConnection;

import android.util.Log;

import com.boloji.videocallchat.InternetConnection.NetworkDialog;
import com.boloji.videocallchat.OtherActivities.AppCheck;
import com.boloji.videocallchat.OtherData.ReSchedualCall;

import org.json.JSONObject;

import java.util.Arrays;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.functions.Action1;

public class SocketConfig {
    static String TAG = "Socket";
    static SocketConfig socketHandler;
    SocketListner callbacks;
    NetworkDialog internetDialog;
    private Socket socket;

    public void UnregisterClient(SocketListner socketCallBackListener) {
    }

    public static SocketConfig getInstance() {
        if (socketHandler == null) {
            socketHandler = new SocketConfig();
        }
        return socketHandler;
    }

    public <T> void registerClient(T t) {
        if (t instanceof SocketListner) {
            Log.e(TAG, "registerClient:");
            this.callbacks = (SocketListner) t;
        }
    }

    public void DisconnectScoket() {
        Socket socket2 = this.socket;
        if (socket2 != null && socket2.connected()) {
            Log.e(TAG, "Socket disconnected!!");
            this.socket.close();
            this.socket.disconnect();
        }
    }

    public void SocketConnect() {

        Socket socket2 = this.socket;
        if (socket2 == null) {

            initSocket();
        } else if (!socket2.connected()) {

            this.socket.connect();
        } else {
            SocketListner socketCallBackListener = this.callbacks;
            if (socketCallBackListener != null) {

                socketCallBackListener.GetChat(this.socket);
            }
        }
    }

    public void emitCall(JSONObject jSONObject) {
        this.socket.emit("req", jSONObject);
        Log.e(TAG, "emitCall "+jSONObject.toString());
    }

    public void initSocket() {
        if (this.socket == null) {
            try {
                this.socket = AppCheck.GetSocket();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Socket initialization!! ");
                sb.append(this.socket != null);
                Log.e(str, sb.toString());


                this.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {


                    @Override
                    public void call(Object... objArr) {
                        if (SocketConfig.this.socket.connected()) {
                            try {
                                if (SocketConfig.this.internetDialog != null && SocketConfig.this.internetDialog.isShowing()) {
                                    SocketConfig.this.internetDialog.dismiss();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (SocketConfig.this.callbacks != null) {
                                SocketConfig.this.callbacks.GetChat(SocketConfig.this.socket);
                                return;
                            }
                            return;
                        }
                        Log.e(SocketConfig.TAG, "EVENT_CONNECT-->  Error ");
                    }
                });
                this.socket.on("res", new Emitter.Listener() {


                    @Override
                    public void call(Object... objArr) {
                        if (SocketConfig.this.callbacks != null) {
                            JSONObject jSONObject = (JSONObject) objArr[0];
                            SocketConfig.this.callbacks.call(objArr);
                            Log.e(SocketConfig.TAG, "EVENT_CONNECT-->  call "+ Arrays.toString(objArr));
                        }
                    }
                });
                this.socket.on("reconnecting", new Emitter.Listener() {


                    @Override
                    public void call(Object... objArr) {
                        String str = SocketConfig.TAG;
                        Log.e(str, "EVENT_RECONNECTING " + SocketConfig.this.socket.connected());
                    }
                });
                this.socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {


                    @Override
                    public void call(Object... objArr) {
                        ReSchedualCall.runOnUi(new Action1() {


                            @Override
                            public final void call(Object obj) {
                                try {
                                    SocketConfig.this.internetDialog = new NetworkDialog.Builder(AppCheck.getInstance().activity).build();
                                    SocketConfig.this.internetDialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        String str = SocketConfig.TAG;
                        Log.e(str, "EVENT_DISCONNECT " + SocketConfig.this.socket.connected());
                    }


                });
                this.socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {


                    @Override
                    public void call(Object... objArr) {
                        String str = SocketConfig.TAG;
                        Log.e("EVENT_CONNECT_ERROR", "EVENT_CONNECT_ERROR " + Arrays.toString(objArr));
                    }
                });
                this.socket.on("connect_timeout", new Emitter.Listener() {


                    @Override
                    public void call(Object... objArr) {
                        String str = SocketConfig.TAG;
                        Log.e(str, "EVENT_CONNECT_TIMEOUT " + SocketConfig.this.socket.connected());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

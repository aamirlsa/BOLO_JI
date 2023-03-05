package com.boloji.videocallchat.NetworkConnection;

import io.socket.client.Socket;

public interface SocketListner {
    void GetChat(Socket socket);

    void call(Object... objArr);
}

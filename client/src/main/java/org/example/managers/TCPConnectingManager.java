package org.example.managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class TCPConnectingManager {
    private Socket socket;
    public TCPConnectingManager(String hostname, int port) throws IOException {
        this.socket = new Socket(hostname, port);
    }
    public OutputStream getOut() throws IOException {
        return socket.getOutputStream();
    }
    public InputStream getIn() throws IOException {
        return socket.getInputStream();
    }
    public void setKeepAliveMode(int mills) throws SocketException {
        socket.setKeepAlive(true);
        socket.setSoTimeout(mills);
    }
}

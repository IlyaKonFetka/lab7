package org.example.managers;

import org.example.managers.TCPConnectingManager;

import java.io.*;
import java.net.SocketException;

public class TCPSendManager {
    private PrintWriter out;
    private OutputStream outputStream;

    public TCPSendManager(TCPConnectingManager connection) throws IOException {
        outputStream = connection.getOut();
        out = new PrintWriter(new OutputStreamWriter(outputStream), true);
    }

    public PrintWriter getOut() {
        return out;
    }

    public void push(String jsonMessage) throws IOException {
        byte[] messageBytes = jsonMessage.getBytes("UTF-8");
        int messageLength = messageBytes.length;

        DataOutputStream dataOut = new DataOutputStream(outputStream);
        dataOut.writeInt(messageLength);

        dataOut.write(messageBytes);
        dataOut.flush();
    }

}

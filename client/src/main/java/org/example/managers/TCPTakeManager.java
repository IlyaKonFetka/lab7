package org.example.managers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

public class TCPTakeManager {
    private BufferedReader in;
    private InputStream inputStream;

    public TCPTakeManager(TCPConnectingManager connection) throws IOException {
        this.inputStream = connection.getIn();
        this.in = new BufferedReader(new InputStreamReader(inputStream));
    }

    public BufferedReader getIn() {
        return in;
    }

    public String take() throws IOException {
        try {
            DataInputStream dataIn = new DataInputStream(inputStream);

            int messageLength = dataIn.readInt();
            byte[] messageBytes = new byte[messageLength];

            dataIn.readFully(messageBytes);
            return new String(messageBytes, "UTF-8");
        } catch (SocketTimeoutException e) {
            throw new IOException("Timeout waiting for server response", e);
        }
    }

}

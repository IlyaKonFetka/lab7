package org.example.managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TCPConnectingManager implements AutoCloseable {
    private ServerSocketChannel serverSocket;
    private Selector selector;

    public TCPConnectingManager(int port) throws IOException {
        this.serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(port));

        this.selector = Selector.open();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    }

    public Selector getSelector() {
        return selector;
    }

    public ServerSocketChannel getServerSocket() {
        return serverSocket;
    }

    public void waitForNewConnect() throws IOException {
        selector.select();
    }

    public SocketChannel acceptConnection() throws IOException {
        SocketChannel clientChannel = serverSocket.accept();
        if (clientChannel != null) {
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
        }
        return clientChannel;
    }

    @Override
    public void close() throws IOException {
        try {
            if (selector != null) {
                selector.close();
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}

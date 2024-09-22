package org.example.running;

import com.google.gson.JsonSyntaxException;
import org.example.Main;
import org.example.TCP_components.Request;
import org.example.TCP_components.Response;
import org.example.database.UsersTableHelper;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.WrongUserPasswordException;
import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CommandManager;
import org.example.managers.TCPConnectingManager;
import org.example.managers.TCPSerializationManager;
import org.example.user.User;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Runner {
    private Console console;
    private CommandManager commandManager;
    private CommandManager serverCommandManager;

    private TCPConnectingManager connector;
    private TCPSerializationManager serializator;

    private UsersTableHelper usersTableHelper;

    private final ForkJoinPool readPool = ForkJoinPool.commonPool();
    private final ExecutorService processPool = Executors.newCachedThreadPool();
    private final ForkJoinPool writePool = new ForkJoinPool();

    public Runner(Console console,
                  CommandManager commandManager,
                  CommandManager servarCommandManager,
                  TCPConnectingManager connector,
                  TCPSerializationManager serializator,
                  UsersTableHelper usersTableHelper) {
        this.console = console;
        this.commandManager = commandManager;
        this.serverCommandManager = servarCommandManager;
        this.connector = connector;
        this.serializator = serializator;
        this.usersTableHelper = usersTableHelper;
    }

    public void interactiveMode() {
        new Thread(this::handleConsoleInput).start();

        while (true) {
            try {
                connector.waitForNewConnect();
                Iterator<SelectionKey> keyIterator = connector.getSelector().selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    try {
                        if (key.isAcceptable()) {
                            SocketChannel channel = connector.acceptConnection();
                            Main.logger.info("One more connected: " + channel.getRemoteAddress());
                            console.prompt();
                        } else if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            handleClientRead(clientChannel);
                        }
                    } catch (IOException e) {
                        handleClientError(key, e);
                    }
                }
            } catch (IOException e) {
                Main.logger.error("Error in main server loop: " + e.getMessage());
                console.printError("Ошибка в главном цикле сервера. Перезапуск цикла...");
            } catch (Exception e) {
                Main.logger.error("Unexpected error in main server loop: " + e.getMessage());
                console.printError("Непредвиденная ошибка в главном цикле сервера. Перезапуск цикла...");
            }
        }
    }
    private void handleClientRead(SocketChannel clientChannel) {
        CompletableFuture.supplyAsync(() -> {
                    try {
                        return readMessage(clientChannel);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, readPool)
                .thenApplyAsync(this::processRequest, processPool)
                .thenAcceptAsync(response -> sendResponse(clientChannel, response), writePool)
                .exceptionally(e -> {
                    handleClientError(clientChannel, e);
                    return null;
                });
    }

    private void handleClientError(SelectionKey key, Exception e) {
        try {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            Main.logger.info(clientChannel.getRemoteAddress() + ": Клиент отключился или произошла ошибка");
            console.printWarning("Клиент отключился или произошла ошибка: " + e.getMessage());
            console.prompt();
        } catch (Exception ex) {
            Main.logger.error("Error while handling client error: " + ex.getMessage());
        } finally {
            try {
                key.cancel();
                key.channel().close();
            } catch (IOException ioEx) {
                Main.logger.error("Error while closing client channel: " + ioEx.getMessage());
            }
        }
    }
    private void handleClientError(SocketChannel clientChannel, Throwable e) {
        try {
            Main.logger.info(clientChannel.getRemoteAddress() + ": Клиент отключился или произошла ошибка");
            console.printWarning("Клиент отключился или произошла ошибка: " + e.getMessage());
            console.prompt();
        } catch (Exception ex) {
            Main.logger.error("Error while handling client error: " + ex.getMessage());
        } finally {
            try {
                clientChannel.close();
            } catch (IOException ioEx) {
                Main.logger.error("Error while closing client channel: " + ioEx.getMessage());
            }
        }
    }


    private Request readMessage(SocketChannel clientChannel) throws IOException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        int bytesRead = 0;
        while (bytesRead < 4) {
            int result = clientChannel.read(lengthBuffer);
            if (result == -1) {
                throw new IOException("Connection closed by client");
            }
            bytesRead += result;
        }
        lengthBuffer.flip();
        int messageLength = lengthBuffer.getInt();

        ByteBuffer messageBuffer = ByteBuffer.allocate(messageLength);
        bytesRead = 0;
        while (bytesRead < messageLength) {
            int result = clientChannel.read(messageBuffer);
            if (result == -1) {
                throw new IOException("Connection closed by client");
            }
            bytesRead += result;
        }
        messageBuffer.flip();
        String message = StandardCharsets.UTF_8.decode(messageBuffer).toString();

        return serializator.request(message);
    }

    private Response processRequest(Request request) {
        try {
            User user = serializator.user(request.getSerializedUser());
            long userID = -1;

            if (!request.getCommandName().equals("register")) {
                try {
                    userID = usersTableHelper.checkUserPassword(user);
                } catch (UserNotFoundException | WrongUserPasswordException e) {
                    return new Response(false, "Неверные login или password", "");
                } catch (SQLException e) {
                    return new Response(false, "Ошибка в работе базы данных пользователей", "");
                }
            }

            if (!request.getCommandName().equals("auth")) {
                Main.logger.info("Processing command: " + request.getCommandName());
            }

            if (request.getCommandName().equals("exit")) {
                return new Response(true, "Выход выполнен успешно", "");
            }

            return launchCommand(request, commandManager, userID);
        } catch (Exception e) {
            Main.logger.error("Неожиданная ошибка при обработке запроса: " + e.getMessage());
            return new Response(false, "Остаточная ошибка сервера. Попробуйте снова...", "");
        }
    }
    private void sendResponse(SocketChannel clientChannel, Response response) {
        try {
            String message = serializator.serialize(response);
            byte[] responseBytes = message.getBytes(StandardCharsets.UTF_8);
            ByteBuffer lengthBuffer = ByteBuffer.allocate(4).putInt(responseBytes.length);
            lengthBuffer.flip();
            while (lengthBuffer.hasRemaining()) {
                clientChannel.write(lengthBuffer);
            }

            ByteBuffer messageBuffer = ByteBuffer.wrap(responseBytes);
            while (messageBuffer.hasRemaining()) {
                clientChannel.write(messageBuffer);
            }
        } catch (IOException e) {
            Main.logger.error("Ошибка при отправке ответа: " + e.getMessage());
        }
    }

    private void sendMessage(SocketChannel clientChannel, String message) throws IOException {
        byte[] responseBytes = message.getBytes("UTF-8");
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        lengthBuffer.putInt(responseBytes.length);
        lengthBuffer.flip();
        clientChannel.write(lengthBuffer);

        ByteBuffer messageBuffer = ByteBuffer.wrap(responseBytes);
        while (messageBuffer.hasRemaining()) {
            clientChannel.write(messageBuffer);
        }
    }


    private void handleConsoleInput() {

        try {
            Response response;
            String responseBody;

            String userCommandName;
            String userCommandArgument;

            while (true) {
                console.prompt();
                userCommandName = console.read().trim();
                userCommandArgument = console.readln().trim();

                response = launchCommand(new Request(userCommandName,"",userCommandArgument), serverCommandManager, 0);
                responseBody = response.getResponseBody();

                if (response.isStatus()){
                    if (responseBody != null && !responseBody.isEmpty()) {
                        Main.logger.info(response.getResponseBody());
                    }
                    console.printSuccessful(response.getTextStatus());
                }else {
                    if (responseBody == null || responseBody.isEmpty()) {
                        Main.logger.info(response.getResponseBody());
                    }
                    console.printError(response.getTextStatus());
                }
            }
        } catch (NoSuchElementException exception) {
            console.printError("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
        }
    }
    private Response launchCommand(Request request, CommandManager currentCommandManager, long userID) {
        String commandName = request.getCommandName();
        String stringRequestBody = request.getSerializedRequestBody();

        if (commandName.isEmpty()) return new Response(false,"Пустой запрос","");
        ServerExecutable command = (ServerExecutable) currentCommandManager.getCommands().get(commandName);

        if (command == null) return new Response(false, "Команда '" + commandName
                + "' не найдена на сервере. Наберите 'help' для справки", "");
        currentCommandManager.addToHistory(commandName);
        return command.apply(stringRequestBody, userID);
    }
}
package org.example.running;


import com.google.gson.JsonSyntaxException;
import org.example.TCP_components.Request;
import org.example.TCP_components.Response;
import org.example.cmd.utils.Ask;
import org.example.exceptions.ExecutionException;
import org.example.exceptions.ExitException;
import org.example.exceptions.NoSuchCommandFoundException;
import org.example.exceptions.NullRequest;
import org.example.interfaces.ClientExecutable;
import org.example.interfaces.Console;
import org.example.managers.CommandManager;
import org.example.managers.TCPSendManager;
import org.example.managers.TCPSerializationManager;
import org.example.managers.TCPTakeManager;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;

public class Runner {
    private Console console;
    private CommandManager commandManager;
    private CommandManager noLoginCommandManager;
    private TCPSendManager sender;
    private TCPTakeManager taker;
    private TCPSerializationManager packager;


    public Runner(Console console,
                  CommandManager commandManager,
                  CommandManager noLoginCommandManager,
                  TCPSendManager sender,
                  TCPTakeManager taker,
                  TCPSerializationManager packager) {
        this.console = console;
        this.commandManager = commandManager;
        this.noLoginCommandManager = noLoginCommandManager;
        this.sender = sender;
        this.taker = taker;
        this.packager = packager;
    }
    public void interactiveMode() {
        Request loginRequest;
        Request bodyRequest;
        Request loginBodyRequest;
        Response response;
        String[] userInput;
        String strResponse;
        main:
        while (true) {
            try{
                do {
                    console.println("Введите auth для авторизации или register для регистрации");
                    console.prompt();
                    userInput = takeUserInput();
                    if(userInput[0].equals("exit")){
                        if(!commandManager.isScript()){
                            console.printSuccessful("Завершение работы...");
                            powerOff();
                        }else break main;
                    }
                    loginRequest = launchLoginCommand(userInput[0], userInput[1]);
                    sendRequest(loginRequest);
                    strResponse = taker.take();
                    response = takeResponse(strResponse);
                } while (!response.isStatus());
                console.println("Введите команду...");
                console.prompt();
                userInput = takeUserInput();
                console.prompt();
                bodyRequest = launchCommand(userInput[0], userInput[1]);
                loginBodyRequest = new Request(bodyRequest.getCommandName(),
                        loginRequest.getSerializedUser(),
                        bodyRequest.getSerializedRequestBody());
                sendRequest(loginBodyRequest);
                strResponse = taker.take();
                takeResponse(strResponse);
            } catch (NoSuchElementException exception) {
                console.printError("Пользовательский ввод не обнаружен");
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка");
            } catch (Ask.AskBreak | NullRequest ignored) {
            } catch (NoSuchCommandFoundException e) {
                console.printWarning(e.getMessage());
            } catch (ExecutionException e) {
                console.printError("Ошибка выполнения команды...");
                console.printError(e);
            } catch (ExitException e) {
                console.printSuccessful("Отмена...");
            } catch (SocketTimeoutException e) {
                console.printError("Сервер не отвечает. Возможно, он отключен.");
            } catch (IOException e) {
                console.printError("Ошибка связи с сервером: " + e.getMessage());
            } finally {
                loginRequest = null;
                bodyRequest = null;
                loginBodyRequest = null;
                response = null;
                userInput = null;
                strResponse = null;
            }
        }

    }
    public String[] takeUserInput(){
        String userCommandName;
        String userCommandArgument;
        do {
            userCommandName = console.read().trim();
            userCommandArgument = console.readln().trim();
        } while (userCommandName.isEmpty());
        return new String[]{userCommandName, userCommandArgument};
    }
    private void sendRequest(Request request) throws NullRequest, ExitException {
        try {
            if (request == null) {
                throw new NullRequest();
            }
            if (request.getSerializedRequestBody().equals("exit")) {
                throw new ExitException();
            }
            String stringRequest = packager.serialize(request);


            sender.push(stringRequest);
        } catch (IOException e) {
            console.printError("Сервер не отвечает");
        } catch (JsonSyntaxException e) {
            console.printError("Ошибка чтения ответа сервера");
        }
    }

    public Response takeResponse(String serializedResponse){
        Response response = packager.response(serializedResponse);
        String responseBody = response.getResponseBody();
        if (response.isStatus()) {
            console.printSuccessful(response.getTextStatus());
        } else {
            console.printError(response.getTextStatus());
        }
        if (responseBody != null && !responseBody.isEmpty()) {
            console.println(responseBody);
        }
        return response;
    }
    private Request launchCommand(String userCommandName, String userCommandArgument) throws ExecutionException,
            Ask.AskBreak, NoSuchCommandFoundException {
        ClientExecutable command = (ClientExecutable) commandManager.getCommands().get(userCommandName);

        if (command == null) {
            throw new NoSuchCommandFoundException("Команда '" + userCommandName + "' не найдена у клиента. " +
                    "Наберите 'help' для справки");
        }
        commandManager.addToHistory(userCommandName);
        return command.apply(userCommandArgument, commandManager.isScript());
    }
    private Request launchLoginCommand(String userCommandName, String userCommandArgument) throws ExecutionException,
            Ask.AskBreak, NoSuchCommandFoundException {
        ClientExecutable command = (ClientExecutable) noLoginCommandManager.getCommands().get(userCommandName);
        if (command == null) {
            throw new NoSuchCommandFoundException("Команда '" + userCommandName + "' не найдена у клиента.");
        }

        commandManager.addToHistory(userCommandName);
        return command.apply(userCommandArgument, noLoginCommandManager.isScript());
    }
    public void powerOff(){
        System.exit(777);
    }
}
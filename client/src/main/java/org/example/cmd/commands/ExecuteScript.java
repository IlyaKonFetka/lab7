package org.example.cmd.commands;

import org.example.interfaces.ClientExecutable;
import org.example.managers.CommandManager;
import org.example.running.Runner;
import org.example.TCP_components.Request;
import org.example.cmd.utils.Ask;
import org.example.cmd.utils.SmartInputParser;
import org.example.exceptions.ExecutionException;
import org.example.interfaces.Console;
import org.example.managers.TCPSendManager;
import org.example.managers.TCPSerializationManager;
import org.example.managers.TCPTakeManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ExecuteScript extends ExecuteScript_Description implements ClientExecutable {
    private Console console;
    private CommandManager commandManager;
    private CommandManager noLoginCommandManager;
    private TCPSendManager sender;
    private TCPTakeManager taker;
    private TCPSerializationManager serializator;

    public ExecuteScript(Console console,
                         CommandManager commandManager,
                         CommandManager noLoginCommandManager,
                         TCPSendManager sender,
                         TCPTakeManager taker,
                         TCPSerializationManager serializator) {
        super();
        this.console = console;
        this.commandManager = commandManager;
        this.noLoginCommandManager = noLoginCommandManager;
        this.sender = sender;
        this.taker = taker;
        this.serializator = serializator;
    }

    @Override
    public Request apply(String userCommandArgument, boolean isScript) throws Ask.AskBreak, ExecutionException {
        String stringPath;
        if (userCommandArgument.isEmpty()) {
            stringPath = SmartInputParser.parseToString(console,"file-script path", isScript);
        }else stringPath = userCommandArgument;

        try {
            File file = new File(stringPath);

            if (!file.exists()) {
                throw new ExecutionException("Данный файл не найден");
            }
            console.selectFileScanner(new Scanner(file));

            if(commandManager.addScriptFileName(stringPath))
                new Runner(console, commandManager, noLoginCommandManager, sender, taker, serializator)
                        .interactiveMode();
            else {
                throw new ExecutionException("Обнаружена рекурсия. Скрипт остановлен");
            }
        } catch (FileNotFoundException e){
            throw new ExecutionException("Данный файл не найден");
        } finally {
            console.selectConsoleScanner();
            commandManager.deleteLastScriptFileName(stringPath);
            commandManager.cleanScriptStack();
        }
        return new Request(getName(),"","");
    }
}

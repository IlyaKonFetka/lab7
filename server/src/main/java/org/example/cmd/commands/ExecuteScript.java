package org.example.cmd.commands;

import org.example.TCP_components.Response;
import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CommandManager;

public class ExecuteScript extends ExecuteScript_Description implements ServerExecutable {
    public ExecuteScript() {
        super();
    }

    @Override
    public Response apply(String userCommandArgument, long userID) {
        return new Response(true,"Скрипт успешно выполнен","");
    }
}

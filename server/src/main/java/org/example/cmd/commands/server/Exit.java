package org.example.cmd.commands.server;

import org.example.TCP_components.Response;
import org.example.cmd.commands.Exit_Description;
import org.example.cmd.utils.Command;
import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;


public class Exit extends Exit_Description implements ServerExecutable {
    private Console console;
    public Exit(Console console) {
        super();
        this.console = console;
    }

    @Override
    public Response apply(String userCommandArgument, long userID) {
        console.printSuccessful("Завершение работы...");
        System.exit(777);
        return new Response(true,"","");
    }
}

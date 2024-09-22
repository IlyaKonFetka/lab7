package org.example.cmd.commands;


import org.example.TCP_components.Request;
import org.example.cmd.utils.Command;
import org.example.interfaces.ClientExecutable;
import org.example.interfaces.Console;

public class Show extends Show_Description implements ClientExecutable {
    Console console;
    public Show(Console console) {
        super();
        this.console = console;
    }

    @Override
    public Request apply(String userCommandArgument, boolean isScript) {
        return new Request(getName(),"","");
    }
}

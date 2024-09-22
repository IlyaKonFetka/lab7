package org.example.cmd.commands;


import org.example.TCP_components.Request;
import org.example.cmd.utils.Command;
import org.example.interfaces.ClientExecutable;

public class Clear extends Clear_Description implements ClientExecutable {

    public Clear() {
        super();
    }

    @Override
    public Request apply(String userCommandArgument, boolean isScript) {
        return new Request(getName(), "","");
    }
}

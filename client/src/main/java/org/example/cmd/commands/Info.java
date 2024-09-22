package org.example.cmd.commands;

import org.example.TCP_components.Request;
import org.example.cmd.utils.Command;
import org.example.interfaces.ClientExecutable;
import org.example.interfaces.Console;
import org.example.model.Person;

public class Info extends Info_Description implements ClientExecutable {
    public Info() {
        super();
    }

    @Override
    public Request apply(String userCommandArgument, boolean isScript) {
        return new Request(getName(),"","");
    }
}

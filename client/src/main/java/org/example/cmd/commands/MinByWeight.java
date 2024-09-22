package org.example.cmd.commands;

import org.example.TCP_components.Request;
import org.example.cmd.utils.Command;
import org.example.interfaces.ClientExecutable;
import org.example.interfaces.Console;
import org.example.model.Person;

public class MinByWeight extends MinByWeight_Description implements ClientExecutable {
    public MinByWeight() {
        super();
    }

    @Override
    public Request apply(String userCommandArgument, boolean isScript) {
        return new Request(getName(),"","");
    }
}

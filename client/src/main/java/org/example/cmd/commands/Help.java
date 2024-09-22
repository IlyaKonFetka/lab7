package org.example.cmd.commands;

import org.example.TCP_components.Request;
import org.example.cmd.utils.Command;
import org.example.interfaces.ClientExecutable;

public class Help extends Help_Description implements ClientExecutable {
    public Help() {
        super();
    }

    @Override
    public Request apply(String userCommandArgument, boolean isScript) {
        return new Request(getName(),"","");
    }
}

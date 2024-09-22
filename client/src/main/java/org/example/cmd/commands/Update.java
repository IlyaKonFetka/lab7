package org.example.cmd.commands;

import org.example.TCP_components.Request;
import org.example.cmd.utils.Ask;

import org.example.cmd.utils.SmartInputParser;
import org.example.interfaces.ClientExecutable;
import org.example.interfaces.Console;
import org.example.managers.TCPSerializationManager;
import org.example.model.Person;

public class Update extends Update_Description implements ClientExecutable {
    Console console;
    TCPSerializationManager serializator;
    public Update(Console console, TCPSerializationManager serializator) {
        super();
        this.console = console;
        this.serializator = serializator;
    }

    @Override
    public Request apply(String userCommandArgument, boolean isScript) throws Ask.AskBreak {
        int id;
        if (userCommandArgument.isEmpty()){
            id = SmartInputParser.parseTo_int(console,"id", true, isScript);
        } else {
            id = Integer.parseInt(userCommandArgument);
        }
        if(!isScript)console.println("Введите новые данные для этого Person: ");

        Person p = Ask.askPerson(console, id, null, isScript, -1);

        String stringPerson = serializator.serialize(p);
        return new Request(getName(), "",stringPerson);
    }
}

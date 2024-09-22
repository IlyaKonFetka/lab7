package org.example.cmd.commands;


import org.example.TCP_components.Request;
import org.example.cmd.utils.*;
import org.example.interfaces.ClientExecutable;
import org.example.interfaces.Console;
import org.example.managers.TCPSerializationManager;
import org.example.model.Person;

public class Add extends Add_Description implements ClientExecutable {
    private Console console;
    private TCPSerializationManager serializator;

    public Add(Console console, TCPSerializationManager serializator) {
        super();
        this.console = console;
        this.serializator = serializator;
    }

    @Override
    public Request apply(String userCommandArgument, boolean isScript) throws Ask.AskBreak {

        if (!isScript) {
            console.println("Введите данные для нового Person: ");
        }

        Person newPersonWithNullID = Ask.askPerson(console, null, null, isScript,-1);
        String stringNewPersonWithNullID = serializator.serialize(newPersonWithNullID);

        return new Request(getName(), "", stringNewPersonWithNullID);

    }
}

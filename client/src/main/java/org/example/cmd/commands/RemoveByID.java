package org.example.cmd.commands;

import org.example.TCP_components.Request;
import org.example.cmd.utils.Ask;
import org.example.cmd.utils.Command;
import org.example.cmd.utils.SmartInputParser;
import org.example.exceptions.ExecutionException;
import org.example.interfaces.ClientExecutable;
import org.example.interfaces.Console;
import org.example.model.Person;

public class RemoveByID extends RemoveByID_Description implements ClientExecutable {
    private Console console;
    public RemoveByID(Console console) {
        super();
        this.console = console;
    }

    @Override
    public Request apply(String userCommandArgument, boolean isScript) throws ExecutionException, Ask.AskBreak {
        int id;
        if (userCommandArgument.isEmpty()){
            id = SmartInputParser.parseTo_int(console,"id",true,isScript);
        }
        else {
            try {
                id = Integer.parseInt(userCommandArgument);
            } catch (NumberFormatException e) {
                throw new ExecutionException("Неверный формат id, введите целое число не меньше 0");
            }
        }
        return new Request(getName(), id+"","");
    }
}

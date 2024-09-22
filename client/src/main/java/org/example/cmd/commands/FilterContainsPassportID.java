package org.example.cmd.commands;

import org.example.TCP_components.Request;
import org.example.cmd.utils.Ask;
import org.example.cmd.utils.SmartInputParser;
import org.example.cmd.utils.Command;
import org.example.interfaces.ClientExecutable;
import org.example.interfaces.Console;
import org.example.model.Person;

public class FilterContainsPassportID extends FilterContainsPassportID_Description implements ClientExecutable {
    Console console;
    public FilterContainsPassportID(Console console) {
        super();
        this.console = console;
    }

    @Override
    public Request apply(String userCommandArgument, boolean isScript) throws Ask.AskBreak {
        String subStr;
        if (userCommandArgument.isEmpty()) {
            if(!isScript){
                console.print("Введите подстроку: ");
            }
            subStr = SmartInputParser.parseToString(console, "substring of passportID", isScript);

        }else subStr = userCommandArgument;
        return new Request(getName(), "",subStr);
    }
}

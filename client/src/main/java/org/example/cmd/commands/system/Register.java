package org.example.cmd.commands.system;

import org.example.TCP_components.Request;
import org.example.cmd.utils.Ask;
import org.example.cmd.utils.Command;
import org.example.cmd.utils.Hash;
import org.example.exceptions.ExecutionException;
import org.example.interfaces.ClientExecutable;
import org.example.interfaces.Console;
import org.example.managers.TCPSerializationManager;
import org.example.user.User;


public class Register extends Register_Description implements ClientExecutable {
    private Console console;
    private TCPSerializationManager serializator;

    public Register(Console console, TCPSerializationManager serializator) {
        super();
        this.console = console;
        this.serializator = serializator;
    }

    @Override
    public Request apply(String userCommandArgument, boolean isScript) throws Ask.AskBreak, ExecutionException {

        String[] args = userCommandArgument.split(" ",2);
        User user;
        if (args.length == 2){
            user = new User(args[0], Hash.stringToSha256(args[1]));
        }else{
            console.println("Введите данные для нового пользователя.");
            user = Ask.askUser(console, isScript);
        }
        String serUser = serializator.serialize(user);
        return new Request(getName(), serUser, serUser);
    }
}

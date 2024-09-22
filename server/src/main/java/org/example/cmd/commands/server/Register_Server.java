package org.example.cmd.commands.server;

import com.google.gson.JsonSyntaxException;
import org.example.TCP_components.Request;
import org.example.TCP_components.Response;
import org.example.cmd.commands.system.Register_Description;
import org.example.cmd.utils.Ask;
import org.example.database.UsersTableHelper;
import org.example.exceptions.UserAlreadyExistException;
import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.managers.TCPSerializationManager;
import org.example.user.User;

import java.sql.SQLException;

public class Register_Server extends Register_Description implements ServerExecutable {
    CollectionManager collectionManager;
    Console console;
    TCPSerializationManager serializator;
    UsersTableHelper usersTableHelper;

    public Register_Server(CollectionManager collectionManager, Console console, TCPSerializationManager serializator, UsersTableHelper usersTableHelper) {
        super();
        this.collectionManager = collectionManager;
        this.console = console;
        this.serializator = serializator;
        this.usersTableHelper = usersTableHelper;
    }


    @Override
    public Response apply(String commandArgument, long userID) {
        console.print("Введите данные для нового пользователя.");
        User user;
        try {
            user = Ask.askUser(console, false);
        } catch (Ask.AskBreak e) {
            return new Response(false,"Команда отменена. Пользователь не добавлен","");
        }
        long id;
        try {
            id = usersTableHelper.insertUser(user.getLogin(),user.getHashPassword());
        } catch (SQLException e) {
            console.printError("Ошибка добавления пользователя в базу");
            console.printError(e);
            return new Response(false,"Ошибка добавления пользователя в базу","");
        } catch (UserAlreadyExistException e) {
            return new Response(false,e.getMessage(),"");
        }
        return new Response(true,"Регистрация успешна","");

    }
}
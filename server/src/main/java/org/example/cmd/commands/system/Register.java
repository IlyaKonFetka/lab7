package org.example.cmd.commands.system;

import com.google.gson.JsonSyntaxException;
import org.example.TCP_components.Response;
import org.example.database.UsersTableHelper;
import org.example.exceptions.UserAlreadyExistException;
import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.managers.TCPSerializationManager;
import org.example.model.Person;
import org.example.user.User;

import java.sql.SQLException;

public class Register extends Register_Description implements ServerExecutable {
    CollectionManager collectionManager;
    Console console;
    TCPSerializationManager serializator;
    UsersTableHelper usersTableHelper;

    public Register(CollectionManager collectionManager, Console console, TCPSerializationManager serializator, UsersTableHelper usersTableHelper) {
        super();
        this.collectionManager = collectionManager;
        this.console = console;
        this.serializator = serializator;
        this.usersTableHelper = usersTableHelper;
    }


    @Override
    public Response apply(String responseBody, long userID) {
        User user;
        try {
            user = serializator.user(responseBody);
        }catch (JsonSyntaxException e){
            console.printWarning("Ошибка чтения. Некорректная сериализация User");
            return new Response(false, "Невозможно десериализовать User","");
        }
        try {
             usersTableHelper.insertUser(user.getLogin(), user.getHashPassword());
        } catch (SQLException e) {
            console.printError("Ошибка добавления пользователя в базу");
            console.printError(e);
            return new Response(false,"Ошибка добавления пользователя в базу","");
        } catch (UserAlreadyExistException e) {
            return new Response(false,e.getMessage(), "");
        }
        return new Response(true,"Регистрация успешна","");
    }
}

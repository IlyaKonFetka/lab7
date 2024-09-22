package org.example.cmd.commands.system;

import org.example.TCP_components.Response;
import org.example.database.UsersTableHelper;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.WrongUserPasswordException;
import org.example.interfaces.ServerExecutable;
import org.example.managers.TCPSerializationManager;
import org.example.user.User;

import java.sql.SQLException;

public class Auth extends Auth_Description implements ServerExecutable {
    private UsersTableHelper usersTableHelper;
    TCPSerializationManager serializator;
    public Auth(UsersTableHelper usersTableHelper, TCPSerializationManager serializator) {
        super();
        this.usersTableHelper = usersTableHelper;
        this.serializator = serializator;
    }

    @Override
    public Response apply(String commandArgument, long userID) {
        return new Response(true,"авторизация успешна","");
    }
}

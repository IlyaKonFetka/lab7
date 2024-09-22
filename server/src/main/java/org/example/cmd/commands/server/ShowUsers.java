package org.example.cmd.commands.server;

import org.example.TCP_components.Response;
import org.example.cmd.commands.system.ShowUsers_Description;
import org.example.database.UsersTableHelper;
import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;

import java.sql.SQLException;

public class ShowUsers extends ShowUsers_Description implements ServerExecutable {
    Console console;
    UsersTableHelper usersTableHelper;
    public ShowUsers(Console console, UsersTableHelper usersTableHelper) {
        super();
        this.console = console;
        this.usersTableHelper = usersTableHelper;
    }

    @Override
    public Response apply(String commandArgument, long userID) {
        String tableContent;
        try {
            tableContent = usersTableHelper.getTableContents();
        } catch (SQLException e) {
            return new Response(false, "Ошибка при получении доступа к таблице","");
        }
        console.print(tableContent);
        return new Response(true, "Таблица успешно выведена","");
    }
}

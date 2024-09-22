package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.cmd.commands.*;
import org.example.cmd.commands.server.Exit;
import org.example.cmd.commands.server.Register_Server;
import org.example.cmd.commands.server.ShowUsers;
import org.example.cmd.commands.system.Auth;
import org.example.cmd.commands.system.Register;
import org.example.database.CollectionTableHelper;
import org.example.database.DBHelper;
import org.example.database.UsersTableHelper;
import org.example.exceptions.ValidateException;
import org.example.managers.*;
import org.example.running.Runner;

import java.io.*;
import java.sql.SQLException;


public class Main {

    public static final Logger logger = LogManager.getLogger("Server");

    public static void main(String[] args) {
        var console = new ConsoleManager();



        try {
            var connector = new TCPConnectingManager(Integer.parseInt(args[0]));
            var serializator = new TCPSerializationManager();

            var commandManager = new CommandManager();
            var serverCommandManager = new CommandManager();

            var dbHelper = new DBHelper(console,args[1],args[2],args[3],args[4]);
            var usersTableHelper = new UsersTableHelper(dbHelper, "users43241532154354314", console);
            var collectionTableHelper = new CollectionTableHelper(dbHelper,console, "collection431544214132423");

            var collectionManager = new CollectionManager(collectionTableHelper);

            try {
                collectionManager.loadCollection();
            } catch (ValidateException e) {
                console.printError("Объекты базы данных невалидны");
                console.printError("Завершение работы");
            }

            try {
                usersTableHelper.createTable();
                collectionTableHelper.createTable();
                console.printSuccessful("Таблицы базы успешно созданы или уже существуют");
            } catch (SQLException e) {
                console.printError("Ошибка создания таблиц(ы)");
                console.printError(e);
            }


            commandManager.register(new Add(console, collectionManager, serializator));
            commandManager.register(new AddIfMax(collectionManager, console, serializator));
            commandManager.register(new Clear(collectionManager));
            commandManager.register(new ExecuteScript());
            commandManager.register(new FilterContainsPassportID(collectionManager,console));
            commandManager.register(new Help(commandManager));
            commandManager.register(new History(commandManager));
            commandManager.register(new Info(collectionManager,console, collectionTableHelper, dbHelper));
            commandManager.register(new MinByWeight(collectionManager,console));
            commandManager.register(new PrintAscending(collectionManager,console));
            commandManager.register(new RemoveByID(collectionManager));
            commandManager.register(new RemoveLower(collectionManager,console));
            commandManager.register(new Show(collectionManager, console));
            commandManager.register(new Update(collectionManager,console,serializator));
            commandManager.register(new Register(collectionManager, console, serializator, usersTableHelper));
            commandManager.register(new Auth(usersTableHelper, serializator));

            serverCommandManager.register(new Exit(console));
            serverCommandManager.register(new Help(serverCommandManager));
            serverCommandManager.register(new Show(collectionManager,console));
            serverCommandManager.register(new ShowUsers(console,usersTableHelper));
            serverCommandManager.register(new Register_Server(collectionManager,console,serializator, usersTableHelper));



            new Runner(console,
                    commandManager,
                    serverCommandManager,
                    connector,
                    serializator,
                    usersTableHelper)
                    .interactiveMode();


        } catch (IOException e) {
            console.printError("Ошибка создания канала или селектора");
            console.printError(e);
            System.exit(1);
        }
    }
}
package org.example.cmd.commands;

import org.example.TCP_components.Response;
import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.model.Person;

public class PrintAscending extends PrintAscending_Description implements ServerExecutable {
    CollectionManager collectionManager;
    Console console;

    public static final String RESET = "\033[0m";
    public static final String LIGHT_GREEN = "\033[0;92m";

    public PrintAscending(CollectionManager collectionManager, Console console) {
        super();
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public Response apply(String userCommandArgument, long userID) {
        if (collectionManager.getCollection().isEmpty()) {
            return new Response(true, "Коллекция пуста", "");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Person p : collectionManager.getCollection()) {
            if (p.getOwnerID() == userID) {
                stringBuilder.append(LIGHT_GREEN)
                        .append(p.toString())
                        .append(RESET);
            } else {
                stringBuilder.append(p.toString());
            }
            stringBuilder.append("\n");
        }
        return new Response(true, "Коллекция успешно выведена", stringBuilder.toString());
    }
}


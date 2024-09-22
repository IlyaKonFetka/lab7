package org.example.cmd.commands;

import org.example.TCP_components.Response;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.interfaces.Console;
import org.example.model.Person;


public class Show extends Show_Description implements ServerExecutable {
    CollectionManager collectionManager;
    Console console;

    public static final String RESET = "\033[0m";

    public static final String GREEN = "\033[0;32m";
    public static final String LIGHT_GREEN = "\033[0;92m";
    public static final String CYAN = "\033[0;96m";
    public static final String YELLOW = "\033[0;93m";
    public static final String MAGENTA = "\033[0;95m";
    public static final String BLUE = "\033[0;94m";

    public Show(CollectionManager collectionManager, Console console) {
        super();
        this.collectionManager = collectionManager;
        this.console = console;
    }


    @Override
    public Response apply(String __, long userID) {
        if(collectionManager.getCollection().isEmpty()) {
            return new Response(true, "Коллекция пуста", "");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(Person p: collectionManager.getSortedById()) {
            String personString = p.toString();
            if (p.getOwnerID() == userID) {


                stringBuilder.append(LIGHT_GREEN)
                        .append(personString)
                        .append(RESET);
            } else {
                stringBuilder.append(personString);
            }
            stringBuilder.append("\n");
        }

        return new Response(true, "Коллекция успешно выведена", stringBuilder.toString());
    }
}

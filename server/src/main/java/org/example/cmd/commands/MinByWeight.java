package org.example.cmd.commands;

import org.example.TCP_components.Response;
import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.model.Person;

public class MinByWeight extends MinByWeight_Description implements ServerExecutable {
    CollectionManager collectionManager;
    Console console;

    public static final String RESET = "\033[0m";
    public static final String LIGHT_GREEN = "\033[0;92m";

    public MinByWeight(CollectionManager collectionManager, Console console) {
        super();
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public Response apply(String userCommandArgument, long userID) {
        if (collectionManager.getCollection().isEmpty()) {
            return new Response(true, "Коллекция пуста", "");
        }

        Long minWeight = Long.MAX_VALUE;
        Person minP = null;
        for (Person p : collectionManager.getCollection()) {
            if (p.getWeight() <= minWeight) {
                minP = p;
                minWeight = p.getWeight();
            }
        }
//        assert minP != null;
        String result = minP.getOwnerID() == userID ?
                LIGHT_GREEN + minP.toString() + RESET :
                minP.toString();
        return new Response(true, "Person с минимальным weight успешно получен", result);
    }
}

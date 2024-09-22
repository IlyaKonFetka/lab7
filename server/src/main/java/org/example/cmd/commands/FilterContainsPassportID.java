package org.example.cmd.commands;

import org.example.TCP_components.Response;
import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.model.Person;


public class FilterContainsPassportID extends FilterContainsPassportID_Description implements ServerExecutable {
    CollectionManager collectionManager;
    Console console;

    public static final String RESET = "\033[0m";
    public static final String LIGHT_GREEN = "\033[0;92m";

    public FilterContainsPassportID(CollectionManager collectionManager, Console console) {
        super();
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public Response apply(String subStr, long userID) {
        if (subStr.isEmpty()) {
            return new Response(false, "Подстрока пуста или передана некорректно", "");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Person p : collectionManager.getCollection()) {
            if (p.getPassportID().contains(subStr)) {
                String personString = p.toString();
                if (p.getOwnerID() == userID) {
                    stringBuilder.append(LIGHT_GREEN).append(personString).append(RESET);
                } else {
                    stringBuilder.append(personString);
                }
                stringBuilder.append("\n");
            }
        }
        if (stringBuilder.isEmpty()) {
            return new Response(true, "Объектов с данной подстрокой в паспорте не найдено", "");
        }
        return new Response(true, "Список объектов с данной подстрокой в паспорте успешно получен", stringBuilder.toString());
    }
}


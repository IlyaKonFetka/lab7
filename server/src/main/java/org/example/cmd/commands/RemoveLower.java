package org.example.cmd.commands;

import org.example.TCP_components.Response;
import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.model.Person;

public class RemoveLower extends RemoveLower_Description implements ServerExecutable {
    CollectionManager collectionManager;
    Console console;

    public RemoveLower(CollectionManager collectionManager, Console console) {
        super();
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public Response apply(String userCommandArgument, long userID) {
        if(collectionManager.getCollection().isEmpty()) {
            return new Response(true, "Коллекция пуста", "");
        }

        if (userCommandArgument.isEmpty()) {
            return new Response(false, "Имя пусто", "");
        }

        boolean removed = false;
        for (Person p : collectionManager.getCollection()) {
            if (p.getName().compareTo(userCommandArgument) < 0 && p.getOwnerID() == userID) {
                collectionManager.remove(p, userID);
                removed = true;
            }
        }

        if (removed) {
            return new Response(true, "Элементы успешно удалены", "");
        } else {
            return new Response(true, "Нет элементов для удаления", "");
        }
    }
}


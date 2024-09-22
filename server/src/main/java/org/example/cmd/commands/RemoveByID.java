package org.example.cmd.commands;

import org.example.TCP_components.Response;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.model.Person;

public class RemoveByID extends RemoveByID_Description implements ServerExecutable {
    CollectionManager collectionManager;

    public RemoveByID(CollectionManager collectionManager) {
        super();
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String userCommandArgument, long userID) {
        if(collectionManager.getCollection().isEmpty()) {
            return new Response(false, "Коллекция пуста", "");
        }

        int id;
        try {
            id = Integer.parseInt(userCommandArgument);
        } catch (NumberFormatException e) {
            return new Response(false, "Некорректный формат ID", "");
        }

        Person p = collectionManager.byId(id);
        if (p == null) {
            return new Response(false, "Элемента с таким id нет в коллекции", "");
        }

        if (p.getOwnerID() != userID) {
            return new Response(false, "У вас нет прав на удаление этого элемента", "");
        }

        if (collectionManager.remove(id, userID)) {
            return new Response(true, "Объект успешно удалён", "");
        } else {
            return new Response(false, "Непредвиденная ошибка, объект не удалён", "");
        }
    }
}


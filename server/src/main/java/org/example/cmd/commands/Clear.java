package org.example.cmd.commands;

import org.example.TCP_components.Response;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;

public class Clear extends Clear_Description implements ServerExecutable {
    CollectionManager collectionManager;
    public Clear(CollectionManager collectionManager) {
        super();
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String commandArgument, long userID) {
        int removedCount = collectionManager.cleanCollectionForUser(userID);
        if (removedCount == 0) {
            return new Response(true, "В коллекции нет элементов, принадлежащих вам", "");
        } else {
            return new Response(true, "Удалено " + removedCount + " элементов, принадлежащих вам", "");
        }
    }
}

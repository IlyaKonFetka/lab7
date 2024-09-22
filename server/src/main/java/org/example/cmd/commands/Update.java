package org.example.cmd.commands;

import org.example.TCP_components.Response;

import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.managers.TCPSerializationManager;
import org.example.model.Person;

public class Update extends Update_Description implements ServerExecutable {
    private Console console;
    private CollectionManager collectionManager;
    private TCPSerializationManager serializator;
    public Update(CollectionManager collectionManager, Console console, TCPSerializationManager serializator) {
        super();
        this.collectionManager = collectionManager;
        this.serializator = serializator;
        this.console = console;
    }

    @Override
    public Response apply(String userCommandArgument, long userID) {
        Person updatedPerson = serializator.person(userCommandArgument);
        int id = updatedPerson.getID();

        Person existingPerson = collectionManager.byId(id);
        if (existingPerson == null) {
            return new Response(false, "Элемента с таким id нет в коллекции", "");
        }

        if (existingPerson.getOwnerID() != userID) {
            return new Response(false, "У вас нет прав на обновление этого элемента", "");
        }

        updatedPerson.setOwnerID(userID);
        updatedPerson.setCreationDate(existingPerson.getCreationDate());
        if (collectionManager.update(updatedPerson, userID)) {
            return new Response(true, "Элемент успешно обновлён", "");
        } else {
            return new Response(false, "Не удалось обновить элемент", "");
        }
    }
}

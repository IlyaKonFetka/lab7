package org.example.cmd.commands;

import com.google.gson.JsonSyntaxException;
import org.example.TCP_components.Response;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.interfaces.Console;
import org.example.managers.TCPSerializationManager;
import org.example.model.Person;

public class AddIfMax extends AddIfMax_Description implements ServerExecutable {
    private CollectionManager collectionManager;
    private Console console;
    private TCPSerializationManager serializator;

    public AddIfMax(CollectionManager collectionManager , Console console, TCPSerializationManager serializator) {
        super();
        this.collectionManager = collectionManager;
        this.console = console;
        this.serializator = serializator;
    }

    @Override
    public Response apply(String argument, long userID) {
        Person person;
        try {
            person = serializator.person(argument);
            person.setOwnerID(userID);
        }catch (JsonSyntaxException e){
            console.printWarning("Ошибка чтения. Некорректная сериализация Person");
            return new Response(false, "Невозможно десериализовать Person","");
        }

        if (person == null){
            console.printWarning("Person пуст");
            return new Response(false, "Person пуст","");
        }

        if (!person.validate()){
            console.printWarning("Поля принятого Person не валидны");
            return new Response(false, "Поля Person не валидны","");
        }
        if (person.getName().compareTo(collectionManager.getMaxName()) > 0){
            collectionManager.append(person, userID);
            collectionManager.setLastInitTime();
            return new Response(true,"Новый Person успешно добавлен","");
        }else {
            return new Response(true,"Переданный Person не превосходит наибольший в коллекции. Объект не добавлен","");
        }
    }
}

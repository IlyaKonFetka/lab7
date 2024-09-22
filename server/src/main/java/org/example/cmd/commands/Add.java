package org.example.cmd.commands;


import com.google.gson.JsonSyntaxException;
import org.example.TCP_components.Response;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.interfaces.Console;
import org.example.managers.TCPSerializationManager;
import org.example.model.Person;

public class Add extends Add_Description implements ServerExecutable {
    private Console console;
    private CollectionManager collectionManager;
    private TCPSerializationManager serializator;

    public Add(Console console, CollectionManager collectionManager, TCPSerializationManager serializator){
        super();
        this.console = console;
        this.collectionManager = collectionManager;
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
        collectionManager.append(person, userID);
        collectionManager.setLastInitTime();
        return new Response(true,"Новый Person успешно добавлен","");
    }
}

package org.example.cmd.utils;

import org.example.interfaces.Console;
import org.example.model.Color;
import org.example.model.Coordinates;
import org.example.model.Location;
import org.example.model.Person;
import org.example.user.User;

import java.util.NoSuchElementException;

public class Ask{
    public static class AskBreak extends Exception{}

    public static Person askPerson(Console console, Integer id, String nameOrNull, boolean isRec, long ownerID)throws AskBreak{
        try {
            String name = nameOrNull==null? SmartInputParser.parseToString(console, "name", isRec):nameOrNull;
            var coordinates = askCoordinates(console, isRec);
            var location = askLocation(console, isRec);
            Long height = SmartInputParser.parseToLong(console, "height", true, isRec);

            Long weight = SmartInputParser.parseToLong(console, "weight", true, isRec);

            String passportID = SmartInputParser.parseToString(console, "passport ID", 4, isRec);
            Color hairColor;
            while (true){
                if(!isRec)console.print("hair color (green, blue, yellow): ");
                var line = console.readln().trim();
                if (!line.isEmpty()) {
                    switch (line) {
                        case "exit" -> throw new AskBreak();
                        case "null" -> hairColor = null;
                        case "green", "1" -> hairColor = Color.GREEN;
                        case "blue", "2" -> hairColor = Color.BLUE;
                        case "yellow", "3" -> hairColor = Color.YELLOW;
                        default -> {
                            if(!isRec)console.printWarning("Введите один из цветов или соответсвующий ему номер (1-3)");
                            continue;
                        }
                    }
                    break;
                } else if(!isRec)console.printWarning("Введите непустое значение");
            }
            return new Person(id, name, coordinates, location, height,weight,passportID, hairColor, ownerID);
        }catch (NoSuchElementException | IllegalStateException e){
            if(!isRec)console.printError("Ошибка чтения!");
            return null;
        }
    }

    public static Coordinates askCoordinates(Console console, boolean isRec)throws AskBreak{
        try{
            int x;
            while (true){
                if(!isRec)console.print("coordinates.x: ");
                var line = console.readln().trim();
                if (!line.isEmpty()) {
                    if(line.equals("exit")) throw new AskBreak();
                    else if (line.equals("null")) {
                        if(!isRec)console.printWarning("Не может быть null");
                    }
                    else try {
                        x = Integer.parseInt(line);
                        break;
                    }
                    catch(NumberFormatException e) {
                        if(!isRec){
                            console.printWarning("Число неверного формата");
                        };
                    }
                } else {
                    if(!isRec)console.printWarning("Введите непустое значение");
                }
            }
            float y;
            while (true) {
                if(!isRec)console.print("coordinates.y: ");
                var line = console.readln().trim();
                if (!line.isEmpty()) {
                    if (line.equals("exit")) throw new AskBreak();
                    else if (line.equals("null")) {
                        if(!isRec)console.printWarning("Не может быть null");
                    }
                    else try {
                        y = Float.parseFloat(line);
                        if (Float.isInfinite(y)){
                            if(!isRec)console.printWarning("Число слишком большое");
                            continue;
                        }
                        break;
                    }
                    catch(NumberFormatException e) {
                        if(!isRec)console.printWarning("Число неверного формата");
                    }
                }else if(!isRec)console.printWarning("Введите непустое значение");
            }
            return new Coordinates(x,y);
        }catch (NoSuchElementException | IllegalStateException e) {
            if(!isRec)console.printError("Ошибка чтения");
            return null;
        }
    }

    public static Location askLocation(Console console, boolean isRec)throws AskBreak{
        try{
            while (true) {
                if(!isRec)console.print("Создать Location? (yes/no) ");
                var line = console.readln().trim();
                if (!line.isEmpty()) {
                    if (line.equals("exit")) throw new AskBreak();
                    else if (line.equals("no")) return null;
                    else if (!line.equals("yes")) {
                        if(!isRec)console.printWarning("Введите \"yes\" или \"no\"");
                    }
                    else break;
                } else {
                    if(!isRec)console.printWarning("Введите непустое значение");
                }
            }
            float x;
            while (true){
                if(!isRec)console.print("location.x: ");
                var line = console.readln().trim();
                if (!line.isEmpty()) {
                    if(line.equals("exit")) throw new AskBreak();
                    else if (line.equals("null")) {
                        if(!isRec)console.printWarning("Не может быть null");
                    }
                    else try {
                        x = Float.parseFloat(line);
                        if (Float.isInfinite(x)){
                            if(!isRec)console.printWarning("Число слишком большое");
                            continue;
                        }
                        break;
                    }
                    catch(NumberFormatException e) {
                        if(!isRec)console.printWarning("Число неверного формата");
                    }
                } else {
                    if(!isRec)console.printWarning("Введите непустое значение");
                }
            }
            int y;
            while (true) {
                if(!isRec)console.print("location.y: ");
                var line = console.readln().trim();
                if (!line.isEmpty()){
                    if (line.equals("exit")) throw new AskBreak();
                    else if (line.equals("null")) {
                        if(!isRec)console.printWarning("Не может быть null");
                    }
                    else try {
                        y = Integer.parseInt(line);
                        break;
                    }
                    catch(NumberFormatException e) {
                        if(!isRec)console.printWarning("Число неверного формата");
                    }
                } else {
                    if(!isRec)console.printWarning("Введите непустое значение");
                }
            }
            String name = SmartInputParser.parseToString(console,"location.name", isRec);
            return new Location(x, y, name);
        }catch (NoSuchElementException | IllegalStateException e) {
            if(!isRec)console.printError("Ошибка чтения");
            return null;
        }
    }
    public static User askUser(Console console, boolean isRec) throws AskBreak {
        String login;
        while (true){
            login = SmartInputParser.parseToString(console,"login",isRec);
            if(login.equals("register")) {
                console.printWarning("Имя пользователя не может быть register");
            } else if (login.equals("auth")) {
                console.printWarning("Имя пользователя не может быть auth");
            } else break;
        }
        String password = SmartInputParser.parseToString(console,"password",isRec);
        String hashPassword = Hash.stringToSha256(password);
        return new User(login, hashPassword);
    }
}
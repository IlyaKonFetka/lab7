package org.example.cmd.commands;

import org.example.TCP_components.Response;
import org.example.database.CollectionTableHelper;
import org.example.database.DBHelper;
import org.example.interfaces.Console;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CollectionManager;
import org.example.model.Person;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Info extends Info_Description implements ServerExecutable {
    CollectionManager collectionManager;
    Console console;
    CollectionTableHelper collectionTableHelper;
    DBHelper dbHelper;

    public Info(CollectionManager collectionManager, Console console, CollectionTableHelper collectionTableHelper, DBHelper dbHelper) {
        super();
        this.collectionManager = collectionManager;
        this.console = console;
        this.collectionTableHelper = collectionTableHelper;
        this.dbHelper = dbHelper;
    }

    @Override
    public Response apply(String userCommandArgument, long userID) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Информация о коллекции и базе данных").append("\n")
                .append("==================================================").append("\n\n")
                .append("Информация о коллекции:").append("\n")
                .append("--------------------------------------------------").append("\n")
                .append("Тип коллекции: ").append(collectionManager.getCollection().getClass().getSimpleName()).append("\n")
                .append("Количество элементов в памяти: ").append(collectionManager.getCollection().size()).append("\n");
        try {
            stringBuilder.append("Время последней инициализации: ")
                    .append(collectionManager.getLastInitTime().format(Person.timeFormatter)).append("\n");
        } catch (NullPointerException e) {
            stringBuilder.append("Инициализация не происходила").append("\n");
        }

        stringBuilder.append("\nИнформация о базе данных:").append("\n")
                .append("--------------------------------------------------").append("\n");
        try {
            stringBuilder.append("URL базы данных: ").append(collectionTableHelper.getDbHelper().getURL()).append("\n")
                    .append("Имя базы данных: ").append(collectionTableHelper.getDbHelper().getDatabaseName()).append("\n")
                    .append("Схема: ").append(collectionTableHelper.getDbHelper().getSchemaName()).append("\n")
                    .append("Имя таблицы: ").append(collectionTableHelper.getTableName()).append("\n")
                    .append("Количество записей в таблице: ").append(collectionTableHelper.getTableRowCount()).append("\n");

            LocalDateTime lastModTime = collectionTableHelper.getLastModificationTime();
            if (lastModTime != null) {
                stringBuilder.append("Время последней модификации: ").append(lastModTime.format(Person.timeFormatter)).append("\n");
            } else {
                stringBuilder.append("Таблица пуста").append("\n");
            }
        } catch (SQLException e) {
            stringBuilder.append("Ошибка при получении информации о базе данных: ").append(e.getMessage()).append("\n");
        }

        stringBuilder.append("\nСравнение данных в памяти и в базе данных:").append("\n")
                .append("--------------------------------------------------").append("\n");
        try {
            int dbCount = collectionTableHelper.getTableRowCount();
            int memoryCount = collectionManager.getCollection().size();
            stringBuilder.append("Количество элементов в базе данных: ").append(dbCount).append("\n")
                    .append("Количество элементов в памяти: ").append(memoryCount).append("\n");
            if (dbCount == memoryCount) {
                stringBuilder.append("Данные в памяти и базе данных синхронизированы").append("\n");
            } else {
                stringBuilder.append("Внимание: Количество элементов в памяти и базе данных не совпадает!").append("\n");
            }
        } catch (SQLException e) {
            stringBuilder.append("Ошибка при сравнении данных: ").append(e.getMessage()).append("\n");
        }

        stringBuilder.append("\n==================================================");

        return new Response(true, "Информация о коллекции и базе данных получена", stringBuilder.toString());
    }
}

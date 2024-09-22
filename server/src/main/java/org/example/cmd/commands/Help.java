package org.example.cmd.commands;

import org.example.TCP_components.Response;
import org.example.interfaces.ServerExecutable;
import org.example.managers.CommandManager;
import org.example.interfaces.Describable;

import java.util.Map;

public class Help extends Help_Description implements ServerExecutable {
    private CommandManager commandManager;
    public Help(CommandManager commandManager) {
        super();
        this.commandManager = commandManager;
    }

    @Override
    public Response apply(String userCommandArgument, long userID) {
        if (commandManager.getCommands().isEmpty()) {
            return new Response(true, "Список доступных команд пуст", "");
        }
        StringBuilder stringBuilder = new StringBuilder("\n");
        for (Map.Entry<String, Describable> entry : commandManager.getCommands().entrySet() ){
            stringBuilder.append(entry.getValue().getName()).append(" - ").append(entry.getValue().getDescription())
                    .append("\n");
        }
        return new Response(true, "Список доступных команд успешно выведен", stringBuilder.toString());
    }
}

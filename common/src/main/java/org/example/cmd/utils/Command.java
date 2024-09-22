package org.example.cmd.utils;


import org.example.interfaces.Describable;

public abstract class Command implements Describable {

    private final String name;

    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}

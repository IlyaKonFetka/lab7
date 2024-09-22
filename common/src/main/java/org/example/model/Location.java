package org.example.model;

import org.example.interfaces.Validatable;

public class Location implements Validatable {
    private float x;
    private Integer y; //Поле не может быть null
    private String name; //Строка не может быть пустой, Поле не может быть null

    public Location() {}

    public Location(float x, Integer y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    @Override
    public boolean validate() {
        if (y == null) return false;
        if (name == null) return false;
        if (name.isEmpty())return false;
        return true;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " (" + x + ", " + y + ")";
    }
}


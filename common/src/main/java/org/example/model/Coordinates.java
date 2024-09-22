package org.example.model;

import org.example.interfaces.Validatable;

public class Coordinates implements Validatable {
    private int x;
    private Float y; //Поле не может быть null

    public Coordinates(){};

    public Coordinates(int x, Float y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean validate() {
        return y != null;
    }
}

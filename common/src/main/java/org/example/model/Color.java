package org.example.model;

public enum Color {
    GREEN,
    BLUE,
    YELLOW;

    public static String getNames(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Color i: values()){
            stringBuilder.append(i.name()).append(", ");
        }
        return stringBuilder.substring(0,stringBuilder.length()-2);
    }
    @Override
    public String toString() {
        return this.name();
    }
}
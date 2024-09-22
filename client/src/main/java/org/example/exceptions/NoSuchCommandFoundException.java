package org.example.exceptions;

public class NoSuchCommandFoundException extends Exception{
    public NoSuchCommandFoundException() {
    }

    public NoSuchCommandFoundException(String message) {
        super(message);
    }
}

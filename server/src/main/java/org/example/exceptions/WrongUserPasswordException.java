package org.example.exceptions;

public class WrongUserPasswordException extends Exception{
    public WrongUserPasswordException(String message) {
        super(message);
    }
}

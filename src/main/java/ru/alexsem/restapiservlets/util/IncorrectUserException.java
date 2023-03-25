package ru.alexsem.restapiservlets.util;

public class IncorrectUserException extends RuntimeException{
    public IncorrectUserException(String message) {
        super(message);
    }
}

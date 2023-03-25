package ru.alexsem.restapiservlets.util;

public class IncorrectParameterException extends RuntimeException{
    public IncorrectParameterException(String message) {
        super(message);
    }
}

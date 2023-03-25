package ru.alexsem.restapiservlets.util;

public class IncorrectURLException extends RuntimeException{
    public IncorrectURLException(String message) {
        super(message);
    }
}

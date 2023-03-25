package ru.alexsem.restapiservlets.util;

public class IncorrectFileException extends RuntimeException {
    public IncorrectFileException(String message) {
        super(message);
    }
}

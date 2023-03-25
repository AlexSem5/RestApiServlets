package ru.alexsem.restapiservlets.util;

public class FileNotCreatedException extends RuntimeException{
    public FileNotCreatedException(String message) {
        super(message);
    }
}

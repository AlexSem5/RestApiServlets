package ru.alexsem.restapiservlets.config;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperClass {
    private static ObjectMapper objectMapper;
    
    private ObjectMapperClass() {
    }
    
    public static ObjectMapper getInstance() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
}

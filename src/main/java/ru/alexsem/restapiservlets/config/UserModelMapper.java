package ru.alexsem.restapiservlets.config;

import org.modelmapper.ModelMapper;

public class UserModelMapper {
    
    private static ModelMapper modelMapper;
    
    private UserModelMapper() {
    }
    
    public static ModelMapper getInstance() {
        if (modelMapper == null) {
            modelMapper = new ModelMapper();
        }
        return modelMapper;
    }
}

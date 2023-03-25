package ru.alexsem.restapiservlets.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDTO {
    private String name;
    private String filePath;
}

package ru.alexsem.restapiservlets.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.alexsem.restapiservlets.models.File;
import ru.alexsem.restapiservlets.models.User;

@Getter
@Setter
@ToString
public class EventDTO {
    private User user;
    private File file;
}

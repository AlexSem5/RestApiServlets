package ru.alexsem.restapiservlets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import ru.alexsem.restapiservlets.config.ObjectMapperClass;
import ru.alexsem.restapiservlets.config.UserModelMapper;
import ru.alexsem.restapiservlets.dao.EventDAO;
import ru.alexsem.restapiservlets.dao.FileDAO;
import ru.alexsem.restapiservlets.dto.EventDTO;
import ru.alexsem.restapiservlets.dto.FileDTO;
import ru.alexsem.restapiservlets.models.Event;
import ru.alexsem.restapiservlets.models.File;
import ru.alexsem.restapiservlets.util.FileNotCreatedException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create Event,
 * Read Events
 */
@WebServlet(name = "getPostEvents", value = "/api/v1/events")
public class EventGetPostServlet extends HttpServlet {
    private EventDAO eventDAO;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        eventDAO = EventDAO.getInstance();
        modelMapper = UserModelMapper.getInstance();
        objectMapper = ObjectMapperClass.getInstance();
    }
    
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        List<EventDTO> eventDTOS = eventDAO.index()
                                          .stream()
                                          .map(event -> modelMapper.map(event, EventDTO.class))
                                          .toList();
        PrintWriter out = resp.getWriter();
        out.println(objectMapper.writeValueAsString(eventDTOS));
    }
    
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        String jsonBody = new BufferedReader(
                new InputStreamReader(req.getInputStream()))
                .lines()
                .collect(Collectors.joining("\n"));
        EventDTO eventDTO = objectMapper.readValue(jsonBody, EventDTO.class);
        eventDAO.save(modelMapper.map(eventDTO, Event.class));
    }
}

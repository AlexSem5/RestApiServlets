package ru.alexsem.restapiservlets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import ru.alexsem.restapiservlets.config.ObjectMapperClass;
import ru.alexsem.restapiservlets.config.UserModelMapper;
import ru.alexsem.restapiservlets.dao.EventDAO;
import ru.alexsem.restapiservlets.dao.FileDAO;
import ru.alexsem.restapiservlets.dto.EventDTO;
import ru.alexsem.restapiservlets.dto.UserDTO;
import ru.alexsem.restapiservlets.models.Event;
import ru.alexsem.restapiservlets.models.User;
import ru.alexsem.restapiservlets.util.IncorrectURLException;
import ru.alexsem.restapiservlets.util.IncorrectUserException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

/**
 * Read, Update, Delete Event
 */
@WebServlet(name = "getPutDeleteEvents", value = "/api/v1/events/*")
public class EventGetPutDeleteServlet extends HttpServlet {
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
        int id = getId(req);
        Event event = eventDAO.show(id);
        PrintWriter out = resp.getWriter();
        out.println(objectMapper.writeValueAsString(
                modelMapper.map(event, EventDTO.class)));
    }
    
    @Override
    protected void doPut(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        String jsonBody = new BufferedReader(
                new InputStreamReader(req.getInputStream()))
                .lines()
                .collect(Collectors.joining("\n"));
        EventDTO eventDTO = objectMapper.readValue(jsonBody, EventDTO.class);
        int id = getId(req);
        eventDAO.update(id, modelMapper.map(eventDTO, Event.class));
    }
    
    @Override
    protected void doDelete(HttpServletRequest req,
                            HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        int id = getId(req);
        eventDAO.delete(id);
    }
    
    private int getId(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        String[] path = pathInfo.split("/");
        if (path[1] == null) {
            throw new IncorrectURLException("Incorrect URL");
        }
        return Integer.parseInt(path[1]);
    }
}

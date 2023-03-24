package ru.alexsem.restapiservlets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import ru.alexsem.restapiservlets.dto.UserDTO;
import ru.alexsem.restapiservlets.config.ObjectMapperClass;
import ru.alexsem.restapiservlets.config.UserModelMapper;
import ru.alexsem.restapiservlets.dao.UserDAO;
import ru.alexsem.restapiservlets.models.User;
import ru.alexsem.restapiservlets.util.UserNotCreatedException;

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
import java.util.stream.Stream;

/**
 * Create User,
 * Read User
 */
@WebServlet(name = "getPostUsers", value = "/api/v1/users")
public class UserGetPostServlet extends HttpServlet {
    private UserDAO userDAO;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        userDAO = UserDAO.getInstance();
        modelMapper = UserModelMapper.getInstance();
        objectMapper = ObjectMapperClass.getInstance();
    }
    
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        List<UserDTO> userDTOS = userDAO.index()
                                        .stream()
                                        .map(user -> modelMapper.map(user, UserDTO.class))
                                        .collect(Collectors.toList());
        PrintWriter out = resp.getWriter();
        out.println(objectMapper.writeValueAsString(userDTOS));
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
        if (jsonBody == null || jsonBody.isBlank()) {
            throw new UserNotCreatedException("User was not created");
        }
        UserDTO userDTO = objectMapper.readValue(jsonBody, UserDTO.class);
        userDAO.save(modelMapper.map(userDTO, User.class));
    }
}

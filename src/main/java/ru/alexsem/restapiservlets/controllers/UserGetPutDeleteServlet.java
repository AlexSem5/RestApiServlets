package ru.alexsem.restapiservlets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import ru.alexsem.restapiservlets.config.ObjectMapperClass;
import ru.alexsem.restapiservlets.config.UserModelMapper;
import ru.alexsem.restapiservlets.dao.UserDAO;
import ru.alexsem.restapiservlets.dto.UserDTO;
import ru.alexsem.restapiservlets.models.User;
import ru.alexsem.restapiservlets.util.IncorrectParameterException;
import ru.alexsem.restapiservlets.util.UserNotCreatedException;
import ru.alexsem.restapiservlets.util.UserNotFoundException;

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

@WebServlet(name = "getPupDeleteUsers", value = "/api/v1/users/*")
public class UserGetPutDeleteServlet extends HttpServlet {
    
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
        int id = getId(req);
        User user = userDAO.show(id);
        PrintWriter out = resp.getWriter();
        out.println(objectMapper.writeValueAsString(
                modelMapper.map(user, UserDTO.class)));
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String jsonBody = new BufferedReader(
                new InputStreamReader(req.getInputStream()))
                .lines()
                .collect(Collectors.joining("\n"));
        if (jsonBody == null || jsonBody.isBlank()) {
            throw new UserNotFoundException("User was not found");
        }
        UserDTO userDTO = objectMapper.readValue(jsonBody, UserDTO.class);
        int id = getId(req);
        userDAO.update(id, modelMapper.map(userDTO, User.class));
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        int id = getId(req);
        userDAO.delete(id);
    }
    
    private int getId(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        String[] path = pathInfo.split("/");
        if (path[1] == null) {
            throw new IncorrectParameterException("Incorrect parameter");
        }
        return Integer.parseInt(path[1]);
    }
}

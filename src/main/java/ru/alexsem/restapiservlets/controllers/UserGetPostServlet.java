package ru.alexsem.restapiservlets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import ru.alexsem.restapiservlets.DTO.UserDTO;
import ru.alexsem.restapiservlets.config.ObjectMapperClass;
import ru.alexsem.restapiservlets.config.UserModelMapper;
import ru.alexsem.restapiservlets.dao.UserDAO;
import ru.alexsem.restapiservlets.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "getUsers", value = "/api/v1/users")
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
        resp.setContentType("text/html");
        List<User> users = userDAO.index();
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        users.forEach(user -> {
            try {
                out.println(objectMapper.writeValueAsString(
                        modelMapper.map(user, UserDTO.class)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        out.println("</body></html>");
    }
}

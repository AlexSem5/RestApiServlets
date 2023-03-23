package ru.alexsem.restapiservlets.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class GetAllUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        List<User> users = UserDAO.getInstance().index();
        PrintWriter out = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        out.println("<html><body>");
        users.forEach(user -> {
            try {
                out.println(objectMapper.writeValueAsString(user));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        out.println("</body></html>");
    }
}

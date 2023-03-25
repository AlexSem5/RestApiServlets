package ru.alexsem.restapiservlets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import ru.alexsem.restapiservlets.config.ObjectMapperClass;
import ru.alexsem.restapiservlets.config.UserModelMapper;
import ru.alexsem.restapiservlets.dao.FileDAO;
import ru.alexsem.restapiservlets.dao.UserDAO;
import ru.alexsem.restapiservlets.dto.FileDTO;
import ru.alexsem.restapiservlets.dto.UserDTO;
import ru.alexsem.restapiservlets.models.File;
import ru.alexsem.restapiservlets.models.User;
import ru.alexsem.restapiservlets.util.FileNotCreatedException;
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

/**
 * Create File,
 * Read Files
 */
@WebServlet(name = "getPostFiles", value = "/api/v1/files")
public class FileGetPostServlet extends HttpServlet {
    private FileDAO fileDAO;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        fileDAO = FileDAO.getInstance();
        modelMapper = UserModelMapper.getInstance();
        objectMapper = ObjectMapperClass.getInstance();
    }
    
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        List<FileDTO> fileDTOS = fileDAO.index()
                                        .stream()
                                        .map(file -> modelMapper.map(file, FileDTO.class))
                                        .toList();
        PrintWriter out = resp.getWriter();
        out.println(objectMapper.writeValueAsString(fileDTOS));
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
            throw new FileNotCreatedException("File was not created");
        }
        FileDTO fileDTO = objectMapper.readValue(jsonBody, FileDTO.class);
        fileDAO.save(modelMapper.map(fileDTO, File.class));
    }
}
